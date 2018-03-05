package com.revature.bank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Warehouse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1142814203676107768L;
	private HashMap<String, User> users = new HashMap<String, User>();
	private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	private Random random = new Random();
	
	public Warehouse() {
		this.users = users;
		this.accounts = accounts;
	}
	
	public void newUser(String username, String password) {
		User u = new User(username, password, 0);
		this.users.put(username, u);
	}

	public void newEmployee(String username, String password) {
		User e = new User(username, password, 1);
		this.users.put(username, e);
	}

	public void newAdmin(String username, String password) {
		User a = new User(username, password, 2);
		this.users.put(username, a);
	}
	
	public User fetchUser(String username) {
		if (this.users.containsKey(username)) {
			return this.users.get(username);
		} else {
			return null;
		}
	}
	
	public int randomAccountNumber() {
		// Creates a random number from 0 to 9999 that will be assigned as the account's ID #.
		int number = random.nextInt(10000);
		if (accounts.containsKey(number)) {
			// If it just *happens* to generate a number that's already being used, this runs the function again.
			// This can easily hit an infinite loop if every number from 0-9999 is being used, but that's way outside the 
			// purview of this assignment.
			return randomAccountNumber();
		} else {
			return number;
		}
	}
	
	public Account newAccount(String owner) {
		// If given a single owner, converts to an owner array.
		String[] owners = new String[] {owner};
		Account a = new Account(randomAccountNumber(), owners);
		return a;
	}
	
	public Account newAccount(String[] owners) {
		Account a = new Account(randomAccountNumber(), owners);
		return a;
	}
	
	public Account fetchAccount(int id) {
		if (this.accounts.containsKey(id)) {
			return this.accounts.get(id);
		} else {
			return null;
		}
	}
	
	public void accountApply(String username) {
		User u = fetchUser(username);
		Account a = newAccount(username);
		u.accountApply(a);
		this.accounts.put(a.getAccountId(), a);
	}
	
	public void jointAccountApply(String[] owners) {
		Account a = newAccount(owners);
		for (String owner : owners) {
			User u = fetchUser(owner);
			u.accountApply(a);
		}
		this.accounts.put(a.getAccountId(), a);
	}
	
	public void removeAccountFromUser(String owner, int account) {
		User u = fetchUser(owner);
		u.ownedAccounts.remove(fetchAccount(account));
	}
	
	public void removeAccount(int accountId) {
		accounts.remove(accountId);
	}
	
	public void approveAccount(boolean judgement, int accountId) {
		Account a = fetchAccount(accountId);
		a.setApproval(judgement);
	}
	
	public void deposit(int amount, int accountId) {
		Account account = fetchAccount(accountId);
		account.deposit(amount);
	}
	
	public void withdraw(int amount, int accountId) {
		Account account = fetchAccount(accountId);
		account.withdraw(amount);
	}
	
	// Getters & Setters

	public HashMap<Integer, Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(HashMap<Integer, Account> accounts) {
		this.accounts = accounts;
	}
	
	public HashMap<String, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
}
