package com.property.activiti;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.property.business.AssignSelectHelper;
import com.property.constants.ActivitiDefineConstants;
import com.property.constants.CustomProcessJsonConstants;
import com.property.data.CustomAssign;
import com.property.data.CustomConditionAssign;
import com.property.data.FlowConfigContentParam;

/**
 * (简单配置方式)动态生成processdefine
 */
public class DynamicProcessCustomJsonResolve {
	private AssignSelectHelper assignSelectHelper;
	private DynamicProcessElementCreate dynamicProcessElementCreate;
	/**
	 * 审批类型 option1:无分支，直线审批 option2:条件判断分支
	 */
	private String type;
	// 分支条件字段
	private String conditionTyepName;
	private List<CustomAssign> options1;
	private List<CustomConditionAssign> options2;
	private boolean isJsonCorrect;

	public DynamicProcessCustomJsonResolve(JSONObject jsonObject) {
		this.dynamicProcessElementCreate = new DynamicProcessElementCreate();
		this.assignSelectHelper = new AssignSelectHelper();

		if (jsonObject.containsKey(CustomProcessJsonConstants.ASSIGN_TYPE))
			this.type = jsonObject.getString("type");
		else
			this.type = "";

		switch (this.type) {
		case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION1:
			Object options1assigners = jsonObject.get(CustomProcessJsonConstants.ASSIGN_TYPE_OPTION1);
			options1 = JSON.parseArray(options1assigners + "", CustomAssign.class);
			isJsonCorrect = true;
			break;
		case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION2:
			JSONObject options2assigners = jsonObject.getJSONObject(CustomProcessJsonConstants.ASSIGN_TYPE_OPTION2);
			if (options2assigners.containsKey(CustomProcessJsonConstants.ASSIGN_OPTION2_CONDITIONTYPENAME)) {
				this.conditionTyepName = options2assigners
						.getString(CustomProcessJsonConstants.ASSIGN_OPTION2_CONDITIONTYPENAME);
			}
			if (options2assigners.containsKey(CustomProcessJsonConstants.ASSIGN_OPTION2_CONDITIONTYPENAME)) {
				this.options2 = JSON.parseArray(
						options2assigners.getJSONArray(CustomProcessJsonConstants.ASSIGN_OPTION2_CONDITIONS) + "",
						CustomConditionAssign.class);
			}
			isJsonCorrect = true;
			break;
		default:
			this.type = "";
			this.conditionTyepName = "";
			this.options1 = new ArrayList<CustomAssign>();
			this.options2 = new ArrayList<CustomConditionAssign>();
			isJsonCorrect = false;
			break;
		}
	}

	public boolean isJsonCorrect() {
		return isJsonCorrect;
	}

	public void setJsonCorrect(boolean isJsonCorrect) {
		this.isJsonCorrect = isJsonCorrect;
	}

	/**
	 * 动态生成process
	 */
	public Process getProcess() {
		Process process = new Process();
		if (!this.type.isEmpty() && isJsonCorrect) {
			switch (this.type) {
			case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION1:
				process = setProcessOption1(process);
				break;
			case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION2:
				process = setProcessOption2(process);
				break;
			default:
				break;
			}
		}
		return process;
	}

