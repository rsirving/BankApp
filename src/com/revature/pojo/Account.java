package com.revature.pojo;

public class Account {
	int accountId, balance, approval;
	
	public Account() {
		super();
	}
	
	public Account(int balance, int approval) {
		super();
		this.balance = balance;
		this.approval = approval;
	}
	
	public Account(int accountId, int balance, int approval) {
		super();
		this.accountId = accountId;
		this.balance = balance;
		this.approval = approval;
	}
	
	@Override
	public String toString() {
		return "Treasure map #: " + accountId + ", Contains: " + balance + " pieces o' eight.";
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getApproval() {
		return approval;
	}

	public void setApproval(int approval) {
		this.approval = approval;
	}
	
}
