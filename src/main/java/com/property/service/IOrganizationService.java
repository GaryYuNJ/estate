package com.property.service;

import java.util.List;

import com.property.data.Node;
import com.property.data.NodeTree;
import com.property.model.Organization;

public interface IOrganizationService {
	List<NodeTree> getAllNodeTree();
	int saveOrupdateOrg(Organization Organization );
	Organization getOrgById(String id);
	List<Node> getAllNodes();
	int deleteOrgById(String id);
}
