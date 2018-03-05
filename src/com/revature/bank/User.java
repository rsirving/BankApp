package com.revature.bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3520104275553299756L;
	String username, password;
	int permissionLevel;
	List<Account> ownedAccounts = new ArrayList<Account>();

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String username, String password, int permissionLevel) {
		this.username = username;
		this.password = password;
		this.permissionLevel = permissionLevel;
		this.ownedAccounts = ownedAccounts;
	}
	
	public void accountApply(Account a) {
		this.ownedAccounts.add(a);
	}

	// Getters & Setters
	
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

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	public List<Account> getOwnedAccounts() {
		return ownedAccounts;
	}

	public void setOwnedAccounts(List<Account> ownedAccounts) {
		this.ownedAccounts = ownedAccounts;
	}

}
