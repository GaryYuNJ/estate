package com.property.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.enterprise.inject.New;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.db.DbSchemaUpdate;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.property.activiti.DynamicProcessCustomJsonResolve;
import com.property.activiti.DynamicProcessElementCreate;
import com.property.constants.ActivitiDefineConstants;
import com.property.constants.CustomProcessJsonConstants;
import com.property.data.APIMessage;
import com.property.data.FlowConfigContent;
import com.property.data.FlowConfigContentFormField;
import com.property.data.FlowConfigContentParam;
import com.property.data.FormField;
import com.property.model.BUser;
import com.property.model.FlowConfig;
import com.property.model.FormTableDefine;
import com.property.service.IActivitiService;
import com.property.service.IBuserService;
import com.property.service.IFlowConfigService;
import com.property.utils.CommonHelper;

@Service("activitiHelper")
public class ActivitiHelper {
	@Resource
	private IActivitiService activitiService;
	@Resource
	private IFlowConfigService flowconfigService;
	@Autowired
	private RepositoryService aRepositoryService;
	@Resource
	private IBuserService buserService;

	/**
	 * 获取流程实例当前的状态
	 */
	@SuppressWarnings("unchecked")
	public String getProcessInstanceStateById(String piId) {
		APIMessage state = activitiService.getProcessInstanceStateById(piId);
		String stateStr = "";
		if (state.getStatus() == 0)
			stateStr = "结束";
		if (state.getStatus() == 1)
			stateStr = "等待 " + StringUtils.join((List<String>) state.getContent(), "/") + " 审批";
		if (state.getStatus() == -1)
			stateStr = "系统故障，无法获取当前状态";
		return stateStr;
	}

	/**
	 * 获取用户发起的审批流程
	 */
	public Object getHisUserInputTaskByUser(String user) {
		List<HistoricTaskInstance> list = activitiService.findHisTaskListByName(user, "userinputtask");

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();// 返回结果集合
		List<String> processinstancesID = new ArrayList<String>();// 存放流程实例ID，避免出现重复的流程实例，当被驳回至提交人时一个流程实例会出现两个userinputtask

		for (HistoricTaskInstance task : list) {
			if (processinstancesID.contains(task.getProcessInstanceId())) {
				continue;// 当前流程实例已经存在，continue
			} else {
				processinstancesID.add(task.getProcessInstanceId());
				Map<String, Object> mapTask = new HashMap<String, Object>();
				// 获取该流程实例的所有流程变量
				List<HistoricVariableInstance> variables = activitiService
						.getVariablesByPiID(task.getProcessInstanceId());
				for (HistoricVariableInstance hisV : variables) {
					switch (hisV.getVariableName()) {
					// 该流程实例对应审批的名称
					case "tableName":
						mapTask.put("tablename", hisV.getValue());
						break;
					// businesskey
					case "objId":
						mapTask.put("objId", hisV.getValue());
						break;
					default:
						break;
					}
				}

				mapTask.put("state", getProcessInstanceStateById(task.getProcessInstanceId()));
				mapTask.put("piId", task.getProcessInstanceId());
				mapTask.put("StartTime", task.getStartTime());

				results.add(mapTask);
			}
		}
		return results;
	}

	/**
	 * 启动流程实例，让启动的流程实例关联业务
	 * 
	 * @param formTableDefine：表单流程定义
	 * @param id：表单数据主键id
	 * @param user：提交表单的用户
	 * @param mapValues：流程变量
	 */
	public String saveStartProcess(FormTableDefine formTableDefine, String id, String user,
			Map<String, Object> mapValues, List<FormField> formValuesList) {
		String resultmessage = "";
		// 使用当前对象获取到流程定义的key（对象的名称就是流程定义的key）
		String key = formTableDefine.getTableNameEn();
		// businesskey，用于流程实例 和 外置表单 数据的关联
		String objId = id;

		mapValues.put("tableName", formTableDefine.getTableName());

		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(formTableDefine.getTableNameEn()).latestVersion().singleResult();
		List<FlowConfig> flowConfiglist = flowconfigService.getFlowConfigByPRID(processDefinition.getId());
		FlowConfig flowConfig = null;
		List<FlowConfigContent> flowConfigContents = new ArrayList<FlowConfigContent>();
		if (!flowConfiglist.isEmpty()) {
			flowConfig = flowConfiglist.get(0);
			flowConfigContents = CommonHelper.getFlowConfigContent(flowConfig.getcontent());
		}
		for (FlowConfigContent flowConfigContent : flowConfigContents) {
			List<FlowConfigContentParam> flowConfigContentParams = flowConfigContent.getParams();
			for (FlowConfigContentParam item : flowConfigContentParams) {
				String value = getExpressionValue(item, user);
				if (value.startsWith("@error:")) {
					resultmessage += value.split(":")[1] + ",";
				} else
					mapValues.put(item.getExpression().replace("${", "").replace("}", ""), value);
			}
		}

		if (!resultmessage.isEmpty()) {
			return resultmessage;
		}

		activitiService.startProcessAndCommitInput(key, objId, user, mapValues, formValuesList);

		return "";
	}

