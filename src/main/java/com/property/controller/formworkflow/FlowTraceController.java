package com.property.controller.formworkflow;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.property.service.*;
import com.property.business.*;
import com.property.utils.ActivityUtil;
import com.property.utils.BPMModelHelper;

import org.activiti.engine.runtime.ProcessInstance;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.jobexecutor.*;
import org.activiti.engine.impl.task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程图 跟踪相关接口
 */
@Controller
@RequestMapping(value = "FlowTrace")
public class FlowTraceController {
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
	@Autowired
	private RepositoryService aRepositoryService;
	@Autowired
	private RuntimeService aRuntimeService;
	@Autowired
	private TaskService aTaskService;
	@Autowired
	private IdentityService aIdentityService;

	/**
	 * 查询流程图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFlowImg/{processInstanceDefinitionId}", method = RequestMethod.GET)
	public void getFlowImg(@PathVariable("processInstanceDefinitionId") String processInstanceDefinitionId,
			HttpServletResponse response) throws Exception {
		ProcessInstance processInstance = aRuntimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceDefinitionId).singleResult();
		ProcessDefinition pd = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).latestVersion().singleResult();

		// 通过接口读取
		InputStream resourceAsStream = aRepositoryService.getResourceAsStream(pd.getDeploymentId(),
				pd.getDiagramResourceName());

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 查询流程图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFlowImgByProcessDefineID/{processDefinitionId}", method = RequestMethod.GET)
	public void getFlowImgByProcessDefineID(@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletResponse response) throws Exception {
		ProcessDefinition pd = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).latestVersion().singleResult();

		// 通过接口读取
		InputStream resourceAsStream = aRepositoryService.getResourceAsStream(pd.getDeploymentId(),
				pd.getDiagramResourceName());

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 读取流程定义中的所有活动
	 *
	 * @param executionId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/define/data/{processDefinitionId}")
	@ResponseBody
	public List<Map<String, Object>> readDefineActivitiDatas(
			@PathVariable("processDefinitionId") String processDefinitionId) throws Exception {
		// 获取流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) aRepositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		// 获取流程中所有活动
		List<ActivityImpl> activitiList = ((ProcessDefinitionImpl) processDefinition).getActivities();

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();

		for (ActivityImpl activity : activitiList) {

			ActivityBehavior activityBehavior = activity.getActivityBehavior();

			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, "", false);
			activityInfos.add(activityImageInfo);

			// 处理子流程
			if (activityBehavior instanceof SubProcessActivityBehavior) {
				List<ActivityImpl> innerActivityList = activity.getActivities();
				for (ActivityImpl innerActivity : innerActivityList) {
					activityImageInfo = packageSingleActivitiInfo(innerActivity, "", false);
					activityInfos.add(activityImageInfo);
				}
			}

		}

		return activityInfos;
	}

	/**
	 * 读取跟踪数据
	 *
	 * @param executionId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/trace/data/{executionId}")
	@ResponseBody
	public List<Map<String, Object>> readActivityDatas(@PathVariable("executionId") String executionId)
			throws Exception {
		ExecutionEntity executionEntity = (ExecutionEntity) aRuntimeService.createExecutionQuery()
				.executionId(executionId).singleResult();
		List<String> activeActivityIds = aRuntimeService.getActiveActivityIds(executionId);

		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) aRepositoryService;
		ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl
				.getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) deployedProcessDefinition;
		List<ActivityImpl> activitiList = processDefinition.getActivities();// 获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			ActivityBehavior activityBehavior = activity.getActivityBehavior();

			boolean currentActiviti = false;
			// 当前节点
			String activityId = activity.getId();
			if (activeActivityIds.contains(activityId)) {
				currentActiviti = true;
			}
			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, executionEntity.getId(),
					currentActiviti);
			activityInfos.add(activityImageInfo);

			// 处理子流程
			if (activityBehavior instanceof SubProcessActivityBehavior) {
				List<ActivityImpl> innerActivityList = activity.getActivities();
				for (ActivityImpl innerActivity : innerActivityList) {
					String innerActivityId = innerActivity.getId();
					if (activeActivityIds.contains(innerActivityId)) {
						currentActiviti = true;
					} else {
						currentActiviti = false;
					}
					activityImageInfo = packageSingleActivitiInfo(innerActivity, executionEntity.getId(),
							currentActiviti);
					activityInfos.add(activityImageInfo);
				}
			}

		}

		return activityInfos;
	}

	/**
	 * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 *
	 * @param activity
	 * @param currentActiviti
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, String executionId,
			boolean currentActiviti) throws Exception {
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);

		// 设置图形的XY坐标以及宽度、高度
		setSizeAndPositonInfo(activity, activityInfo);

		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> properties = activity.getProperties();
		vars.put("type", properties.get("type"));
		vars.put("name", properties.get("name"));
		vars.put("id", activity.getId());

		if (properties.containsKey("timerDeclarations")) {
			List<TimerDeclarationImpl> timerDeclarationImplList = (List<TimerDeclarationImpl>) properties
					.get("timerDeclarations");

			TimerDeclarationImpl timerDeclarationImp = timerDeclarationImplList.get(0);
			params.put("duedate", timerDeclarationImp.getDescription().getExpressionText());
			// for(TimerDeclarationImpl e:timerDeclarationImplList){
			// e.getDescription().getExpressionText();
			// }
		}
		if (properties.containsKey("taskDefinition")) {
			TaskDefinition taskDefinition = (TaskDefinition) properties.get("taskDefinition");
			Expression expressionAsignTemp = taskDefinition.getAssigneeExpression();
			if (expressionAsignTemp != null) {
				params.put("assign", expressionAsignTemp.getExpressionText());
			}
			// String assigneeExpressionValue=
			// taskDefinition.getAssigneeExpression().getExpressionText();
		}

		// 当前节点的task
		if (currentActiviti && !executionId.isEmpty()) {
			// setCurrentTaskInfo(executionId, activity.getId(), vars);
		}

		// logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		activityInfo.put("params", params);
		return activityInfo;
	}

	/**
	 * 获取当前节点信息
	 *
	 * @return
	 */
	private void setCurrentTaskInfo(String executionId, String activityId, Map<String, Object> vars) {
		Task currentTask = aTaskService.createTaskQuery().executionId(executionId).taskDefinitionKey(activityId)
				.singleResult();
		// logger.debug("current task for processInstance: {}",
		// ToStringBuilder.reflectionToString(currentTask));

		if (currentTask == null)
			return;

		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			User assigneeUser = aIdentityService.createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName() + "/"
					+ assigneeUser.getId();
			vars.put("当前处理人", userInfo);
			vars.put("创建时间", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(currentTask.getCreateTime()));
		} else {
			vars.put("任务状态", "未签收");
		}

	}

	/**
	 * 设置宽度、高度、坐标属性
	 *
	 * @param activity
	 * @param activityInfo
	 */
	private void setSizeAndPositonInfo(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}
}
