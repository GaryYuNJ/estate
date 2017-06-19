package com.property.business;

import java.util.List;

import javax.annotation.Resource;

import com.property.constants.CustomProcessJsonConstants;
import com.property.data.CustomAssign;
import com.property.model.BUser;
import com.property.service.IBuserService;
import com.property.utils.SpringContextHolder;

public class AssignSelectHelper {

	public String getAssigner(CustomAssign customAssign) {
		IBuserService buserService = SpringContextHolder.getBean("buserService", IBuserService.class);
		String assigner = "";
		switch (customAssign.getType()) {
		case CustomProcessJsonConstants.CUSTOM_ASSIGN_TYPE_ROLE:
			List<BUser> users = buserService.getUser(customAssign.getValue());
			if (users != null || !users.isEmpty()) {
				assigner = users.get(0).getLoginid();
			}
			break;
		default:
			assigner = customAssign.getAssign();
			break;
		}

		return assigner;
	}
}
