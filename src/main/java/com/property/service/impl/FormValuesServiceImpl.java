package com.property.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.property.dao.FlowConfigMapper;
import com.property.dao.FormValuesMapper;
import com.property.model.FlowConfig;
import com.property.model.FormValues;
import com.property.service.IFlowConfigService;
import com.property.service.IFormValuesService;

@Service("formvaluesService")
public class FormValuesServiceImpl implements IFormValuesService{

	@Resource
	private FormValuesMapper FormValuesMapper;
	
	@Override
	public FormValues getFormValuesById(String id){
		// TODO Auto-generated method stub
		return this.FormValuesMapper.selectById(id);
	}
	
	@Override
	public List<FormValues> getFormValuesByFormdefineID(String formdefineid){
		// TODO Auto-generated method stub
		return this.FormValuesMapper.selectByFormDefineID(formdefineid);
	}
	
	@Override
	public int updateFormValues(FormValues formvalues)
	{
		// TODO Auto-generated method stub
		return this.FormValuesMapper.updateByPrimaryKey(formvalues);
	}
	
	@Override
	public int insertFormValues(FormValues formvalues){
		// TODO Auto-generated method stub
		return this.FormValuesMapper.insert(formvalues);
	}
}
