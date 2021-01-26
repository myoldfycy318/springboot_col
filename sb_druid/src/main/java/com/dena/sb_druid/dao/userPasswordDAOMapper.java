package com.dena.sb_druid.dao;

import com.dena.sb_druid.entity.userPasswordDAO;

public interface userPasswordDAOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(userPasswordDAO record);

    int insertSelective(userPasswordDAO record);

    userPasswordDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(userPasswordDAO record);

    int updateByPrimaryKey(userPasswordDAO record);
}