package com.property.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.property.dao.FormTableDefineMapper;
import com.property.model.FormTableDefine;
import com.property.service.IFormService;

@Service("formService")
public class FormServiceImpl implements IFormService {

	@Resource
	private FormTableDefineMapper formTableDefineMapper;

	@Override
	public FormTableDefine getFormTableDefineById(int formTableDefineId) {
		// TODO Auto-generated method stub
		return this.formTableDefineMapper.selectByPrimaryKey(formTableDefineId);
	}
	
	@Override
	public FormTableDefine getFormTableDefineByName(String formTableDefineName){
		return this.formTableDefineMapper.selectByName(formTableDefineName);
	}
	
	@Override
	public List<FormTableDefine> getFormList(){
		return this.formTableDefineMapper.getFormList();
	}
	
	@Override
	public int deleteFormTableDefineById(int formTableDefineId){
		return this.formTableDefineMapper.deleteByPrimaryKey(formTableDefineId);
	}
	
	@Override
	public int updateFormTableDefineById(FormTableDefine formTableDefine){
		return this.formTableDefineMapper.updateByPrimaryKey(formTableDefine);
	}
	
	@Override
	public int insertFormTableDefineById(FormTableDefine formTableDefine){
		return this.formTableDefineMapper.insert(formTableDefine);
	}

}
