package com.revature.dao;

import java.util.ArrayList;

import com.revature.pojo.User;

public interface UserDao {
	public void createUser(User user);
	public User retrieveUserById(int id);
	public User retrieveUserByUsername(String username);
	public ArrayList<User> retrieveAllUsers();
	public void updateUser(User user);
}
