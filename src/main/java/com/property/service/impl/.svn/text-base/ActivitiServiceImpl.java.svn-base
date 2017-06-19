package com.property.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.property.data.APIMessage;
import com.property.data.FormField;
import com.property.service.IActivitiService;

@Service("activitiService")
public class ActivitiServiceImpl implements IActivitiService {
	@Autowired
	private RuntimeService aRuntimeService;
	@Autowired
	private TaskService aTaskService;
	@Autowired
	private RepositoryService aRepositoryService;
	@Autowired
	private HistoryService aHistoryService;

	/**
	 * 审批流程 同意
	 * 
	 * @param message:审批时的批注
	 * @param taskId：当前的任务
	 * @param id：执行任务的用户
	 */
	@Override
	public void saveSubmitTask(String message, String taskId, String user, Map<String, Object> map) {
		Task task = aTaskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();
		String processInstanceId = task.getProcessInstanceId();// 流程实例ID

		Authentication.setAuthenticatedUserId(user);
		aTaskService.addComment(taskId, processInstanceId, message);// 完成任务前添加批注

		aTaskService.complete(taskId, map);// 完成指定task，进入下一流程任务

	}

	/**
	 * 审批流程 同意
	 * 
	 * @param message:审批时的批注
	 * @param taskId：当前的任务
	 * @param id：执行任务的用户
	 * @param formValuesList:表单提交的值
	 */
	@Override
	public void saveSubmitTask(String message, String taskId, String user, Map<String, Object> map,
			List<FormField> formValuesList) {
		Task task = aTaskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();
		String processInstanceId = task.getProcessInstanceId();// 流程实例ID

		if (map == null)
			map = new HashMap<String, Object>();

		map.putAll(setGateWayVariables(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), formValuesList));

		Authentication.setAuthenticatedUserId(user);
		aTaskService.addComment(taskId, processInstanceId, message);// 完成任务前添加批注

