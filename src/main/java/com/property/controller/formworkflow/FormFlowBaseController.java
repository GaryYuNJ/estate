package com.property.controller.formworkflow;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.property.model.*;
import com.property.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.property.business.*;
import com.property.data.APIMessage;
import com.property.data.FlowConfigContent;
import com.property.data.FlowConfigContentFormField;
import com.property.data.FormDefineFlowConfig;
import com.property.data.FormField;
import com.property.utils.BPMModelHelper;
import com.property.utils.CommonHelper;

@Controller
@RequestMapping(value = "FormFlowBase")
public class FormFlowBaseController {
	@Resource
	private IFormDynService formDynService;
	@Resource
	private IFormValuesService formValuesService;
	@Resource
	private IFormService formService;
	@Resource
	private IActivitiService activitiService;
	@Resource
	private IBuserService buserService;
	@Resource
	private ActivitiHelper activitiHelper;
	@Resource
	private BPMModelHelper bpmmodelHelper;
	@Resource
	private FormFlowHelper formFlowHelper;
	@Resource
	private IFlowConfigService flowconfigService;
	@Autowired
	private RepositoryService aRepositoryService;
	@Autowired
	private TaskService aTaskService;
	@Autowired
	private RuntimeService aRuntimeService;
	@Autowired
	private HistoryService aHistoryService;

	/**
	 * 根据审批id 获取activiti 流程定义
	 */
	@RequestMapping(value = "/getProcessDefineID/{formdefineid}")
	@ResponseBody
	public APIMessage getProcessDefineID(@PathVariable("formdefineid") String formdefineid) {
		int id = Integer.parseInt(formdefineid);

		APIMessage result = new APIMessage();

		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);

		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(formTableDefine.getTableNameEn()).latestVersion().singleResult();

		if (processDefinition == null) {
			result.setStatus(0);
			result.setMessage("找不到流程定义！请先使用设计器配置流程并部署");
		} else {
			result.setStatus(1);
			result.setContent(processDefinition.getId());
		}

