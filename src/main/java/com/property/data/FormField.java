package com.property.data;

//API 返回状态
public class FormField {
	private String label;
	private String field_type;
	private Boolean required;
	private Boolean need_print;
	private String cid;
	private String access;
	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	private Object field_options;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Boolean getNeed_print() {
		return need_print;
	}
	public void setNeed_print(Boolean need_print) {
		this.need_print = need_print;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Object getField_options() {
		return field_options;
	}
	public void setField_options(Object field_options) {
		this.field_options = field_options;
	}
}
