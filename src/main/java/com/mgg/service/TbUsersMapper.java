package com.mgg.service;

import org.apache.ibatis.annotations.Mapper;

import com.mgg.model.master.User;
import java.util.List;

@Mapper
public interface TbUsersMapper {

	public List<User> getAllUsers();

    public User findByUsername(String username);
}
