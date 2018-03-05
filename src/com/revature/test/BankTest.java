package com.revature.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.bank.*;

import static org.junit.Assert.*;

import java.util.*;

public class BankTest {
	
	// TODO Implement more tests. The way I made the app, there's not really much I can actually 'test' due to lack of return statements.
	//		Figure something out.

	private static final Main main = new Main();
	private static final Warehouse warehouse = main.readWare();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testFetchUser() {
		assertEquals("user", warehouse.fetchUser("admin").getUsername());
	}
	
	@Test
	public void testFetchEmployee() {
		assertEquals("employee", warehouse.fetchUser("employee").getUsername());
	}
	
	@Test
	public void testFetchAdmin() {
		assertEquals("admin", warehouse.fetchUser("admin").getUsername());
	}
}