		aTaskService.complete(taskId, map);// 完成指定task，进入下一流程任务

	}

	/**
	 * 获取流程实例当前状态，结束或等待某人审批
	 * 
	 * @return APIMessage.status==0 结束
	 * @return APIMessage.status==1 等待审批;content 为 List<string> 审批人
	 */
	@Override
	public APIMessage getProcessInstanceStateById(String processInstanceId) {
		APIMessage result = new APIMessage();
		// 获取流程实例
		ProcessInstance processInstance = aRuntimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			result.setStatus(0);
			result.setMessage("流程已经结束");
			return result;
		}
		// 获取当前的task列表
		List<Task> taskCur = aTaskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
		if (taskCur == null || taskCur.isEmpty()) {
			result.setStatus(-1);
			result.setMessage("Task为空");
			return result;
		}
		List<String> assignerList = new ArrayList<String>();
		taskCur.forEach(task -> assignerList.add(task.getAssignee()));
		result.setStatus(1);
		result.setMessage("等待审批");
		result.setContent(assignerList);
		return result;
	}

	/**
	 * 获取指定用户、特定阶段、已完成 的审批流程任务
	 * 
	 * @param user:用户
	 * @param taskKey:
	 *            task id (userinputtask:本系统默认的提交申请 task)
	 */
	@Override
	public List<HistoricTaskInstance> findHisTaskListByName(String name, String taskKey) {
		List<HistoricTaskInstance> list = new ArrayList<HistoricTaskInstance>();
		if (taskKey == null || taskKey.isEmpty())
			list = aHistoryService.createHistoricTaskInstanceQuery().taskAssignee(name).finished()
					.orderByHistoricTaskInstanceEndTime().desc().list();
		else
			list = aHistoryService.createHistoricTaskInstanceQuery().taskAssignee(name)// 指定用户
					.taskDefinitionKey(taskKey).finished().orderByHistoricTaskInstanceStartTime().asc().list();
		return list;
	}

	/**
	 * 根据taskid获取流程变量
	 */
	@Override
	public Map<String, Object> getVariablesByTaskID(String id) {
		return aTaskService.getVariables(id);
	}

	/**
	 * 根据流程实例ID获取所有历史流程变量
	 * 
	 * @param id:流程实例ID
	 */
	@Override
	public List<HistoricVariableInstance> getVariablesByPiID(String id) {
		return aHistoryService.createHistoricVariableInstanceQuery().processInstanceId(id).list();
	}

	/**
	 * 根据流Task ID获取所有历史流程变量
	 */
	@Override
	public List<HistoricVariableInstance> getHisVariablesByTaskID(String id) {
		return aHistoryService.createHistoricVariableInstanceQuery().taskId(id).list();
	}

	/**
	 * 根据id获取流程实例子
	 */
	@Override
	public ProcessInstance findProcessInstanceByID(String id) {
		return aRuntimeService.createProcessInstanceQuery().processDefinitionId(id).singleResult();
	}

	/**
	 * 根据id获取历史流程实例子
	 */
	@Override
	public HistoricProcessInstance findHisProcessInstanceByID(String id) {
		return aHistoryService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
	}

	/**
	 * 使用xml字符串部署流程
	 * 
	 * @param XmlStr：xml字符串
	 * @param Name：流程Name,可以为中文
	 * @param nameEn：流程key
	 */
	@Override
	public void saveNewDeploye(String XmlStr, String Name, String nameEn) {
		try {
			aRepositoryService.createDeployment().name(Name).addString(nameEn + ".bpmn20.xml", XmlStr).deploy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>
	 */
	@Override
	public List<Task> findTaskListByName(String name) {
		List<Task> list = aTaskService.createTaskQuery()//
				.taskAssignee(name)// 指定个人任务查询
				.orderByTaskCreateTime().asc()//
				.list();
		return list;
	}

	/**
	 * 启动流程实例，让启动的流程实例关联业务,并完成用户提交任务,判断下一个任务审批人，如果为提交者本人默认通过
	 * 
	 * @param key：表单流程key
	 * @param objId：businesskey，表名＋主键id
	 * @param user：提交表单的用户
	 * @param mapValues：流程变量
	 * @param formValuesList:表单提交的值
	 */
	@Override
	public void startProcessAndCommitInput(String key, String objId, String user, Map<String, Object> mapValues,
			List<FormField> formValuesList) {
		// 流程变量集合
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("objId", objId);
		variables.putAll(mapValues);
		// 使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
		aRuntimeService.startProcessInstanceByKey(key, objId, variables);
		// 获得userinputtask（提交申请）任务
		Task task = aTaskService.createTaskQuery().processInstanceBusinessKey(objId).singleResult();
		variables.put("inputUser", user);
		variables.putAll(
				setGateWayVariables(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), formValuesList));

		aTaskService.setAssignee(task.getId(), user);// 将任务assign给提交人

		Authentication.setAuthenticatedUserId(user);
		aTaskService.addComment(task.getId(), task.getProcessInstanceId(), "提交申请");
		aTaskService.complete(task.getId(), variables);//
		// 启动流程时随即完成提交task

		task = aTaskService.createTaskQuery().processInstanceBusinessKey(objId).singleResult();
		if (task.getAssignee().equals(user)) {
			aTaskService.complete(task.getId(), variables);// 判断下一个审批人是不是自己，是默认提交
		}

	}

	/**
	 * 启动流程实例，让启动的流程实例关联业务,并完成用户提交任务,判断下一个任务审批人，如果为提交者本人默认通过
	 * 
	 * @param key：表单流程key
	 * @param objId：businesskey，表名＋主键id
	 * @param user：提交表单的用户
	 * @param mapValues：流程变量
	 */
	@Override
	public void startProcessAndCommitInput(String key, String objId, String user, Map<String, Object> mapValues) {
		// 流程变量集合
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("objId", objId);
		variables.putAll(mapValues);
		// 使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
		aRuntimeService.startProcessInstanceByKey(key, objId, variables);
		// 获得userinputtask（提交申请）任务
		Task task = aTaskService.createTaskQuery().processInstanceBusinessKey(objId).singleResult();
		variables.put("inputUser", user);

		aTaskService.setAssignee(task.getId(), user);// 将任务assign给提交人

		Authentication.setAuthenticatedUserId(user);
		aTaskService.addComment(task.getId(), task.getProcessInstanceId(), "提交申请");
		aTaskService.complete(task.getId(), variables);//
		// 启动流程时随即完成提交task

		task = aTaskService.createTaskQuery().processInstanceBusinessKey(objId).singleResult();
		if (task.getAssignee().equals(user)) {
			aTaskService.complete(task.getId(), variables);// 判断下一个审批人是不是自己，是默认提交
		}

	}

	/**
	 * 通过流程实例ID获取批注信息
	 */
	@Override
	public Object getHisCommentsByPIID(String processID) {
		return aTaskService.getProcessInstanceComments(processID);
	}

	/**
	 * 返回任意流程节点，模拟驳回和撤销 *
	 * 
	 * @param destTaskkey为xml中task的id，驳回到发起者时应为userinputtask,destTaskKey为空时默认回退到上一层级
	 */
	@Override
	public APIMessage rollBackToUserTask(String taskId, String destTaskkey, String user, String remark) {
		APIMessage result = new APIMessage();
		try {
			Map<String, Object> variables;
			// 取得当前任务.当前任务节点
			HistoricTaskInstance currTask = aHistoryService.createHistoricTaskInstanceQuery().taskId(taskId)
					.singleResult();
			// 取得流程实例，流程实例
			ProcessInstance instance = aRuntimeService.createProcessInstanceQuery()
					.processInstanceId(currTask.getProcessInstanceId()).singleResult();
			if (instance == null) {
				result.setStatus(0);
				result.setMessage("流程已经结束");
				return result;
			}
			variables = instance.getProcessVariables();
			// 取得流程定义
			ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) aRepositoryService)
					.getDeployedProcessDefinition(currTask.getProcessDefinitionId());
			if (definition == null) {
				result.setStatus(0);
				result.setMessage("流程定义未找到");
				return result;
			}

			// 取得当前活动节点
			ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
					.findActivity(currTask.getTaskDefinitionKey());
			// destTaskkey为空时 默认 退回到上一步
			if (destTaskkey.isEmpty()) {
				List<PvmTransition> oriLastTransitionList = currActivity.getIncomingTransitions();
				APIMessage resultdestTaskkey = this.getTaskKey(oriLastTransitionList);
				if (resultdestTaskkey.getStatus() == 1)
					destTaskkey = (String) resultdestTaskkey.getContent();
			}
			if (destTaskkey.isEmpty()) {
				result.setStatus(0);
				result.setMessage("目标活动key为空");
				return result;
			}

			// 清除当前活动的出口,并保存原出口用于复原
			List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
			oriPvmTransitionList.addAll(currActivity.getOutgoingTransitions());
			currActivity.getOutgoingTransitions().clear();

			// 根据destTaskkey获取目标活动，并建立新出口
			ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(destTaskkey);
			// 获取目标活动的所有入口用于复原（为一个活动设置出口的同时，目标活动也会加上新的入口，因而复原时需要复原当前活动的出口以及目标活动的入口）
			List<PvmTransition> oriNextActivityPvmTransitionList = new ArrayList<PvmTransition>();
			oriNextActivityPvmTransitionList.addAll(nextActivityImpl.getIncomingTransitions());
			// 建立新的出口
			TransitionImpl newTransition = currActivity.createOutgoingTransition();
			newTransition.setDestination(nextActivityImpl);

			// 完成任务，增加批注以便追踪历史
			Authentication.setAuthenticatedUserId(user);
			aTaskService.addComment(taskId, instance.getId(), remark);
			aTaskService.complete(taskId, variables);
			// 删除任务记录，删除后对应的批注也会被删除
			// historyService.deleteHistoricTaskInstance(task.getId());

			// 恢复当前活动出口
			currActivity.getOutgoingTransitions().clear();
			currActivity.getOutgoingTransitions().addAll(oriPvmTransitionList);
			// 恢复目标活动入口
			nextActivityImpl.getIncomingTransitions().clear();
			nextActivityImpl.getIncomingTransitions().addAll(oriNextActivityPvmTransitionList);

			List<Task> currentTask = aTaskService.createTaskQuery().processInstanceId(instance.getId()).list();
			for(Task item : currentTask){
				if(item.getTaskDefinitionKey().equals("userinputtask")){
					aTaskService.setAssignee(item.getId(), aTaskService.getVariable(item.getId(), "inputUser").toString());
				}
			}
			
			result.setStatus(1);
			return result;
		} catch (Exception e) {
			result.setStatus(-1);
			result.setMessage("系统错误");
			return result;
		}
	}

	/**
	 * 转办流程
	 * 
	 * @param taskId
	 *            当前任务节点ID
	 * @param userCode
	 *            被转办人Code
	 */
	public void transferAssignee(String taskId, String user) {

		Task task = aTaskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();

		Authentication.setAuthenticatedUserId(task.getAssignee());
		aTaskService.addComment(taskId, task.getProcessInstanceId(), "改签-->" + user + "审批");// 完成任务前添加批注

		aTaskService.setAssignee(taskId, user);
	}

	/**
	 * 获取上一步活动usertask key
	 *
	 */
	private APIMessage getTaskKey(List<PvmTransition> oriLastTransitionList) {
		APIMessage result = new APIMessage();
		if (oriLastTransitionList == null || oriLastTransitionList.isEmpty()) {
			result.setStatus(0);
			result.setMessage("上一步活动为空");
			return result;
		}
		if (oriLastTransitionList.size() > 1) {
			result.setStatus(0);
			result.setMessage("发现多个上一步活动");
			return result;
		}
		TransitionImpl oriLastTransitionImpl = (TransitionImpl) oriLastTransitionList.get(0);
		ActivityImpl activityImpl = oriLastTransitionImpl.getSource();
		if (activityImpl.getProperties().get("type").toString().equals("exclusiveGateway")) {
			result = this.getTaskKey(oriLastTransitionList.get(0).getSource().getIncomingTransitions());
		} else {
			result.setStatus(1);
			result.setContent(oriLastTransitionList.get(0).getSource().getId());
		}
		return result;
	}

	/**
	 * 根据流程定义，获取所有exclusiveGateway，获取变量插入
	 */
	private Map<String, Object> setGateWayVariables(String processDefinitionId, String currTaskKey,
			List<FormField> formValuesList) {
		Map<String, Object> variables = new HashMap<String, Object>();
		List<String> conditionTextList = new ArrayList<String>();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) aRepositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		// 取得当前活动节点
		List<ActivityImpl> activitiList = ((ProcessDefinitionImpl) processDefinition).getActivities();
		for (ActivityImpl activity : activitiList) {
			Map<String, Object> properties = activity.getProperties();
			String type = properties.get("type").toString();
			if (type.equals("exclusiveGateway")) {
				List<PvmTransition> transitionList = activity.getOutgoingTransitions();
				for (PvmTransition pvmTransition : transitionList) {
					Object conditionTextObj = pvmTransition.getProperty("conditionText");
					if (conditionTextObj != null) {
						String conditionText = conditionTextObj.toString();
						conditionText = conditionText.split("==")[0].replace("${", "").trim();
						if (!conditionTextList.contains(conditionText))
							conditionTextList.add(conditionText);
					}
				}
			}
		}

		for (String conditionText : conditionTextList) {
			for (FormField formField : formValuesList) {
				if (conditionText.equals(formField.getLabel().trim()))
					variables.put(conditionText, formField.getValue());
			}
		}

		return variables;

	}
}
