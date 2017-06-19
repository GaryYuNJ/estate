package com.property.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.property.data.APIMessage;
import com.property.data.Node;
import com.property.data.NodeTree;
import com.property.model.Organization;
import com.property.service.IOrganizationService;

@Controller
@RequestMapping(value = "organization")
public class OrganizationControl {
	
	@Resource(name="organizationService")
	private IOrganizationService organizationService;
	
	@RequestMapping(value = "page", method = RequestMethod.GET)
	public String resouceManage(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 0);
		return "organization";
	}

	@ResponseBody
	@RequestMapping(value = "getAll.json")
	public String getAll() {
		return JSON.toJSONString(organizationService.getAllNodeTree());
	}
	
	@ResponseBody
	@RequestMapping(value = "getNodes.json")
	public String getNodesAll() {
		return JSON.toJSONString(organizationService.getAllNodes());
	}
	
	@ResponseBody
	@RequestMapping(value = "createOrupdate.json")
	public String createOrupdate(@RequestParam("orgModelJson") String orgModelJson) {
		Organization organization = JSON.parseObject(orgModelJson,
				Organization.class);
		APIMessage am = new APIMessage();
		am.setStatus(organizationService.saveOrupdateOrg(organization));
		am.setMessage(organization.getId());
		return JSON.toJSONString(am);
	}
	
	@ResponseBody
	@RequestMapping(value = "getOrgById.json")
	public String getOrgById(@RequestParam("id") String id) {
		return JSON.toJSONString(organizationService.getOrgById(id));
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteOrg.json")
	public String deleteOrg(@RequestParam("id") String id) {
		APIMessage am = new APIMessage();
		am.setStatus(organizationService.deleteOrgById(id));
		return JSON.toJSONString(am);
	}
	
}
