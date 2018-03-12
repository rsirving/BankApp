package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.pojo.User;
import com.revature.util.ConnectionFactory;

public class UserDaoImpl implements UserDao {
	
	@Override
	public void createUser(User user) {
		String sql = "INSERT INTO USERS (USERNAME, PASSWORD, PERMISSION_LEVEL) VALUES (?, ?, ?)";
		PreparedStatement ps;
		try {
			ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getPermissionLevel());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User retrieveUserById(int id) {
		User us = new User();
		String sql = "SELECT * FROM USERS WHERE USERID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				us.setUserId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				us.setPassword(rs.getString(3));
				us.setPermissionLevel(rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return us;
	}
	
	public ArrayList<String> getUsersOwningAccount(int accountId){
		ArrayList<String> owners = new ArrayList<String>();
		String sql = "SELECT USERNAME FROM USER_ACCOUNTS JOIN USERS ON (USER_ACCOUNTS.USERID = USERS.USERID) JOIN ACCOUNTS ON (USER_ACCOUNTS.ACCOUNTID = ACCOUNTS.ACCOUNTID) WHERE ACCOUNTS.ACCOUNTID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, accountId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				owners.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return owners;
	}
	
	@Override
	public User retrieveUserByUsername(String username) {
		User us = new User();
		String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				us.setUserId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				us.setPassword(rs.getString(3));
				us.setPermissionLevel(rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return us;
	}
	
	@Override
	public ArrayList<User> retrieveAllUsers() {
		String sql = "SELECT * FROM USERS";
		ArrayList<User> us_list = new ArrayList<User>();
		User us = new User();
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				us.setUserId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				us.setPassword(rs.getString(3));
				us.setPermissionLevel(rs.getInt(4));
				us_list.add(us);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return us_list;
	}

	@Override
	public void updateUser(User user) {
		String sql = "UPDATE USERS SET USERNAME = ?, PASSWORD = ?, PERMISSION_LEVEL = ? WHERE USERID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getPermissionLevel());
			ps.setInt(4, user.getUserId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
