package com.property.controller.formworkflow;

import javax.annotation.Resource;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.property.model.*;
import com.property.service.*;
import com.property.business.*;
import com.property.utils.BPMModelHelper;
import com.property.utils.CommonHelper;

@Controller
@RequestMapping(value = "Form")
public class FormController {
	@Resource
	private IFormDynService formDynService;
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

	// xml新建流程定义，测试方法
	@RequestMapping(value = "/newWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object newWorkFlow(@RequestParam("xmlStr") String xmlStr) {
		activitiService.saveNewDeploye(xmlStr, "test", "test");
		return "{\"result\":\"success\"}";
	}

	/**
	 * 查询表单定义，用于编辑表单时初始化编辑器
	 */
	@RequestMapping(value = "/queryFoemDefine.json", method = RequestMethod.POST)
	@ResponseBody
	public Object queryFoemDefine(@RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);
		return formTableDefine;
	}

	// 新建表单
	@RequestMapping(value = "/addform.json", method = RequestMethod.POST)
	@ResponseBody
	public Object addform(@RequestParam("formDefine") String formDefine, @RequestParam("name") String tableName,
			@RequestParam("dec") String tableDec) {
		FormTableDefine formTableDefine = new FormTableDefine();
		String tableNameEn = CommonHelper.getPinYin(tableName);// 名称转拼音，作为动态创建表时的表名，以及activiti流程的key
		DateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmss");
		Date dateNow = new Date();
		String date = formatDate.format(dateNow); // 用作表名
		formTableDefine.setTableName(tableName);
		formTableDefine.setDescription(tableDec);
		formTableDefine.setTableNameEn(tableNameEn + "_" + date);
		formTableDefine.setFieldsJson(formDefine);
		formTableDefine.setState(0);// 设置为初始状态
		formTableDefine.setCratetime(dateNow);// 设置创建时间
		formTableDefine.setUpdatetime(dateNow);
		formService.insertFormTableDefineById(formTableDefine);
		return "{\"result\":\"success\"}";
	}

	// 编辑表单
	@RequestMapping(value = "/updateform.json", method = RequestMethod.POST)
	@ResponseBody
	public Object updateform(@RequestParam("formDefine") String formDefine, @RequestParam("name") String tableName,
			@RequestParam("dec") String tableDec, @RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		if (formTableDefine == null)
			return "{\"error\":\"审批表单不存在，id为" + id + "\"}";
		//String tableNameEn = CommonHelper.getPinYin(tableName);// 名称转拼音，作为动态创建表时的表名，以及activiti流程的key
		DateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmss");
		Date dateNow = new Date();
		//String date = formatDate.format(dateNow); // 用作表名
		formTableDefine.setTableName(tableName);
		formTableDefine.setDescription(tableDec);
		//formTableDefine.setTableNameEn(tableNameEn + "_" + date);
		formTableDefine.setFieldsJson(formDefine);
		formTableDefine.setUpdatetime(dateNow);
		formService.updateFormTableDefineById(formTableDefine);
		return "{\"result\":\"success\"}";
	}

	// 获取所有表单
	@RequestMapping(value = "/queryformlist.json", method = RequestMethod.POST)
	@ResponseBody
	public Object queryformlist() {
		List<FormTableDefine> formList = formService.getFormList();
		return formList;
	}

	// 根据ID获取表单定义
	@RequestMapping(value = "/getFormById.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getFormById(@RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		return formTableDefine;
	}

	// 更新流程定义
	@RequestMapping(value = "/setWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object setWorkFlow(@RequestParam("activitiJson") String activitiJson,
			@RequestParam("formID") Integer formID) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(formID);
		if (formTableDefine == null)
			return "{\"error\":\"formID不存在\"}";
		Date dateNow = new Date();
		formTableDefine.setActivitiJson(activitiJson);
		formTableDefine.setState(1);// 设置为初始状态
		formTableDefine.setUpdatetime(dateNow);
		formService.updateFormTableDefineById(formTableDefine);
		return "{\"result\":\"success\"}";
	}

	// 根据taskid办理任务
	@RequestMapping(value = "/pushWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object pushWorkFlow(@RequestParam("taskid") String taskid, @RequestParam("remark") String remark,
			@RequestParam("user") String user) {
		remark = remark.isEmpty() ? "同意" : "同意(" + remark + ")";
		activitiService.saveSubmitTask(remark, taskid, user, null);
		return "{\"result\":\"success\"}";
	}

	/**
	 * 根据流程实例ID获取流程实例当前状态
	 * 
	 * @param piID
	 *            流程实例ID
	 */
	@RequestMapping(value = "/getHisByProcessInstaceId.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getHisByProcessInstaceId(@RequestParam("piId") String piId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("commentsList", activitiService.getHisCommentsByPIID(piId));

		resultMap.put("State", activitiHelper.getProcessInstanceStateById(piId));

		return resultMap;
	}

	// 根据业务key查询表单值
	@RequestMapping(value = "/getFormByBusinessKey.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getFormByBusinessKey(@RequestParam("objId") String objId) {
		String tableName = objId.split("\\.")[0];
		String id = objId.split("\\.")[1];
		FormTableDefine formTableDefine = formService.getFormTableDefineByName(tableName.replace("f_", ""));
		Map<String, Object> map = new HashMap<String, Object>();

		String selectSql = "SELECT * FROM `" + tableName + "` where `id`='" + id + "';";
		System.out.println("selectSql = " + selectSql);
		List<Map<String, Object>> resultSelect = formDynService.selectFormValues(selectSql);

		map.put("filedsDefine", formTableDefine);
		map.put("valueList", resultSelect);
		map.put("filedsEn", CommonHelper.getPinYin(formTableDefine.getFieldsJson()));
		return map;
	}

	// 按用户发起的审批任务
	@RequestMapping(value = "/shenpiApplyByUser.json", method = RequestMethod.POST)
	@ResponseBody
	public Object shenpiApplyByUser(@RequestParam("user") String user) {
		return activitiHelper.getHisUserInputTaskByUser(user);
	}

	// 按用户获取需要处理的审批任务
	@RequestMapping(value = "/shenpiListByUser.json", method = RequestMethod.POST)
	@ResponseBody
	public Object shenpiListByUser(@RequestParam("user") String user) {
		List<Task> tasks = activitiService.findTaskListByName(user);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (Task task : tasks) {
			Map<String, Object> mapTask = new HashMap<String, Object>();
			Map<String, Object> variables = activitiService.getVariablesByTaskID(task.getId());
			mapTask.put("taskid", task.getId());
			mapTask.put("tablename", variables.get("tableName"));
			mapTask.put("objId", variables.get("objId"));
			mapTask.put("inputUser", variables.get("inputUser"));
			mapTask.put("TASK_DEF_KEY", task.getTaskDefinitionKey());

			HistoricProcessInstance processInstance = activitiService
					.findHisProcessInstanceByID(task.getProcessInstanceId());
			mapTask.put("StartTime", processInstance.getStartTime());
			mapTask.put("piId", processInstance.getId());
			results.add(mapTask);
		}
		return results;
	}

	// 按用户获取需要处理的审批任务
	@RequestMapping(value = "/listShenpiByUser.json", method = RequestMethod.POST)
	@ResponseBody
	public Object listShenpiByUser(@RequestParam("user") String user) {
		List<HistoricTaskInstance> tasks = activitiService.findHisTaskListByName(user, null);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		List<String> processinstancesID = new ArrayList<String>();// 存放流程实例ID，避免出现重复的流程实例

		for (HistoricTaskInstance task : tasks) {
			if (processinstancesID.contains(task.getProcessInstanceId())
					|| task.getTaskDefinitionKey().equals("userinputtask")) {
				continue;// 当前流程实例已经存在，continue
			}

			processinstancesID.add(task.getProcessInstanceId());
			Map<String, Object> mapTask = new HashMap<String, Object>();

			mapTask.put("taskid", task.getId());
			mapTask.put("StartTime", task.getEndTime());

			HistoricProcessInstance processInstance = activitiService
					.findHisProcessInstanceByID(task.getProcessInstanceId());

			mapTask.put("piId", processInstance.getId());

			List<HistoricVariableInstance> variables = activitiService.getVariablesByPiID(processInstance.getId());
			for (HistoricVariableInstance variableInstance : variables) {
				switch (variableInstance.getVariableName()) {
				case "tableName":
					mapTask.put("tablename", variableInstance.getValue());
					break;
				case "objId":
					mapTask.put("objId", variableInstance.getValue());
					break;
				case "inputUser":
					mapTask.put("inputUser", variableInstance.getValue());
					break;
				default:
					break;
				}
			}
			mapTask.put("state", activitiHelper.getProcessInstanceStateById(task.getProcessInstanceId()));
			results.add(mapTask);

		}
		return results;
	}

	// 重启用审批
	@RequestMapping(value = "/chongqiyong.json", method = RequestMethod.POST)
	@ResponseBody
	public Object chongqiyong(@RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		if (formTableDefine == null)
			return "{\"error\":\"ID不存在\"}";
		String tableNameEn = formTableDefine.getTableNameEn();
		String tableName = formTableDefine.getTableName();
		/* 生成流程 start */
		String activitiJson = formTableDefine.getActivitiJson();
		JSONObject jsonActiviti = new JSONObject(activitiJson);
		String shenpiType = jsonActiviti.getString("type");

		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xmlStr += "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\">";
		xmlStr += "<process id=\"" + tableNameEn + "\" name=\"" + tableName + "\" isExecutable=\"true\">";
		xmlStr += "<startEvent id=\"startevent\" name=\"Start\"></startEvent>";
		xmlStr += "<userTask id=\"userinputtask\" name=\"提交申请\" activiti:assignee=\"${inputUser}\"></userTask>";
		xmlStr += "<sequenceFlow id=\"flow_start\" sourceRef=\"startevent\" targetRef=\"userinputtask\"></sequenceFlow>";
		if (shenpiType.equals("option1")) {
			JSONArray option1 = jsonActiviti.getJSONArray("option1");
			String lastUserTaskID = "";
			for (int i = 0; i < option1.length(); i++) {
				JSONObject assign = option1.getJSONObject(i);
				String assigner = assign.getString("assign");
				String assignText = assign.getString("text");
				String userTaskID = "task_" + UUID.randomUUID().toString();
				if (i == 0) {
					xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText + "审批\" activiti:assignee=\""
							+ assigner + "\"></userTask>";
					xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
							+ "\" sourceRef=\"userinputtask\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
					if (i == (option1.length() - 1)) {
						xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
						xmlStr += "<sequenceFlow id=\"endflow\" sourceRef=\"" + userTaskID
								+ "\" targetRef=\"endevent\"></sequenceFlow>";
					}
					lastUserTaskID = userTaskID;
				} else if (i == (option1.length() - 1)) {
					xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText + "审批\" activiti:assignee=\""
							+ assigner + "\"></userTask>";
					xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
							+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
					xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
					xmlStr += "<sequenceFlow id=\"endflow\" sourceRef=\"" + userTaskID
							+ "\" targetRef=\"endevent\"></sequenceFlow>";
				} else {
					xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText + "审批\" activiti:assignee=\""
							+ assigner + "\"></userTask>";
					xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
							+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
					lastUserTaskID = userTaskID;
				}
			}
		} else if (shenpiType.equals("option2")) {
			JSONObject option2 = jsonActiviti.getJSONObject("option2");
			JSONArray option2Conditions = option2.getJSONArray("conditions");
			String conditionTyepName = "";
			conditionTyepName = option2.getString("conditionTyepName");
			if (!conditionTyepName.isEmpty())
				conditionTyepName = CommonHelper.getPinYin(conditionTyepName);
			String defaultUserTaskID = "task_" + UUID.randomUUID().toString();
			for (int i = 0; i < option2Conditions.length(); i++) {
				JSONObject tempCondition = option2Conditions.getJSONObject(i);
				String valueTemp = tempCondition.getString("value");
				JSONArray assigners = tempCondition.getJSONArray("assigners");
				String lastUserTaskID = "";
				for (int j = 0; j < assigners.length(); j++) {
					JSONObject assign = assigners.getJSONObject(j);
					String assigner = assign.getString("assign");
					String assignText = assign.getString("text");
					String userTaskID = "task_" + UUID.randomUUID().toString();
					if (j == 0) {
						if (i == 0) {
							xmlStr += "<exclusiveGateway id=\"exclusivegateway1\" name=\"Exclusive Gateway\" default=\""
									+ defaultUserTaskID + "\"></exclusiveGateway>";
							xmlStr += "<sequenceFlow id=\"exclusivegateway1_userinputtask\" sourceRef=\"userinputtask\" targetRef=\"exclusivegateway1\"></sequenceFlow>";
							xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
							xmlStr += "<userTask id=\"" + defaultUserTaskID + "\" name=\"" + assignText
									+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
							xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
									+ "\" sourceRef=\"exclusivegateway1\" targetRef=\"" + defaultUserTaskID + "\">";
							xmlStr += "<conditionExpression xsi:type=\"tFormalExpression\"><![CDATA[";
							xmlStr += "${" + conditionTyepName + "=='" + valueTemp + "'}";
							xmlStr += "]]></conditionExpression>";
							xmlStr += "</sequenceFlow>";
							if (j == (assigners.length() - 1)) {
								xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
										+ defaultUserTaskID + "\" targetRef=\"endevent\"></sequenceFlow>";
							}
							lastUserTaskID = defaultUserTaskID;
						} else {
							xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
									+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
							xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
									+ "\" sourceRef=\"exclusivegateway1\" targetRef=\"" + userTaskID + "\">";
							xmlStr += "<conditionExpression xsi:type=\"tFormalExpression\"><![CDATA[";
							xmlStr += "${" + conditionTyepName + "=='" + valueTemp + "'}";
							xmlStr += "]]></conditionExpression>";
							xmlStr += "</sequenceFlow>";
							if (j == (assigners.length() - 1)) {
								xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
										+ userTaskID + "\" targetRef=\"endevent\"></sequenceFlow>";
							}
							lastUserTaskID = userTaskID;
						}
					} else if (j == (assigners.length() - 1)) {
						xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
								+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
								+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
								+ userTaskID + "\" targetRef=\"endevent\"></sequenceFlow>";
					} else {
						xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
								+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
								+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
						lastUserTaskID = userTaskID;
					}
				}
			}
		}
		xmlStr += "</process></definitions>";
		System.out.println("xmlStr = " + xmlStr);
		activitiService.saveNewDeploye(xmlStr, tableName, tableNameEn);
		formTableDefine.setState(2);
		formService.updateFormTableDefineById(formTableDefine);
		/* 生成流程 end */
		return "{\"result\":\"success\"}";
	}

	// 停用审批
	@RequestMapping(value = "/tingyong.json", method = RequestMethod.POST)
	@ResponseBody
	public Object tingyong(@RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		if (formTableDefine == null)
			return "{\"error\":\"ID不存在\"}";
		formTableDefine.setState(3);
		formService.updateFormTableDefineById(formTableDefine);
		/* 生成流程 end */
		return "{\"result\":\"success\"}";
	}

	// 启用审批
	@RequestMapping(value = "/qiyong.json", method = RequestMethod.POST)
	@ResponseBody
	public Object qiyong(@RequestParam("id") Integer id) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
		if (formTableDefine == null)
			return "{\"error\":\"ID不存在\"}";
		String tableName = formTableDefine.getTableName();
		String tableNameEn = formTableDefine.getTableNameEn();
		String fieldsJson = formTableDefine.getFieldsJson();
		/* 生成表 start */
		// 解析报表编辑器的Json创建建表语句
		String createSql = "CREATE TABLE `f_" + tableNameEn + "` (`id` CHAR(36) NOT NULL";
		JSONObject json = new JSONObject(fieldsJson);
		JSONArray results = json.getJSONArray("fields");
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			System.out.println(
					"label = " + result.getString("label") + ", field_type = " + result.getString("field_type"));
			switch (result.getString("field_type")) {
			case "date":
				createSql += ",`" + CommonHelper.getPinYin(result.getString("label")) + "` datetime DEFAULT NULL";
				break;
			case "number":
				createSql += ",`" + CommonHelper.getPinYin(result.getString("label")) + "` double DEFAULT NULL";
				break;
			case "price":
				createSql += ",`" + CommonHelper.getPinYin(result.getString("label")) + "` float DEFAULT NULL";
				break;
			case "paragraph":
				createSql += ",`" + CommonHelper.getPinYin(result.getString("label")) + "` text";
				break;
			default:
				createSql += ",`" + CommonHelper.getPinYin(result.getString("label")) + "` varchar(250) DEFAULT NULL";
				break;
			}
		}
		createSql += ",`inputUser` varchar(250) DEFAULT NULL";
		createSql += ",PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		System.out.println("createSql = " + createSql);
		formDynService.creatNewForm(createSql);
		/* 生成表 end */

		/* 生成流程 start */
		String activitiJson = formTableDefine.getActivitiJson();
		JSONObject jsonActiviti = new JSONObject(activitiJson);
		if (jsonActiviti.has("BPMModelID")) {
			Integer modelid = jsonActiviti.getInt("BPMModelID");
			bpmmodelHelper.deploy(modelid.toString());
		} else {
			String shenpiType = jsonActiviti.getString("type");

			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			xmlStr += "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\">";
			xmlStr += "<process id=\"" + tableNameEn + "\" name=\"" + tableName + "\" isExecutable=\"true\">";
			xmlStr += "<startEvent id=\"startevent\" name=\"Start\"></startEvent>";
			xmlStr += "<userTask id=\"userinputtask\" name=\"提交申请\" activiti:assignee=\"${inputUser}\"></userTask>";
			xmlStr += "<sequenceFlow id=\"flow_start\" sourceRef=\"startevent\" targetRef=\"userinputtask\"></sequenceFlow>";
			if (shenpiType.equals("option1")) {
				JSONArray option1 = jsonActiviti.getJSONArray("option1");
				String lastUserTaskID = "";
				for (int i = 0; i < option1.length(); i++) {
					JSONObject assign = option1.getJSONObject(i);
					String assigner = assign.getString("assign");
					String assignText = assign.getString("text");
					String userTaskID = "task_" + UUID.randomUUID().toString();
					if (i == 0) {
						xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
								+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
								+ "\" sourceRef=\"userinputtask\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
						if (i == (option1.length() - 1)) {
							xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
							xmlStr += "<sequenceFlow id=\"endflow\" sourceRef=\"" + userTaskID
									+ "\" targetRef=\"endevent\"></sequenceFlow>";
						}
						lastUserTaskID = userTaskID;
					} else if (i == (option1.length() - 1)) {
						xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
								+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
								+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
						xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
						xmlStr += "<sequenceFlow id=\"endflow\" sourceRef=\"" + userTaskID
								+ "\" targetRef=\"endevent\"></sequenceFlow>";
					} else {
						xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
								+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
						xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
								+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
						lastUserTaskID = userTaskID;
					}
				}
			} else if (shenpiType.equals("option2")) {
				JSONObject option2 = jsonActiviti.getJSONObject("option2");
				JSONArray option2Conditions = option2.getJSONArray("conditions");
				String conditionTyepName = "";
				conditionTyepName = option2.getString("conditionTyepName");
				if (!conditionTyepName.isEmpty())
					conditionTyepName = CommonHelper.getPinYin(conditionTyepName);
				String defaultflowTaskID = "flow_" + UUID.randomUUID().toString();
				for (int i = 0; i < option2Conditions.length(); i++) {
					JSONObject tempCondition = option2Conditions.getJSONObject(i);
					String valueTemp = tempCondition.getString("value");
					JSONArray assigners = tempCondition.getJSONArray("assigners");
					String lastUserTaskID = "";
					for (int j = 0; j < assigners.length(); j++) {
						JSONObject assign = assigners.getJSONObject(j);
						String assigner = assign.getString("assign");
						String assignText = assign.getString("text");
						String userTaskID = "task_" + UUID.randomUUID().toString();
						if (j == 0) {
							if (i == 0) {
								xmlStr += "<exclusiveGateway id=\"exclusivegateway1\" name=\"Exclusive Gateway\" "
										// +"default=\""+ defaultflowTaskID +
										// "\""
										+ "></exclusiveGateway>";
								xmlStr += "<sequenceFlow id=\"exclusivegateway1_userinputtask\" sourceRef=\"userinputtask\" targetRef=\"exclusivegateway1\"></sequenceFlow>";
								xmlStr += "<endEvent id=\"endevent\" name=\"End\"></endEvent>";
								xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
										+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
								xmlStr += "<sequenceFlow id=\"" + defaultflowTaskID
										+ "\" sourceRef=\"exclusivegateway1\" targetRef=\"" + userTaskID + "\">";
								xmlStr += "<conditionExpression xsi:type=\"tFormalExpression\"><![CDATA[";
								xmlStr += "${" + conditionTyepName + "=='" + valueTemp + "'}";
								xmlStr += "]]></conditionExpression>";
								xmlStr += "</sequenceFlow>";
								if (j == (assigners.length() - 1)) {
									xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
											+ "\" sourceRef=\"" + userTaskID
											+ "\" targetRef=\"endevent\"></sequenceFlow>";
								}
								lastUserTaskID = userTaskID;
							} else {
								xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
										+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
								xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
										+ "\" sourceRef=\"exclusivegateway1\" targetRef=\"" + userTaskID + "\">";
								xmlStr += "<conditionExpression xsi:type=\"tFormalExpression\"><![CDATA[";
								xmlStr += "${" + conditionTyepName + "=='" + valueTemp + "'}";
								xmlStr += "]]></conditionExpression>";
								xmlStr += "</sequenceFlow>";
								if (j == (assigners.length() - 1)) {
									xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString()
											+ "\" sourceRef=\"" + userTaskID
											+ "\" targetRef=\"endevent\"></sequenceFlow>";
								}
								lastUserTaskID = userTaskID;
							}
						} else if (j == (assigners.length() - 1)) {
							xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
									+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
							xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
									+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
							xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
									+ userTaskID + "\" targetRef=\"endevent\"></sequenceFlow>";
						} else {
							xmlStr += "<userTask id=\"" + userTaskID + "\" name=\"" + assignText
									+ "审批\" activiti:assignee=\"" + assigner + "\"></userTask>";
							xmlStr += "<sequenceFlow id=\"flow_" + UUID.randomUUID().toString() + "\" sourceRef=\""
									+ lastUserTaskID + "\" targetRef=\"" + userTaskID + "\"></sequenceFlow>";
							lastUserTaskID = userTaskID;
						}
					}
				}
			}
			xmlStr += "</process></definitions>";
			System.out.println("xmlStr = " + xmlStr);
			activitiService.saveNewDeploye(xmlStr, tableName, tableNameEn);
		}

		formTableDefine.setState(2);
		formService.updateFormTableDefineById(formTableDefine);
		/* 生成流程 end */
		return "{\"result\":\"success\"}";
	}

	// 动态表单纪录查询
	@RequestMapping(value = "/formValuesList.json", method = RequestMethod.POST)
	@ResponseBody
	public Object formValuesList(@RequestParam("id") Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();

		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);
		String tableName = formTableDefine.getTableName();

		String selectSql = "SELECT * FROM `f_" + tableName + "`;";
		System.out.println("insertSql = " + selectSql);
		List<Map<String, Object>> resultSelect = formDynService.selectFormValues(selectSql);

		map.put("filedsDefine", formTableDefine);
		map.put("valueList", resultSelect);
		return map;
	}

	// 动态表单插入数据
	@RequestMapping(value = "/insertForm.json", method = RequestMethod.POST)
	@ResponseBody
	public Object insertForm(@RequestParam("form") String form, @RequestParam("user") String user,
			@RequestParam("id") Integer id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 更新FormTableDefine
		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);
		String tableNameEn = formTableDefine.getTableNameEn();

		String insertSql = "INSERT INTO `f_" + tableNameEn + "` (`id`";
		String insertOptionStr = "";
		String insertValues = "";

		String conditionTyepName = "";
		String activitiJson = formTableDefine.getActivitiJson();
		JSONObject JsonActiviti = new JSONObject(activitiJson);
		Map<String, Object> mapValues = new HashMap<String, Object>();
		if (!JsonActiviti.has("BPMModelID")) {
			String tpyeOption = JsonActiviti.getString("type");

			if (tpyeOption.equals("option2")) {
				conditionTyepName = JsonActiviti.getJSONObject("option2").getString("conditionTyepName");
			}
			if (!conditionTyepName.isEmpty())
				conditionTyepName = CommonHelper.getPinYin(conditionTyepName);

			String resultMessage = "";

			if (tpyeOption.equals("option1")) {
				JSONArray option1 = JsonActiviti.getJSONArray("option1");
				for (int i = 0; i < option1.length(); i++) {
					JSONObject assign = option1.getJSONObject(i);
					String type = assign.getString("type");
					String keyassign = assign.getString("key");
					String value = assign.getString("value");
					if (type.equals("1")) {
						switch (keyassign) {
						case "AssignToNextLevel_1":
							List<BUser> users = buserService.getUser(user, 1);
							if (users == null || users.size() < 1) {
								resultMessage += user + "的 上一级主管 不存在";
							} else {
								mapValues.put("AssignToNextLevel_1", users.get(0).getLoginid());
							}
							break;
						case "AssignToNextLevel_2":
							List<BUser> users2 = buserService.getUser(user, 2);
							if (users2 == null || users2.size() < 1) {
								resultMessage += user + "的 上二级主管 不存在";
							} else {
								mapValues.put("AssignToNextLevel_2", users2.get(0).getLoginid());
							}
							break;
						case "AssignToNextLevel_3":
							List<BUser> users3 = buserService.getUser(user, 3);
							if (users3 == null || users3.size() < 1) {
								resultMessage += user + "的 上三级主管 不存在\r\n";
							} else {
								mapValues.put("AssignToNextLevel_3", users3.get(0).getLoginid());
							}
							break;
						}
					} else if (type.equals("2")) {
						List<BUser> users3 = buserService.getUser(value);
						if (users3 == null || users3.size() < 1) {
							resultMessage += assign.getString("text") + " 不存在\r\n";
						} else {
							mapValues.put(keyassign, users3.get(0).getLoginid());
						}
					}
				}
			} else if (tpyeOption.equals("option2")) {
				JSONObject option2 = JsonActiviti.getJSONObject("option2");
				JSONArray option2Conditions = option2.getJSONArray("conditions");
				for (int i = 0; i < option2Conditions.length(); i++) {
					JSONObject tempCondition = option2Conditions.getJSONObject(i);
					JSONArray assigners = tempCondition.getJSONArray("assigners");
					for (int j = 0; j < assigners.length(); j++) {
						JSONObject assign = assigners.getJSONObject(j);

						String type = assign.getString("type");
						String keyassign = assign.getString("key");
						String value = assign.getString("value");

						if (type.equals("1")) {
							switch (keyassign) {
							case "AssignToNextLevel_1":
								List<BUser> users = buserService.getUser(user, 1);
								if (users == null || users.size() < 1) {
									resultMessage += user + "的 上一级主管 不存在";
								} else {
									mapValues.put("AssignToNextLevel_1", users.get(0).getLoginid());
								}
								break;
							case "AssignToNextLevel_2":
								List<BUser> users2 = buserService.getUser(user, 2);
								if (users2 == null || users2.size() < 1) {
									resultMessage += user + "的 上二级主管 不存在";
								} else {
									mapValues.put("AssignToNextLevel_2", users2.get(0).getLoginid());
								}
								break;
							case "AssignToNextLevel_3":
								List<BUser> users3 = buserService.getUser(user, 3);
								if (users3 == null || users3.size() < 1) {
									resultMessage += user + "的 上三级主管 不存在\r\n";
								} else {
									mapValues.put("AssignToNextLevel_3", users3.get(0).getLoginid());
								}
								break;
							}
						} else if (type.equals("2")) {
							List<BUser> users3 = buserService.getUser(value);
							if (users3 == null || users3.size() < 1) {
								resultMessage += assign.getString("text") + " 不存在\r\n";
							} else {
								mapValues.put(keyassign, users3.get(0).getLoginid());
							}
						}
					}
				}
			}

			if (!resultMessage.isEmpty()) {
				resultMap.put("error", resultMessage);
				return resultMap;
			}
		}
		
		String  temp = "";
		// 解析报表编辑器的Json创建建表语句
		JSONArray results = new JSONArray(form);
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			String namefield = CommonHelper.getPinYin(result.getString("label"));
			insertOptionStr += ",`" + namefield + "`";
			switch (result.getString("field_type")) {
			case "checkboxes":
				JSONArray optionsArry = result.getJSONObject("field_options").getJSONArray("options");
				String checkboxsvalue = "";
				for (int j = 0; j < optionsArry.length(); j++) {
					JSONObject optionItem = optionsArry.getJSONObject(j);
					if (optionItem.getBoolean("checked")) {
						if (j == optionsArry.length() - 1)
							checkboxsvalue += optionItem.getString("label");
						else
							checkboxsvalue += optionItem.getString("label") + ",";
					}
				}
				insertValues += ",'" + checkboxsvalue + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, checkboxsvalue);
				break;
			default:
				insertValues += ",'" + result.getString("value") + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, result.getString("value"));
				if(namefield.equals("xingzhipinggu"))
					temp=result.getString("value");
				break;
			}
		}

		if(tableNameEn.startsWith("gongdanchuli_")){
			mapValues.put("xingzhipinggu", temp);
		}
		
		String idInsert = UUID.randomUUID().toString();
		insertSql += insertOptionStr + ",`inputUser`) VALUES ('" + idInsert + "'" + insertValues + ",'" + user + "');";
		formDynService.creatNewForm(insertSql);
		System.out.println("insertSql = " + insertSql);
		activitiHelper.saveStartProcess(formTableDefine, idInsert, user, mapValues,null);

		return formTableDefine;
	}

	// 被驳回重新提交表单
	@RequestMapping(value = "/reStartWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object reStartWorkFlow(@RequestParam("form") String form, @RequestParam("user") String user,
			@RequestParam("objId") String objId, @RequestParam("taskid") String taskid,
			@RequestParam("formid") Integer id) {
		// 更新FormTableDefine
		FormTableDefine formTableDefine = this.formService.getFormTableDefineById(id);
		String tableNameEn = objId.split("\\.")[0];
		String instanceid = objId.split("\\.")[1];

		String udateSql = "UPDATE `" + tableNameEn + "` SET `id`='" + instanceid + "'";

		String conditionTyepName = "";
		String activitiJson = formTableDefine.getActivitiJson();
		JSONObject JsonActiviti = new JSONObject(activitiJson);
		String tpyeOption = JsonActiviti.getString("type");
		Map<String, Object> mapValues = new HashMap<String, Object>();
		if (tpyeOption.equals("option2")) {
			conditionTyepName = JsonActiviti.getJSONObject("option2").getString("conditionTyepName");
		}
		if (!conditionTyepName.isEmpty())
			conditionTyepName = CommonHelper.getPinYin(conditionTyepName);

		// 解析报表编辑器的Json创建建表语句
		JSONArray results = new JSONArray(form);
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			String namefield = CommonHelper.getPinYin(result.getString("label"));
			udateSql += ",`" + namefield + "`='";
			switch (result.getString("field_type")) {
			case "checkboxes":
				JSONArray optionsArry = result.getJSONObject("field_options").getJSONArray("options");
				String checkboxsvalue = "";
				for (int j = 0; j < optionsArry.length(); j++) {
					JSONObject optionItem = optionsArry.getJSONObject(j);
					if (optionItem.getBoolean("checked")) {
						checkboxsvalue += optionItem.getString("label") + ",";
					}
				}
				udateSql += checkboxsvalue + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, checkboxsvalue);
				break;
			case "number":
				udateSql += result.getBigDecimal("value") + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, result.getBigDecimal("value"));
				break;
			case "price":
				udateSql += result.getBigDecimal("value") + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, result.getBigDecimal("value"));
				break;
			/*
			 * case "date": BigDecimal timesharp=result.getBigDecimal("value");
			 * SimpleDateFormat sdf = new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String datevalue =
			 * sdf.format(new Date(timesharp.longValue())); udateSql +=
			 * datevalue + "'"; if ((!conditionTyepName.isEmpty()) &&
			 * conditionTyepName.equals(namefield))
			 * mapValues.put(conditionTyepName, timesharp); break;
			 */
			default:
				udateSql += result.getString("value") + "'";
				if ((!conditionTyepName.isEmpty()) && conditionTyepName.equals(namefield))
					mapValues.put(conditionTyepName, result.getString("value"));
				break;
			}
		}

		udateSql += " WHERE `id`='" + instanceid + "'";
		System.out.println("udateSql = " + udateSql);
		formDynService.creatNewForm(udateSql);
		activitiService.saveSubmitTask("重新提交", taskid, user, mapValues);
		return formTableDefine;
	}

	// 退回到上一任务
	@RequestMapping(value = "/rollBackWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object rollBackWorkFlow(@RequestParam("taskid") String taskid, @RequestParam("user") String user,
			@RequestParam("remark") String remark) {
		remark = remark.isEmpty() ? "驳回到上一步" : "驳回到上一步(" + remark + ")";
		return activitiService.rollBackToUserTask(taskid, "", user, remark);
	}

	// 退回到申请者
	@RequestMapping(value = "/rollBackToAssignWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object rollBackToAssignWorkFlow(@RequestParam("taskid") String taskid, @RequestParam("user") String user,
			@RequestParam("remark") String remark) {
		remark = remark.isEmpty() ? "驳回到发起者" : "驳回到发起者(" + remark + ")";
		return activitiService.rollBackToUserTask(taskid, "userinputtask", user, remark);
	}

	// 撤销申请，直接跳转至结束任务
	@RequestMapping(value = "/cancelWorkFlow.json", method = RequestMethod.POST)
	@ResponseBody
	public Object cancelWorkFlow(@RequestParam("taskid") String taskid, @RequestParam("user") String user) {
		return activitiService.rollBackToUserTask(taskid, "endevent", user, "撤销申请");
	}
}
