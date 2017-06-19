package com.property.service;

import java.util.List;

import com.property.model.BUser;

public interface IBuserService {
	BUser getUserByloginPassword(String loginId,String password);
	List<BUser> queryBasicResByCondition(BUser buser, int offset, int limit);
	List<BUser> queryBasicResByCondition();
	int queryCountByCondition(BUser buser);
	int saveOrupdateOrg(BUser buser);
	BUser getUserById(String id);
	List<BUser> getUser(String username,int index);
	List<BUser> getUser(String orid);
	int deleteById(String id);
}
