package com.revature.dao;

import java.util.ArrayList;

import com.revature.pojo.Account;
import com.revature.pojo.User;

public interface AccountDao {
	public void createAccount(Account account, User user);
	public Account retrieveAccountById(int id);
	public ArrayList<Account> retrieveAllAccounts();
	public ArrayList<Account> retrieveAccountsByUserId(int userid);
	public void updateAccount(Account account);
	public void deleteAccount(Account account);
}
