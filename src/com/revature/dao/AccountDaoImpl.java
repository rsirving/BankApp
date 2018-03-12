package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.pojo.Account;
import com.revature.pojo.User;
import com.revature.util.ConnectionFactory;

public class AccountDaoImpl implements AccountDao {
	
	UserAccountDaoImpl uad = new UserAccountDaoImpl();

	@Override
	public void createAccount(Account account, User user) {
		String sql = "INSERT INTO ACCOUNTS (BALANCE, APPROVAL) VALUES (?, ?)";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, account.getBalance());
			ps.setInt(2, account.getApproval());
			ps.executeUpdate();
			uad.linkUserToNewAccount(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Account retrieveAccountById(int id) {
		Account ac = new Account();
		String sql = "SELECT * FROM ACCOUNTS WHERE ACCOUNTID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ac.setAccountId(rs.getInt(1));
				ac.setBalance(rs.getInt(2));
				ac.setApproval(rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ac;
	}

	@Override
	public ArrayList<Account> retrieveAllAccounts() {
		String sql = "SELECT * FROM ACCOUNTS";
		ArrayList<Account> ac_list = new ArrayList<Account>();
		Account ac = new Account();
		PreparedStatement ps;
		try {
			ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ac.setAccountId(rs.getInt(1));
				ac.setBalance(rs.getInt(2));
				ac.setApproval(rs.getInt(3));
				ac_list.add(ac);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ac_list;
	}

	@Override
	public void updateAccount(Account account) {
		String sql = "UPDATE ACCOUNTS SET BALANCE = ?, APPROVAL = ? WHERE ACCOUNTID = ?";
		PreparedStatement ps;
		try {
			ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, account.getBalance());
			ps.setInt(2, account.getApproval());
			ps.setInt(3, account.getAccountId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Account> retrieveAccountsByUserId(int userid) {
		String sql = "SELECT * FROM USER_ACCOUNTS JOIN ACCOUNTS ON (USER_ACCOUNTS.ACCOUNTID = ACCOUNTS.ACCOUNTID) WHERE USER_ACCOUNTS.USERID = ?";
		ArrayList<Account> ac_list = new ArrayList<Account>();
		Account ac = new Account();
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ac.setAccountId(rs.getInt(3));
				ac.setBalance(rs.getInt(5));
				ac.setApproval(rs.getInt(6));
				ac_list.add(ac);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ac_list;
	} 
	
	@Override
	public void deleteAccount(Account account) {
		String sql = "DELETE FROM ACCOUNTS WHERE ACCOUNTID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, account.getAccountId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
