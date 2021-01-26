package com.dena.sb_druid.dao;

import com.dena.sb_druid.entity.userDAO;

public interface userDAOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(userDAO record);

    int insertSelective(userDAO record);

    userDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(userDAO record);

    int updateByPrimaryKey(userDAO record);
}