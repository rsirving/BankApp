package com.revature.pojo;

public class User {
	int userId, permissionLevel;
	String username, password;
	

	public User() {
		super();
	}
	
	public User(String username, String password, int permissionLevel) {
		super();
		this.username = username;
		this.password = password;
		this.permissionLevel = permissionLevel;
	}
	
	public User(int id, String username, String password, int permissionLevel) {
		super();
		this.userId = id;
		this.username = username;
		this.password = password;
		this.permissionLevel = permissionLevel;
	}
	
	@Override
	public String toString() {
		return "Pirate #: " + userId + ", Pirate name: " + username + ", Secret phrase: " + password + ", Level: " + permissionLevel;
	}
	
	public String toStringNoPsw() {
		return "Pirate #: " + userId + ", Pirate name: " + username + ", Level: " + permissionLevel;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getPermissionLevel() {
		return permissionLevel;
	}
	
	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
