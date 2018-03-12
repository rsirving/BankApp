package com.revature.dao;

import com.revature.pojo.Account;
import com.revature.pojo.User;

public interface UserAccountDao {
	public void linkUserToNewAccount(User user);
	public void linkUserAccount(User user, Account account);
	public void deleteUserAccount(Account account);
}
