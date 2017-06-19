package com.property.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.property.dao.BUserMapper;
import com.property.dao.OrganizationMapper;
import com.property.data.Node;
import com.property.data.NodeTree;
import com.property.model.BUser;
import com.property.model.Organization;
import com.property.service.IOrganizationService;

@Service("organizationService")
public class OrganizationServiceImpl implements IOrganizationService {
	@Resource
	private OrganizationMapper organizationDao;
	@Resource
	private BUserMapper bUserDao;

	@Override
	public List<NodeTree> getAllNodeTree() {
		List<NodeTree> nodeList = organizationDao.selectNodeByLevel(0);
		getAllChildNodeTree(nodeList);
		return nodeList;
	}

	private void getAllChildNodeTree(List<NodeTree> listNode) {
		if (listNode == null)
			return;
		for (int i = 0; i < listNode.size(); i++) {
			if (listNode.get(i).getLevel() == 0) {
				listNode.get(i).setType("root");
			}
			listNode.get(i).setChildren(
					organizationDao.selectChildNode(listNode.get(i).getId(),
							listNode.get(i).getLevel() + 1));
			getAllChildNodeTree(listNode.get(i).getChildren());
		}
	}
	@Override
	public List<Node> getAllNodes() {
		List<Node> nodes = organizationDao.selectNodesByLevel(0);
		getAllChildNode(nodes);
		return nodes;
	}
	
	private void getAllChildNode(List<Node> listNode) {
		if (listNode == null)
			return;
		for (int i = 0; i < listNode.size(); i++) {
			listNode.get(i).setNodes(
					organizationDao.selectChildNodes(listNode.get(i).getId(),
							listNode.get(i).getLevel() + 1));
			getAllChildNode(listNode.get(i).getNodes());
		}
	}

	@Override
	public int saveOrupdateOrg(Organization Organization) {
		if (!StringUtils.isNullOrEmpty(Organization.getPid())) {
			Organization.setLevel(organizationDao.selectByPrimaryKey(
					Organization.getPid()).getLevel()+1);
		}
		if (StringUtils.isNullOrEmpty(Organization.getId())) {
			return organizationDao.insertSelective(Organization);
		} else {
			return organizationDao.updateByPrimaryKeySelective(Organization);
		}
	}

	@Override
	public Organization getOrgById(String id) {
		return organizationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteOrgById(String id) {
		//判断内容是否包含子节点和成员
		List<Node> nodes= organizationDao.selectChildrenOrgByPId(id);
		BUser buser =new BUser();
		buser.setOrid(id);
		if((null==nodes||nodes.isEmpty())&&bUserDao.selectCountByCondition(buser)==0){
			return organizationDao.deleteByPrimaryKey(id);
		}
		return 0;
	}
}
