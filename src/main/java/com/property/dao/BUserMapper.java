package com.property.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.property.model.BUser;

public interface BUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(BUser record);

    int insertSelective(BUser record);

    BUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BUser record);

    int updateByPrimaryKey(BUser record);
    
    BUser selectByloginPassword(@Param("loginId")String loginId,
			@Param("password")String password);
    
    List<BUser> selectBuserListByCondition(@Param("buser")BUser buser,@Param("startRow") Integer startRow,
			@Param("pageSize") Integer pageSize);
	int selectCountByCondition(@Param("buser")BUser buser);
	
	List<BUser> selectBuserList();
	
	List<BUser> selectBuserListByLoginID(@Param("loginId")String loginId);
	
	List<BUser> selectBuserListByOrid(@Param("orId")String orId);
}