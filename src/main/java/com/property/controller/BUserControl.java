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
import com.property.data.BootstrapTableData;
import com.property.model.BUser;
import com.property.model.Organization;
import com.property.service.IBuserService;

@Controller
@RequestMapping(value = "buser")
public class BUserControl {

	@Resource
	private IBuserService buserService;

	@RequestMapping(value = "page", method = RequestMethod.GET)
	public String resouceManage(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 1);
		return "buser";
	}

	// 查询所有用户
	@ResponseBody
	@RequestMapping(value = "buserSearchAll.json")
	public Object buserSearchAll() {
		List<BUser> buserList = buserService.queryBasicResByCondition();
		List<BUser> buserListResult = new ArrayList<BUser>();
		for(BUser user : buserList){
			if(user.getStatus()==1){
				buserListResult.add(user);
			}
		}
		return buserListResult;
	}

	// 分页查询
	@ResponseBody
	@RequestMapping(value = "buserSearch.json")
	public String bUserSearch(String search,
			@RequestParam("limit") Integer limit,
			@RequestParam("offset") Integer offset) {
		BUser buser = JSON.parseObject(search, BUser.class);
		BootstrapTableData bData = new BootstrapTableData();

		List<BUser> buserList = buserService.queryBasicResByCondition(buser,
				offset, limit);
		if (null != buserList && buserList.size() > 0) {
			bData.setRows(buserList);
			bData.setPage(offset / limit + 1);
			bData.setTotal(buserService.queryCountByCondition(buser));
		} else {
			bData.setPage(0);
			bData.setRows(new Object());
			bData.setTotal(0);
		}
		return JSON.toJSONString(bData);
	}

	// 按照ID查询
	@ResponseBody
	@RequestMapping(value = "getUserById.json")
	public String getUserById(@RequestParam("id") String id) {
		return JSON.toJSONString(buserService.getUserById(id));
	}

	// 新增
	@ResponseBody
	@RequestMapping(value = "createOrupdate.json")
	public String createOrupdate(
			@RequestParam("bUserModelJson") String bUserModelJson) {
		BUser buser = JSON.parseObject(bUserModelJson, BUser.class);
		APIMessage am = new APIMessage();
		if(buser!=null){
			if(buser.getStatus()==null){
				buser.setStatus((short)0);
			}
			if(buser.getType()==null){
				buser.setType((short)0);
			}
		}
		am.setStatus(buserService.saveOrupdateOrg(buser));
		am.setMessage(buser.getId());
		return JSON.toJSONString(am);
	}

	// 删除
	@ResponseBody
	@RequestMapping(value = "delete.json")
	public String delete(@RequestParam("id") String id) {
		APIMessage am = new APIMessage();
		am.setStatus(buserService.deleteById(id));
		return JSON.toJSONString(am);
	}
}
