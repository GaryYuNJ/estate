package com.property.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.property.dao.FlowConfigMapper;
import com.property.model.FlowConfig;
import com.property.service.IFlowConfigService;

@Service("flowconfigService")
public class FlowConfigServiceImpl implements IFlowConfigService{

	@Resource
	private FlowConfigMapper FlowConfigMapper;
	
	@Override
	public List<FlowConfig> getFlowConfigByPRKEY(String processdefine_key){
		// TODO Auto-generated method stub
		return this.FlowConfigMapper.selectByKey(processdefine_key);
	}
	
	@Override
	public List<FlowConfig> getFlowConfigByPRID(String processdefine_id){
		// TODO Auto-generated method stub
		return this.FlowConfigMapper.selectByPRID(processdefine_id);
	}
	
	@Override
	public int updateFlowConfigById(FlowConfig formTableDefine)
	{
		// TODO Auto-generated method stub
		return this.FlowConfigMapper.updateByPrimaryKey(formTableDefine);
	}
	
	@Override
	public int insertFlowConfig(FlowConfig formTableDefine){
		// TODO Auto-generated method stub
		return this.FlowConfigMapper.insert(formTableDefine);
	}
}
