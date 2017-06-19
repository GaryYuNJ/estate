package com.property.model;

import java.util.Date;

public class FormValues {
	private String id; // 
	
	private Integer formdefine_id;

	private String tablename_en; // 
	
	private String values; // 
	
	private Integer status;

	private String createuser;

	private String updateuser;

	private Date createtime;

	private Date updatetime;

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id == null ? null : id.trim();
	}

	public Integer getFormdefineid() {
		return formdefine_id;
	}

	public void setFormdefineid(Integer formdefine_id) {
		this.formdefine_id = formdefine_id;
	}

	public String gettablename_en() {
		return tablename_en;
	}

	public void settablename_en(String tablename_en) {
		this.tablename_en = tablename_en == null ? null : tablename_en.trim();
	}

	public String getvalues() {
		return values;
	}

	public void setvalues(String values) {
		this.values = values == null ? null : values.trim();
	}

	public String getcreateuser() {
		return createuser;
	}

	public void setcreateuser(String createuser) {
		this.createuser = createuser == null ? null : createuser.trim();
	}

	public String getupdateuser() {
		return updateuser;
	}

	public void setupdateuser(String updateuser) {
		this.updateuser = updateuser == null ? null : updateuser.trim();
	}

	public Integer getstatus() {
		return status;
	}

	public void setstatus(Integer status) {
		this.status = status;
	}

	public Date getcreatetime() {
		return createtime;
	}

	public void setcreatetime(Date createtime) {
		this.createtime = createtime == null ? new Date() : createtime;
		;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime == null ? new Date() : updatetime;
	}
}