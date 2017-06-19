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
import com.property.data.DropDownData;
import com.property.model.BUser;
import com.property.model.Organization;
import com.property.service.IBuserService;

@Controller
@RequestMapping(value = "dropdowndata")
public class DropDownDataController {
	// 按照ID查询
	@ResponseBody
	@RequestMapping(value = "getProvince.json")
	public Object getProvince() {
		List<DropDownData> provincelist = new ArrayList<DropDownData>();
		provincelist.add(new DropDownData("beijing", "北京"));
		provincelist.add(new DropDownData("shanghai", "上海"));
		provincelist.add(new DropDownData("jiangsu", "江苏"));
		provincelist.add(new DropDownData("anhui", "安徽"));
		return provincelist;
	}

	// 按照ID查询
	@ResponseBody
	@RequestMapping(value = "getCityByProvinceId.json")
	public Object getCityByProvinceId(@RequestParam("id") String id) {
		List<DropDownData> citylist = new ArrayList<DropDownData>();
		switch (id) {
		case "beijing":
			citylist.add(new DropDownData("beijing", "北京"));
			break;
		case "shanghai":
			citylist.add(new DropDownData("shanghai", "上海"));
			break;
		case "jiangsu":
			citylist.add(new DropDownData("nanjing", "南京"));
			citylist.add(new DropDownData("nantong", "南通"));
			citylist.add(new DropDownData("yancheng", "盐城"));
			break;
		case "anhui":
			citylist.add(new DropDownData("wuhu", "芜湖"));
			citylist.add(new DropDownData("hefei", "合肥"));
			break;
		default:
			citylist.add(new DropDownData("others", "其它"));
			break;
		}
		return citylist;
	}
	
	// 按照ID查询
	@ResponseBody
	@RequestMapping(value = "getRegionByCityId.json")
	public Object getRegionByCityId(@RequestParam("id") String id) {
		List<DropDownData> regionlist = new ArrayList<DropDownData>();
		switch (id) {
		case "beijing":
			regionlist.add(new DropDownData("beijing1", "北京1"));
			regionlist.add(new DropDownData("beijing2", "北京2"));
			break;
		case "shanghai":
			regionlist.add(new DropDownData("shanghai1", "上海1"));
			regionlist.add(new DropDownData("shanghai2", "上海2"));
			regionlist.add(new DropDownData("shanghai3", "上海3"));
			break;
		case "nanjing":
			regionlist.add(new DropDownData("nanjing1", "南京1"));
			regionlist.add(new DropDownData("nanjing2", "南京2"));
			regionlist.add(new DropDownData("nanjing3", "南京3"));
			break;
		case "nantong":
			regionlist.add(new DropDownData("nantong1", "南通1"));
			regionlist.add(new DropDownData("nantong2", "南通2"));
			regionlist.add(new DropDownData("nantong3", "南通3"));
			break;
		case "yancheng":
			regionlist.add(new DropDownData("yancheng1", "盐城1"));
			regionlist.add(new DropDownData("yancheng2", "盐城2"));
			regionlist.add(new DropDownData("yancheng3", "盐城3"));
			break;
		case "wuhu":
			regionlist.add(new DropDownData("wuhu1", "芜湖1"));
			regionlist.add(new DropDownData("wuhu2", "芜湖2"));
			break;
		case "hefei":
			regionlist.add(new DropDownData("hefei1", "合肥1"));
			regionlist.add(new DropDownData("hefei2", "合肥2"));
			break;
		default:
			regionlist.add(new DropDownData("other1", "其它1"));
			break;
		}
		return regionlist;
	}
}