	public List<FlowConfigContentParam> getAssignParams() {
		List<FlowConfigContentParam> flowConfigContentParams = new ArrayList<FlowConfigContentParam>();
		if (!this.type.isEmpty() && isJsonCorrect) {
			switch (this.type) {
			case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION1:	
				for(CustomAssign item : options1){
					if(item.getType().equals(CustomProcessJsonConstants.CUSTOM_ASSIGN_TYPE_NEXTLEVEL)){
						FlowConfigContentParam flowConfigContentParam = new FlowConfigContentParam();
						flowConfigContentParam.setType(CustomProcessJsonConstants.FLOW_CONFIG_PARAMS_TYPE_ASSIGN_NEXTLEVEL);
						flowConfigContentParam.setValue("@@"+item.getValue());
						flowConfigContentParam.setExpression(item.getAssign());
						flowConfigContentParams.add(flowConfigContentParam);
					}
				}
				break;
			case CustomProcessJsonConstants.ASSIGN_TYPE_OPTION2:
				for(CustomConditionAssign item : options2){
					for(CustomAssign assign : item.getAssigners()){
						if(assign.getType().equals(CustomProcessJsonConstants.CUSTOM_ASSIGN_TYPE_NEXTLEVEL)){
							FlowConfigContentParam flowConfigContentParam = new FlowConfigContentParam();
							flowConfigContentParam.setType(CustomProcessJsonConstants.FLOW_CONFIG_PARAMS_TYPE_ASSIGN_NEXTLEVEL);
							flowConfigContentParam.setValue("@@"+assign.getValue());
							flowConfigContentParam.setExpression(assign.getAssign());
							flowConfigContentParams.add(flowConfigContentParam);
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return flowConfigContentParams;
	}

	/**
	 * 无分支审批方式
	 */
	private Process setProcessOption1(Process process) {
		List<String> elementIdList = new ArrayList<String>();

		process.addFlowElement(this.dynamicProcessElementCreate.createStartEvent());
		elementIdList.add(ActivitiDefineConstants.ID_START_EVENT);

		process.addFlowElement(
				this.dynamicProcessElementCreate.createUserTask(ActivitiDefineConstants.ID_START_USERTASK, "提交", ""));
		elementIdList.add(ActivitiDefineConstants.ID_START_USERTASK);

		process.addFlowElement(this.dynamicProcessElementCreate.createEndEvent());

		process = this.setProcess(process, options1, elementIdList, "");

		return process;
	}

	/**
	 * 分支审批方式
	 */
	private Process setProcessOption2(Process process) {
		if (!conditionTyepName.isEmpty()) {
			process.addFlowElement(this.dynamicProcessElementCreate.createStartEvent());
			process.addFlowElement(this.dynamicProcessElementCreate
					.createUserTask(ActivitiDefineConstants.ID_START_USERTASK, "提交", ""));
			process.addFlowElement(this.dynamicProcessElementCreate.createEndEvent());

			ExclusiveGateway exclusiveGateway = this.dynamicProcessElementCreate.createExclusiveGateway();
			process.addFlowElement(exclusiveGateway);

			process.addFlowElement(this.dynamicProcessElementCreate.createSequenceFlow(
					ActivitiDefineConstants.ID_START_EVENT, ActivitiDefineConstants.ID_START_USERTASK));
			process.addFlowElement(this.dynamicProcessElementCreate
					.createSequenceFlow(ActivitiDefineConstants.ID_START_USERTASK, exclusiveGateway.getId()));

			for (int i = 0; i < this.options2.size(); i++) {
				String contionValue = this.options2.get(i).getValue();
				List<CustomAssign> assigners = this.options2.get(i).getAssigners();
				List<String> elementIdList = new ArrayList<String>();
				elementIdList.add(exclusiveGateway.getId());
				process = this.setProcess(process, assigners, elementIdList,
						"${" + this.conditionTyepName + "==\"" + contionValue + "\"}");
			}
		}

		return process;
	}

	private Process setProcess(Process process, List<CustomAssign> assigners, List<String> elementIdList,
			String skipExpression) {
		for (CustomAssign item : assigners) {
			UserTask itemtask = this.dynamicProcessElementCreate.createUserTask(item.getText() + " 审批",
					assignSelectHelper.getAssigner(item));
			process.addFlowElement(itemtask);
			elementIdList.add(itemtask.getId());
		}

		elementIdList.add(ActivitiDefineConstants.ID_END_EVENT);

		for (int j = 0; j < elementIdList.size() - 1; j++) {
			String skipExpressionValue = "";
			if (j == 0) {
				skipExpressionValue = skipExpression;
			}
			process.addFlowElement(this.dynamicProcessElementCreate.createSequenceFlow(elementIdList.get(j),
					elementIdList.get(j + 1), skipExpressionValue));
		}

		return process;
	}
}
