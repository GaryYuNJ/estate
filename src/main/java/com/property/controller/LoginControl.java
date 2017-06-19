package com.property.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.property.model.BUser;
import com.property.service.IBuserService;

@Controller
public class LoginControl {
	
	@Resource(name="buserService")
	private IBuserService buserService;
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,ModelMap model) {
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		BUser bUser=buserService.getUserByloginPassword(userName, password);
		
		request.getSession().setAttribute("user", bUser);
		//暂时跳转到资源页
		return "redirect:organization/page";
	}

	@RequestMapping(value = "logOut", method = RequestMethod.GET)
	public String loginOut(HttpServletRequest request,ModelMap model) {
		request.getSession().removeAttribute("user");
		//暂时跳转到资源页
		return "redirect:login";
	}

	public IBuserService getBuserService() {
		return buserService;
	}

	public void setBuserService(IBuserService buserService) {
		this.buserService = buserService;
	}


}
