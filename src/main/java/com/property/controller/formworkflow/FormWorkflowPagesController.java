package com.property.controller.formworkflow;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "fwpages")
public class FormWorkflowPagesController {

	/**
	 * 新建activiti审批流程，管理员功能，不对普通用户开放
	 */
	@RequestMapping(value = "newworkflowbyadmin", method = RequestMethod.GET)
	public String NewWorkFlowByAdmin(ModelMap model) {
		return "fw/background/newworkflowbyadmin";
	}

	/**
	 * 新建审批表单，数据库新建表
	 */
	@RequestMapping(value = "newformtable", method = RequestMethod.GET)
	public String NewForm(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 3);
		return "fw/background/newformtable";
	}

	/**
	 * 后台审批列表
	 */
	@RequestMapping(value = "bfwlist", method = RequestMethod.GET)
	public String BackgroundFormList(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 2);
		model.put("breadcrumbModul", "审批管理");
		model.put("breadcrumbPage", "审批列表");
		return "fw/background/bfwlist";
	}

	/**
	 * 设置审批人
	 */
	@RequestMapping(value = "assignset", method = RequestMethod.GET)
	public String AssignSet(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 2);
		return "fw/background/assignset";
	}

	/**
	 * 前台用户登录页面
	 */
	@RequestMapping(value = "frontlogin", method = RequestMethod.GET)
	public String FrontLogin(ModelMap model) {
		return "fw/front/frontlogin";
	}

	/**
	 * 审批流程列表页
	 */
	@RequestMapping(value = "ffwlist", method = RequestMethod.GET)
	public String FrontFormListUser(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 0);
		return "fw/front/ffwlist";
	}

	/**
	 * 我申请的审批列表页
	 */
	@RequestMapping(value = "myapplylist", method = RequestMethod.GET)
	public String MyApplyList(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 2);
		return "fw/front/myapplylist";
	}

	/**
	 * 待我审批列表页
	 */
	@RequestMapping(value = "tohandlelist", method = RequestMethod.GET)
	public String ToHandleList(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 1);
		return "fw/front/tohandlelist";
	}

	// 我审批的列表
	@RequestMapping(value = "handlebymehis", method = RequestMethod.GET)
	public String HandleByMeHis(ModelMap model) {
		// 页面菜单样式需要
		model.put("pageIndex", 3);
		return "fw/front/handlebymehis";
	}

	/**
	 * 新建审批实例，发起一个审批流程
	 */
	@RequestMapping(value = "newfwinstance", method = RequestMethod.GET)
	public String NewFormWorkflowInstance(ModelMap model) {
		// 页面菜单样式需要
		return "fw/front/newfwinstance";
	}

	/**
	 * 审批实例详情页
	 */
	@RequestMapping(value = "fwinstancedetial", method = RequestMethod.GET)
	public String shenpiDetail(ModelMap model) {
		// 页面菜单样式需要
		return "fw/front/fwinstancedetial";
	}

	/**
	 * 审批页面
	 */
	@RequestMapping(value = "handlefw", method = RequestMethod.GET)
	public String HandleFWInsatance(ModelMap model) {
		// 页面菜单样式需要
		return "fw/front/handlefw";
	}

	/**
	 * 发起者重新提交或撤销页面，被驳回时
	 */
	@RequestMapping(value = "resubmitfw", method = RequestMethod.GET)
	public String shenpi2(ModelMap model) {
		// 页面菜单样式需要
		return "fw/front/resubmitfw";
	}

	/**
	 * 流程图跟踪
	 */
	@RequestMapping(value = "flowtrace", method = RequestMethod.GET)
	public String usertaskset(ModelMap model) {
		// 页面菜单样式需要
		return "fw/front/flowtrace";
	}

	/**
	 * 流程图 设置usertask
	 */
	@RequestMapping(value = "usertaskset/{processdefineid}", method = RequestMethod.GET)
	public String usertaskset2(ModelMap model, @PathVariable("processdefineid") String processdefineid) {
		// 页面菜单样式需要
		model.put("pageIndex", 2);
		model.put("breadcrumbModul", "审批管理");
		model.put("breadcrumbPage", "配置用户流程节点");
		model.put("processdefineid", processdefineid);
		return "fw/background/usertaskset";
	}
}
