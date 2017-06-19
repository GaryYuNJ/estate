package com.property.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.property.dao.BUserMapper;
import com.property.dao.OrganizationMapper;
import com.property.model.BUser;
import com.property.model.Organization;
import com.property.service.IBuserService;

@Service("buserService")
public class BuserServiceImpl implements IBuserService {

	@Resource
	private BUserMapper bUserMapper;
	@Resource
	private OrganizationMapper organizationDao;

	@Override
	public BUser getUserByloginPassword(String loginId, String password) {
		return bUserMapper.selectByloginPassword(loginId, password);
	}

	@Override
	public List<BUser> queryBasicResByCondition(BUser buser, int offset, int limit) {
		return bUserMapper.selectBuserListByCondition(buser, offset, limit);
	}

	@Override
	public List<BUser> queryBasicResByCondition() {
		return bUserMapper.selectBuserList();
	}

	@Override
	public int queryCountByCondition(BUser buser) {
		return bUserMapper.selectCountByCondition(buser);
	}

	@Override
	public int saveOrupdateOrg(BUser buser) {
		if (StringUtils.isNullOrEmpty(buser.getId())) {
			buser.setPassword("888888");
			return bUserMapper.insertSelective(buser);
		} else {
			return bUserMapper.updateByPrimaryKeySelective(buser);
		}
	}

	@Override
	public BUser getUserById(String id) {
		return bUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BUser> getUser(String username, int index) {
		List<BUser> buserListResult = new ArrayList<BUser>();
		List<BUser> buserList = bUserMapper.selectBuserListByLoginID(username);
		BUser user = buserList.get(0);
		Organization organization = organizationDao.selectByPrimaryKey(buserList.get(0).getOrid());
		String Oid = organization.getId();
		String pid = organization.getPid();
		Short usertype = user.getType();
		if (usertype != null && usertype == 1) {
			Organization organizationtemp = organizationDao.selectByPrimaryKey(pid);
			Oid = organizationtemp.getId();
			pid = organizationtemp.getPid();
		}
		for (int i = 0; i < index; i++) {
			if (i == index - 1) {
				break;
			} else {
				Organization organizationtemp = organizationDao.selectByPrimaryKey(pid);
				if (organizationtemp == null)
					return buserListResult;
				Oid = organizationtemp.getId();
				pid = organizationtemp.getPid();
			}
		}
		buserListResult = bUserMapper.selectBuserListByOrid(Oid);
		return buserListResult;
	}

	@Override
	public List<BUser> getUser(String orid) {
		BUser buser = new BUser();
		buser.setOrid(orid);
		List<BUser> buserListResult = bUserMapper.selectBuserListByOrid(orid);
		return buserListResult;
	}

	@Override
	public int deleteById(String id) {
		return bUserMapper.deleteByPrimaryKey(id);
	}
}
