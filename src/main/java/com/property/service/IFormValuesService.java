package com.property.service;

import java.util.List;

import com.property.model.FormValues;;

public interface IFormValuesService {
	public FormValues getFormValuesById(String id); 
	
	public List<FormValues> getFormValuesByFormdefineID(String formdefineid); 
	
	public int updateFormValues(FormValues formvalues);
	
	public int insertFormValues(FormValues formvalues);
}
