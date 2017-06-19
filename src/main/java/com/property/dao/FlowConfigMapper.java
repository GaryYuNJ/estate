package com.property.dao;

import java.util.List;

import com.property.model.FlowConfig;

public interface FlowConfigMapper {
    
    List<FlowConfig> selectByKey(String key);
    
    List<FlowConfig> selectByPRID(String id);

    int insert(FlowConfig record);

    int updateByPrimaryKey(FlowConfig record);
}