package com.revature.bank;

import java.io.Serializable;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 13456L;
	int balance;
	private final int accountId;	
	Boolean approval;
	private String[] owners = new String[1];

	public Account(int accountId, String ownerId) {
		this.accountId = accountId;
		this.balance = 0;
		this.approval = null;
		this.owners[0] = ownerId;
	}
	
	public Account(int accountId, String[] ownerIds) {
		this.accountId = accountId;
		this.balance = 0;
		this.approval = null;
		this.owners = ownerIds;
	}

	public void deposit(int amount) throws IllegalArgumentException {
		if (amount > 0) {
			this.balance += amount;			
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void withdraw(int amount) throws IllegalArgumentException {
		if (amount > this.balance) {
			throw new IllegalArgumentException();
		} else {
			this.balance -= amount;			
		}
	}
	
	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String[] getOwners() {
		return owners;
	}

	public void setOwners(String[] owners) {
		this.owners = owners;
	}

	public int getAccountId() {
		return accountId;
	}
	
	public Boolean getApproval() {
		return approval;
	}
	
	public void setApproval(Boolean approval) {
		this.approval = approval;
	}
}