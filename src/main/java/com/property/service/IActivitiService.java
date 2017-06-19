package com.property.service;

import java.util.List;
import java.util.Map;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import com.property.data.APIMessage;
import com.property.data.FormField;

public interface IActivitiService {
	/**
	 * 使用xml字符串部署流程
	 * 
	 * @param XmlStr：xml字符串
	 * @param Name：流程Name,可以为中文
	 * @param nameEn：流程key
	 */
	public void saveNewDeploye(String XmlStr, String Name, String nameEn);

	/**
	 * 获取流程实例当前状态，结束或等待某人审批
	 * 
	 * @return APIMessage.status==0 结束
	 * @return APIMessage.status==1 等待审批;content 为 List<string> 审批人
	 */
	public APIMessage getProcessInstanceStateById(String processInstanceId);

	/**
	 * 获取指定用户、特定阶段的审批流程
	 * 
	 * @param user:用户
	 * @param taskKey:
	 *            task id (userinputtask:本系统默认的提交申请 task)
	 */
	public List<HistoricTaskInstance> findHisTaskListByName(String name, String taskKey);

	/**
	 * 根据taskid获取流程变量
	 */
	public Map<String, Object> getVariablesByTaskID(String id);

	/**
	 * 根据流程实例ID获取所有历史流程变量
	 * 
	 * @param id:流程实例ID
	 */
	public List<HistoricVariableInstance> getVariablesByPiID(String id);

	/**
	 * 根据流Task ID获取所有历史流程变量
	 */
	public List<HistoricVariableInstance> getHisVariablesByTaskID(String id);

	/**
	 * 根据id获取流程实例子
	 */
	public ProcessInstance findProcessInstanceByID(String id);

	/**
	 * 根据id获取历史流程实例子
	 */
	public HistoricProcessInstance findHisProcessInstanceByID(String id);

	/**
	 * 使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>
	 */
	public List<Task> findTaskListByName(String name);

	/**
	 * 通过流程实例ID获取批注信息
	 */
	public Object getHisCommentsByPIID(String processID);

	/**
	 * 启动流程实例，让启动的流程实例关联业务,并完成用户提交任务,判断下一个任务审批人，如果为提交者本人默认通过
	 * 
	 * @param key：表单流程key
	 * @param objId：businesskey，表名＋主键id
	 * @param user：提交表单的用户
	 * @param mapValues：流程变量
	 * @param formValuesList:表单提交的值
	 */
	public void startProcessAndCommitInput(String key, String objId, String user, Map<String, Object> mapValues, List<FormField> formValuesList);
	
	/**
	 * 启动流程实例，让启动的流程实例关联业务,并完成用户提交任务,判断下一个任务审批人，如果为提交者本人默认通过
	 * 
	 * @param key：表单流程key
	 * @param objId：businesskey，表名＋主键id
	 * @param user：提交表单的用户
	 * @param mapValues：流程变量
	 */
	public void startProcessAndCommitInput(String key, String objId, String user, Map<String, Object> mapValues);

	/**
	 * 审批流程 同意
	 * 
	 * @param message:审批时的批注
	 * @param taskId：当前的任务
	 * @param id：执行任务的用户
	 * @param formValuesList:表单提交的值
	 */
	public void saveSubmitTask(String message, String taskId, String user, Map<String, Object> map, List<FormField> formValuesList);
	
	/**
	 * 审批流程 同意
	 * 
	 * @param message:审批时的批注
	 * @param taskId：当前的任务
	 * @param id：执行任务的用户
	 */
	public void saveSubmitTask(String message, String taskId, String user, Map<String, Object> map);

	/**
	 * 返回任意流程节点，模拟驳回和撤销 *
	 * 
	 * @param destTaskkey为xml中task的id，驳回到发起者时应为userinputtask,destTaskKey为空时默认回退到上一层级
	 */
	public APIMessage rollBackToUserTask(String taskId, String destTaskkey, String user, String remark);
	
    /** 
     * 转办流程 
     *  
     * @param taskId 
     *            当前任务节点ID 
     * @param userCode 
     *            被转办人Code 
     */  
    public void transferAssignee(String taskId, String userCode);
}