		return result;
	}

	/**
	 * 启用审批
	 */
	@RequestMapping(value = "/startFormFlow/{FormFlowID}", method = RequestMethod.POST)
	@ResponseBody
	public Object startFormFlow(@PathVariable("FormFlowID") String FormFlowID) {
		APIMessage resultMessage = new APIMessage();// 返回只
		FormTableDefine formTableDefine = formService.getFormTableDefineById(Integer.parseInt(FormFlowID));// formdefine

		if (formTableDefine == null) {
			resultMessage.setStatus(0);
			resultMessage.setMessage("流程不存在");
			return resultMessage;
		}

		/* 表单字段验证 start */
		List<FormField> formFields = formTableDefine.getFormFields();
		if (formFields == null || formFields.isEmpty()) {
			resultMessage.setStatus(0);
			resultMessage.setMessage("表单未设置");
			return resultMessage;
		}
		/* 表单字段验证 end */

		/* 流程验证 start */
		FormDefineFlowConfig formDefineFlowConfig = formTableDefine.getFormDefineFlowConfig();

		switch (formDefineFlowConfig.getFlowType()) {
			case "complex":
				List<ProcessDefinition> processDefinition = aRepositoryService.createProcessDefinitionQuery()
						.processDefinitionKey(formTableDefine.getTableNameEn()).list();
				if (processDefinition == null || processDefinition.size() <= 0) {
					resultMessage.setStatus(0);
					resultMessage.setMessage("流程未启用");
					return resultMessage;
				}
				break;
			case "simple":
				activitiHelper.startSimpleActiviti(formTableDefine.getTableNameEn(), formTableDefine.getTableName(),
						formDefineFlowConfig.getContent(), formFields);
				break;
			default:
				break;
		}

		/* 流程验证 end */

		// 更新formdefine
		formTableDefine.setState(2);
		formService.updateFormTableDefineById(formTableDefine);

		resultMessage.setStatus(1);
		return resultMessage;
	}

	/**
	 * 停用审批
	 */
	@RequestMapping(value = "/stopFormFlow/{FormFlowID}", method = RequestMethod.POST)
	@ResponseBody
	public Object stopFormFlow(@PathVariable("FormFlowID") String FormFlowID) {
		APIMessage resultMessage = new APIMessage();// 返回只
		FormTableDefine formTableDefine = formService.getFormTableDefineById(Integer.parseInt(FormFlowID));// formdefine

		if (formTableDefine == null) {
			resultMessage.setStatus(0);
			resultMessage.setMessage("流程不存在");
			return resultMessage;
		}

		// 更新formdefine
		formTableDefine.setState(3);
		formService.updateFormTableDefineById(formTableDefine);

		resultMessage.setStatus(1);
		return resultMessage;
	}

	// 动态表单插入数据，发起审批实例
	@RequestMapping(value = "/insertForm.json", method = RequestMethod.POST)
	@ResponseBody
	public Object insertForm(@RequestParam("form") String form, @RequestParam("user") String user,
			@RequestParam("id") Integer id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);
		String tableNameEn = formTableDefine.getTableNameEn();
		Integer formdefineId = formTableDefine.getId();

		// formvalues插入
		FormValues formValues = new FormValues();
		String uuid = UUID.randomUUID().toString();
		formValues.setID(uuid);
		formValues.setFormdefineid(formdefineId);
		formValues.settablename_en(tableNameEn);
		formValues.setcreatetime(new Date());
		formValues.setcreateuser(user);
		formValues.setstatus(1);
		formValues.setvalues(form);

		Object fields = JSONArray.parse(form);
		List<FormField> formValuesList = JSON.parseArray(fields + "", FormField.class);// 获取表单提交的值
		Map<String, Object> mapValues = new HashMap<String, Object>();// 流程变量
		String result = activitiHelper.saveStartProcess(formTableDefine, uuid, user, mapValues, formValuesList);
		if (!result.isEmpty()) {
			resultMap.put("error", result);
			return resultMap;
		}

		formValuesService.insertFormValues(formValues);

		resultMap.put("Success", true);

		return resultMap;
	}

	/**
	 * 获取表单定义，配置
	 */
	@RequestMapping(value = "/getFormById.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getFormById(@RequestParam("id") Integer id, @RequestParam("taskid") String taskid) {
		Map<String, Object> result = new HashMap<String, Object>();

		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		List<FormField> formFields = formTableDefine.getFormFields();// 表单定义list

		FlowConfigContent flowConfigContent = getTaskConfig(formTableDefine, taskid);
		List<FormField> formFieldsResult = new ArrayList<FormField>();
		if (flowConfigContent != null)
			formFieldsResult = getTaskFormFields(formFields, flowConfigContent.getFormfields());

		result.put("tableName", formTableDefine.getTableName());
		result.put("fieldsJson", formFieldsResult);
		return result;
	}

	/**
	 * 通过businesskey查询表单详情
	 */
	@RequestMapping(value = "/getFormByBusinessKey.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getFormByBusinessKey(@RequestParam("objId") String objId, @RequestParam("taskid") String taskid) {
		Map<String, Object> map = new HashMap<String, Object>();

		FormValues formValues = formValuesService.getFormValuesById(objId);
		FormTableDefine formTableDefine = formService.getFormTableDefineById(formValues.getFormdefineid());

		Object fields = JSONArray.parse(formValues.getvalues());
		List<FormField> formValuesList = JSON.parseArray(fields + "", FormField.class);// 获取表单提交的值
		List<FormField> formFieldsDefine = formTableDefine.getFormFields();// 获取表单定义

		Task task = aTaskService.createTaskQuery().taskId(taskid).singleResult();
		FlowConfigContent flowConfigContent = getTaskConfig(formTableDefine, task.getTaskDefinitionKey());// 获取当前task的配置

		List<FormField> formFieldsResult = getTaskFormFields(formFieldsDefine, flowConfigContent.getFormfields());

		for (FormField item : formFieldsResult) {
			for (FormField itemValues : formValuesList) {
				if (itemValues.getCid().equals(item.getCid())) {
					item.setField_options(itemValues.getField_options());
					item.setValue(itemValues.getValue());
				}
			}
		}

		map.put("tableName", formTableDefine.getTableName());
		map.put("inputUser", formValues.getcreateuser());
		map.put("fieldsJson", formFieldsResult);
		map.put("taskStartTime", task.getCreateTime());
		HistoricProcessInstance processInstance = aHistoryService.createHistoricProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		map.put("processInstanceStartTime", processInstance.getStartTime());

		return map;
	}

	/**
	 * 通过businesskey查询表单详情
	 */
	@RequestMapping(value = "/getInstanceDetialByBusinessKey.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getInstanceDetialByBusinessKey(@RequestParam("objId") String objId,
			@RequestParam("processinstanceid") String processinstanceid, @RequestParam("user") String user) {
		Map<String, Object> map = new HashMap<String, Object>();

		FormValues formValues = formValuesService.getFormValuesById(objId);
		FormTableDefine formTableDefine = formService.getFormTableDefineById(formValues.getFormdefineid());

		Object fields = JSONArray.parse(formValues.getvalues());
		List<FormField> formValuesList = JSON.parseArray(fields + "", FormField.class);// 获取表单提交的值
		List<FormField> formFieldsDefine = formTableDefine.getFormFields();// 获取表单定义

		List<HistoricTaskInstance> tasklist = aHistoryService.createHistoricTaskInstanceQuery()
				.processInstanceId(processinstanceid).taskAssignee(user).list();
		List<FormField> formFieldsResult = new ArrayList<FormField>();
		for (HistoricTaskInstance taskItem : tasklist) {
			FlowConfigContent flowConfigContent = getTaskConfig(formTableDefine, taskItem.getTaskDefinitionKey());// 获取当前task的配置
			formFieldsResult.addAll(getTaskFormFields(formFieldsDefine, flowConfigContent.getFormfields()));// todo
		}

		List<String> cidTemp = new ArrayList<String>();
		List<FormField> formFields = new ArrayList<FormField>();
		for (FormField item : formFieldsResult) {
			if (cidTemp.contains(item.getCid()))
				continue;
			formFields.add(item);
			cidTemp.add(item.getCid());
		}

		for (FormField item : formFields) {
			for (FormField itemValues : formValuesList) {
				if (itemValues.getCid().equals(item.getCid())) {
					item.setField_options(itemValues.getField_options());
					item.setValue(itemValues.getValue());
				}
			}
		}

		map.put("tableName", formTableDefine.getTableName());
		map.put("inputUser", formValues.getcreateuser());
		map.put("fieldsJson", formFields);

		return map;
	}

	// 审批
	@RequestMapping(value = "/pushWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object pushWorkFlow(@RequestParam("form") String form, @RequestParam("user") String user,
			@RequestParam("taskid") String taskid, @RequestParam("remark") String remark,
			@RequestParam("objId") String objId, @RequestParam("isReSubmit") Boolean isReSubmit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		FormValues formValues = formValuesService.getFormValuesById(objId);
		List<FormField> oldFormValuesList = JSON.parseArray(JSONArray.parse(formValues.getvalues()) + "",
				FormField.class);// 原表单数据
		List<FormField> newFormValuesList = JSON.parseArray(JSONArray.parse(form) + "", FormField.class);// 最新提交的表单数据

		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(formValues.getFormdefineid());
		Task task = aTaskService.createTaskQuery().taskId(taskid).singleResult();
		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(formTableDefine.getTableNameEn()).latestVersion().singleResult();
		List<FlowConfig> flowConfiglist = flowconfigService.getFlowConfigByPRID(processDefinition.getId());
		FlowConfig flowConfig = null;
		FlowConfigContent flowConfigContent = new FlowConfigContent();
		if (!flowConfiglist.isEmpty()) {
			flowConfig = flowConfiglist.get(0);
			flowConfigContent = CommonHelper.getFlowConfigContent(flowConfig.getcontent(), task.getTaskDefinitionKey());
		}

		List<String> cidTobeUpdate = new ArrayList<String>();// write 权限的字段cid集合
		for (FlowConfigContentFormField item : flowConfigContent.getFormfields()) {
			if (item.getValue().equals("write")) {
				cidTobeUpdate.add(item.getCid());
			}
		}

		for (FormField item : newFormValuesList) {
			if (cidTobeUpdate.contains(item.getCid())) {
				Boolean isExist = false;
				for (FormField itemvalue : oldFormValuesList) {
					if (itemvalue.getCid().equals(item.getCid())) {
						itemvalue.setValue(item.getValue());
						itemvalue.setField_options(item.getField_options());
						isExist = true;
					}
				}
				if (!isExist) {
					oldFormValuesList.add(item);
				}
			}
		}

		formValues.setvalues(JSON.toJSONString(oldFormValuesList));
		formValues.setUpdatetime(new Date());
		formValues.setupdateuser(user);
		formValuesService.updateFormValues(formValues);

		remark = isReSubmit ? "重新提交" : (remark.isEmpty() ? "同意" : "同意(" + remark + ")");
		activitiService.saveSubmitTask(remark, taskid, user, null, oldFormValuesList);

		resultMap.put("Success", true);

		return resultMap;
	}

	// 审批
	@RequestMapping(value = "/transferAssignee.json", method = RequestMethod.POST)
	@ResponseBody
	public Object transferAssignee(@RequestParam("transferUser") String transferUser,
			@RequestParam("taskid") String taskid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		activitiService.transferAssignee(taskid, transferUser);

		resultMap.put("Success", true);

		return resultMap;
	}

	// simple 方式设置流程
	@RequestMapping(value = "/setWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object setWorkFlow(@RequestParam("activitiJson") String activitiJson,
			@RequestParam("formID") Integer formID) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(formID);
		if (formTableDefine == null)
			return "{\"error\":\"formID不存在\"}";

		FormDefineFlowConfig formDefineFlowConfig = new FormDefineFlowConfig();
		formDefineFlowConfig.setContent(JSONObject.parseObject(activitiJson));
		formDefineFlowConfig.setFlowStatus(0);
		formDefineFlowConfig.setFlowType("simple");

		Date dateNow = new Date();
		formTableDefine.setActivitiJson(JSON.toJSONString(formDefineFlowConfig));
		formTableDefine.setState(1);// 设置为初始状态
		formTableDefine.setUpdatetime(dateNow);
		formService.updateFormTableDefineById(formTableDefine);
		return "{\"result\":\"success\"}";
	}

	/**
	 * 获取指定task的配置
	 */
	private FlowConfigContent getTaskConfig(FormTableDefine formTableDefine, String taskid) {
		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(formTableDefine.getTableNameEn()).latestVersion().singleResult();

		List<FlowConfig> flowConfiglist = flowconfigService.getFlowConfigByPRID(processDefinition.getId());
		FlowConfig flowConfig = null;
		FlowConfigContent flowConfigContent = null;
		if (!flowConfiglist.isEmpty()) {
			flowConfig = flowConfiglist.get(0);
			flowConfigContent = CommonHelper.getFlowConfigContent(flowConfig.getcontent(), taskid);
		}
		return flowConfigContent;
	}

	/**
	 * 按照配置获取需要输出的字段
	 */
	private List<FormField> getTaskFormFields(List<FormField> formFieldsDefine,
			List<FlowConfigContentFormField> formFieldsConfig) {
		List<FormField> formFieldsResult = new ArrayList<FormField>();
		for (FormField item1 : formFieldsDefine) {
			for (FlowConfigContentFormField item2 : formFieldsConfig) {
				if (item1.getCid().equals(item2.getCid())
						&& (item2.getValue().equals("read") || item2.getValue().equals("write"))) {
					item1.setAccess(item2.getValue());
					formFieldsResult.add(item1);
				}
			}
		}
		return formFieldsResult;
	}
}