	/**
	 * 启用(简单配置方式)流程
	 * 
	 * @param jsonObject
	 *            activitiJson.content
	 */
	public void startSimpleActiviti(String tableNameEn, String tableName, JSONObject jsonObject,
			List<FormField> formFields) {
		DynamicProcessCustomJsonResolve dynamicProcessCustomJsonResolve = new DynamicProcessCustomJsonResolve(
				jsonObject);
		// 生成流程定义
		Process process = deploySimpleFlow(tableNameEn, tableName, dynamicProcessCustomJsonResolve);

		// 获取流程定义id
		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(tableNameEn).latestVersion().singleResult();

		// 生成默认flowconfig
		List<FlowConfigContent> flowConfigContents = setSimpleFlowConfig(process, formFields,
				dynamicProcessCustomJsonResolve.getAssignParams());
		FlowConfig flowConfig = new FlowConfig();
		flowConfig.setconfig_type("autogenerate");
		flowConfig.setcontent(JSON.toJSONString(flowConfigContents));
		flowConfig.setcreatetime(new Date());
		flowConfig.setcreateuser("admin");
		flowConfig.setprocessdefine_id(processDefinition.getId());
		flowConfig.setprocessdefine_key(tableNameEn);
		flowConfig.setprocessdefine_version(processDefinition.getVersion());
		flowConfig.setstatus(1);
		flowConfig.setUpdatetime(new Date());
		flowConfig.setupdateuser("admin");
		flowconfigService.insertFlowConfig(flowConfig);
	}

	/**
	 * 设置流程变量
	 */
	private String getExpressionValue(FlowConfigContentParam flowConfigContentParam, String user) {
		String result = "";
		switch (flowConfigContentParam.getType()) {
		case CustomProcessJsonConstants.FLOW_CONFIG_PARAMS_TYPE_DUEDATE:
			String pattern = "yyyy-MM-dd'T'HH:mm:ss";
			SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
			Calendar duedateCal = Calendar.getInstance();
			duedateCal.add(Calendar.MINUTE, Integer.parseInt(flowConfigContentParam.getValue()));
			Date duedate = (Date) duedateCal.getTime();
			result = sFormat.format(duedate);
			break;
		case CustomProcessJsonConstants.FLOW_CONFIG_PARAMS_TYPE_ASSIGN_NEXTLEVEL:
			switch (flowConfigContentParam.getValue()) {
			case "@@AssignToNextLevel_1":
				List<BUser> users = buserService.getUser(user, 1);
				if (users == null || users.size() < 1) {
					result += "@error:" + user + "的 上一级主管 不存在";
				} else {
					result = users.get(0).getLoginid();
				}
				break;
			case "@@AssignToNextLevel_2":
				List<BUser> users2 = buserService.getUser(user, 2);
				if (users2 == null || users2.size() < 1) {
					result += "@error:" + user + "的 上二级主管 不存在";
				} else {
					result = users2.get(0).getLoginid();
				}
				break;
			case "@@AssignToNextLevel_3":
				List<BUser> users3 = buserService.getUser(user, 3);
				if (users3 == null || users3.size() < 1) {
					result += "@error:" + user + "的 上三级主管 不存在\r\n";
				} else {
					result = users3.get(0).getLoginid();
				}
				break;
			default:
				break;
			}
			break;
		default:
			result = flowConfigContentParam.getValue();
			break;
		}
		return result;
	}

	/**
	 * 简单方式配置流程部署
	 */
	private Process deploySimpleFlow(String tableNameEn, String tableName,
			DynamicProcessCustomJsonResolve dynamicProcessCustomJsonResolve) {
		BpmnModel model = new BpmnModel();
		Process process = new Process();

		if (dynamicProcessCustomJsonResolve.isJsonCorrect()) {
			process = dynamicProcessCustomJsonResolve.getProcess();
		}
		process.setId(tableNameEn);
		model.addProcess(process);

		// 自动生成流程图信息
		new BpmnAutoLayout(model).execute();
		// 部署流程
		Deployment deployment = aRepositoryService.createDeployment().addBpmnModel(tableNameEn + ".bpmn20.xml", model)
				.name(tableName).deploy();

		return process;
	}

	/**
	 * 简单方式配置流程 生成默认的flowconfig
	 */
	private List<FlowConfigContent> setSimpleFlowConfig(Process process, List<FormField> formFields,
			List<FlowConfigContentParam> assignParams) {
		List<FlowConfigContent> flowConfigContents = new ArrayList<FlowConfigContent>();

		if (process != null && !process.getFlowElements().isEmpty()) {
			for (FlowElement item : process.getFlowElements()) {
				if (item instanceof UserTask) {
					String processid = item.getId();
					String accessValue = CustomProcessJsonConstants.ASSIGN_ACCESS_READ;
					FlowConfigContent flowConfigContent = new FlowConfigContent();
					List<FlowConfigContentParam> flowConfigContentParams = new ArrayList<FlowConfigContentParam>();

					if (processid.equals(ActivitiDefineConstants.ID_START_USERTASK)) {
						accessValue = CustomProcessJsonConstants.ASSIGN_ACCESS_WRITE;
						flowConfigContentParams = assignParams;
					}

					List<FlowConfigContentFormField> flowConfigContentFormFields = new ArrayList<FlowConfigContentFormField>();
					for (FormField formField : formFields) {
						FlowConfigContentFormField flowConfigContentFormField = new FlowConfigContentFormField();
						flowConfigContentFormField.setCid(formField.getCid());
						flowConfigContentFormField.setField_type(formField.getField_type());
						flowConfigContentFormField.setLabel(formField.getLabel());
						flowConfigContentFormField.setValue(accessValue);
						flowConfigContentFormFields.add(flowConfigContentFormField);
					}

					flowConfigContent.setTaskid(processid);
					flowConfigContent.setParams(flowConfigContentParams);
					flowConfigContent.setFormfields(flowConfigContentFormFields);

					flowConfigContents.add(flowConfigContent);
				}
			}
		}

		return flowConfigContents;
	}
}
