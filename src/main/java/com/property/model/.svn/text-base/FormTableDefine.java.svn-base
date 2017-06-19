package com.property.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.property.data.FormDefineFlowConfig;
import com.property.data.FormField;

public class FormTableDefine {
	private Integer id;

	private String tableName; // 审批的名称

	private String description; // 描述

	private String tableNameEn; // 审批名称 拼音转 字母

	private String fieldsJson; // 表单字段 json

	private String activitiJson;// 绑定的activiti流程

	private Integer state; // 状态 0: 初始状态，流程未创建 1: 流程已经定义 2:启用状态 3:停用状态

	private Date cratetime;

	private Date updatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName == null ? null : tableName.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = tableName == null ? null : description.trim();
	}

	public String getTableNameEn() {
		return tableNameEn;
	}

	public void setTableNameEn(String tableNameEn) {
		this.tableNameEn = tableNameEn == null ? null : tableNameEn.trim();
	}

	public String getFieldsJson() {
		return fieldsJson;
	}

	public void setFieldsJson(String fieldsJson) {
		this.fieldsJson = fieldsJson == null ? null : fieldsJson.trim();
	}

	public String getActivitiJson() {
		return activitiJson;
	}

	public void setActivitiJson(String activitiJson) {
		this.activitiJson = activitiJson == null ? null : activitiJson.trim();
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCratetime() {
		return cratetime;
	}

	public void setCratetime(Date cratetime) {
		this.cratetime = cratetime == null ? new Date() : cratetime;
		;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime == null ? new Date() : updatetime;
	}

	/*** 扩展方法 start ***/
	public List<FormField> getFormFields() {
		List<FormField> formFields = new ArrayList<FormField>();
		if (this.fieldsJson != null && !this.fieldsJson.isEmpty()) {
			JSONObject json = new JSONObject(this.fieldsJson);
			Object fields = json.get("fields");
			formFields = JSON.parseArray(fields + "", FormField.class);
		}
		return formFields;
	}

	public FormDefineFlowConfig getFormDefineFlowConfig() {
		FormDefineFlowConfig formDefineFlowConfig = new FormDefineFlowConfig();
		if (!(this.activitiJson == null) && !this.activitiJson.isEmpty()) {
			formDefineFlowConfig = JSON.parseObject(this.activitiJson, FormDefineFlowConfig.class);
		}
		return formDefineFlowConfig;
	}
	/*** 扩展方法 end ***/
}