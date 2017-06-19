package com.property.data;

import com.alibaba.fastjson.JSONObject;

public class FormDefineFlowConfig {
	private String FlowType;
	private int FlowStatus;
	private JSONObject Content;
	public String getFlowType() {
		return FlowType;
	}
	public void setFlowType(String flowType) {
		FlowType = flowType;
	}
	public int getFlowStatus() {
		return FlowStatus;
	}
	public void setFlowStatus(int flowStatus) {
		FlowStatus = flowStatus;
	}
	public JSONObject getContent() {
		return Content;
	}
	public void setContent(JSONObject content) {
		Content = content;
	}
}
