package com.property.service;

import java.util.List;

import com.property.model.FormTableDefine;

public interface IFormService {
	public FormTableDefine getFormTableDefineById(int formTableDefineId); 
	
	public FormTableDefine getFormTableDefineByName(String formTableDefineName); 
	
	public List<FormTableDefine> getFormList();
	
	public int deleteFormTableDefineById(int formTableDefineId); 
	
	public int updateFormTableDefineById(FormTableDefine formTableDefine);
	
	public int insertFormTableDefineById(FormTableDefine formTableDefine);
}
