package com.property.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.property.data.Node;
import com.property.data.NodeTree;
import com.property.model.Organization;

public interface OrganizationMapper {
	int deleteByPrimaryKey(String id);

	int insert(Organization record);

	int insertSelective(Organization record);

	Organization selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Organization record);

	int updateByPrimaryKey(Organization record);

	List<NodeTree> selectNodeByLevel(Integer level);

	List<NodeTree> selectChildNode(@Param("parentId") String parentId,
			@Param("level") Integer level);

	List<Node> selectNodesByLevel(Integer level);

	List<Node> selectChildNodes(@Param("parentId") String parentId,
			@Param("level") Integer level);
	List<Node> selectChildrenOrgByPId(@Param("parentId") String parentId);
	
}