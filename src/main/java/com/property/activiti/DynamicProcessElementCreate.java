package com.property.activiti;

import java.util.UUID;

import org.activiti.bpmn.model.*;

import com.property.constants.ActivitiDefineConstants;

/**
 * activiti api 动态创建流程节点
 * 
 */
public class DynamicProcessElementCreate {

	/**
	 * 创建互斥网关
	 */
	public ExclusiveGateway createExclusiveGateway() {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId("gate-" + UUID.randomUUID().toString());
		return exclusiveGateway;
	}

	/**
	 * 创建 usertask
	 * 
	 * @param name
	 *            任务名称
	 * @param assignee
	 *            任务签收人
	 */
	public UserTask createUserTask(String name, String assignee) {
		return createUserTask("task-" + UUID.randomUUID().toString(), name, assignee);
	}

	/**
	 * 创建 usertask
	 * 
	 * @param id
	 *            任务id
	 * @param name
	 *            任务名称
	 * @param assignee
	 *            任务签收人
	 */
	public UserTask createUserTask(String id, String name, String assignee) {
		UserTask userTask = new UserTask();
		userTask.setName(name);
		userTask.setId(id);
		if (!assignee.isEmpty())
			userTask.setAssignee(assignee);
		return userTask;
	}

	/**
	 * 创建连线
	 * 
	 * @param from
	 *            连线入口
	 * @param to
	 *            连线出口
	 */
	public SequenceFlow createSequenceFlow(String from, String to) {
		return createSequenceFlow(from, to, "");
	}

	/**
	 * 创建连线
	 * 
	 * @param from
	 *            连线入口
	 * @param to
	 *            连线出口
	 * @param skipExpression
	 *            流条件
	 */
	public SequenceFlow createSequenceFlow(String from, String to, String conditionExpression) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		if (!conditionExpression.isEmpty())
			flow.setConditionExpression(conditionExpression);

		return flow;
	}

	/**
	 * 创建开始任务
	 */
	public StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(ActivitiDefineConstants.ID_START_EVENT);
		return startEvent;
	}

	/**
	 * 创建结束任务
	 */
	public EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(ActivitiDefineConstants.ID_END_EVENT);
		return endEvent;
	}
}
