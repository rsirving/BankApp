package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revature.pojo.Account;
import com.revature.pojo.User;
import com.revature.util.ConnectionFactory;

public class UserAccountDaoImpl implements UserAccountDao {
	@Override
	public void linkUserToNewAccount(User user) {
		String sql = "INSERT INTO USER_ACCOUNTS (USERID, ACCOUNTID) VALUES (?, (SELECT MAX(ACCOUNTID) FROM ACCOUNTS))";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void linkUserAccount(User user, Account account) {
		String sql = "INSERT INTO USER_ACCOUNTS VALUES (?, ?)";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, user.getUserId());
			ps.setInt(2, account.getAccountId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUserAccount(Account account) {
		String sql = "DELETE FROM USER_ACCOUNTS WHERE ACCOUNTID = ?";
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, account.getAccountId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
