package com.revature.bank;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.dao.*;
import com.revature.pojo.*;
import com.revature.util.*;
import com.revature.bank.Menu;

public class Driver {
	static Scanner scan = new Scanner(System.in);
	static AccountDaoImpl adl = new AccountDaoImpl();
	static UserDaoImpl udl = new UserDaoImpl();
	static Menu allMenus = new Menu();
	static UserAccountDaoImpl uadl = new UserAccountDaoImpl();
	static ArrayList<User> userList = new ArrayList<User>();
	static ArrayList<Account> accountList = new ArrayList<Account>();
	static Dashboard dashboard = new Dashboard();
	static final Logger log = Logger.getRootLogger();
	
	public static void main(String[] args) {
		landingBanner();
	}
	
	public static void landingBanner() {
		allMenus.mainMenuBanner();
		landing();
	}
	
	public static void landing() {
		allMenus.mainMenu();
		String command = scan.nextLine();
		switch (command) {
		case "1":
			login();
			break;
		case "2":
			register();
			break;
		case "3":
			employeeRegister();
			break;
		case "4":
			adminRegister();
			break;
		default:
			System.out.println("Yarr, we don't know that one.");
			landing();
		}
	}
	
	public static void register() {
		System.out.println("If ye want to join the crew, ye need a pirate name and a secret phrase.");
		System.out.println("So's we can tell that ye are who ye say ye are.");
		System.out.println("Yer Pirate Name: ");
		String username = scan.nextLine();
		System.out.println("Yer Secret Phrase: ");
		String password = scan.nextLine();
		if (!(username.isEmpty() | password.isEmpty())) {
			if (udl.retrieveUserByUsername(username).getUserId() == 0) {
				udl.createUser(new User(username, password, 0));
				log.info("Created user " + username);
				System.out.println("Alright, matey, yer part o' the crew now.");
				System.out.println("Welcome aboard, ye landlubber.");
				landing();
			} else {
				System.out.println("Arr, we already got a feller by that name.");
				register();
			}
		} else {
			System.out.println("Arr, ye got to have a pirate name AND a secret phrase.");
			register();
		}
	}
	public static void employeeRegister() {
		System.out.println("So, ye want ta be a lieutenant, do ye? Ye got moxie, ye do.");
		System.out.println("Yer Pirate Name: ");
		String username = scan.nextLine();
		System.out.println("Yer Secret Phrase: ");
		String password = scan.nextLine();
		if (!(username.isEmpty() | password.isEmpty())) {
			System.out.println(udl.retrieveUserByUsername(username).toString());
			if (udl.retrieveUserByUsername(username).getUserId() == 0) {
				udl.createUser(new User(username, password, 1));
				log.info("Created user " + username);
				System.out.println("Arr, that oughta do.");
				System.out.println("Look alive, crew! We got a new lieutenant, we do!");
				landing();
			} else {
				System.out.println("Arr, we already got a feller by that name.");
				employeeRegister();
			}
		} else {
			System.out.println("Arr, ye got to have a pirate name AND a secret phrase.");
			employeeRegister();
		}
	}
	
	public static void adminRegister() {
		System.out.println("So, ye want to be the captain, do ye?");
		System.out.println("Yer Pirate Name: ");
		String username = scan.nextLine();
		System.out.println("Yer Secret Phrase: ");
		String password = scan.nextLine();
		if (!(username.isEmpty() | password.isEmpty())) {
			if (udl.retrieveUserByUsername(username).getUserId() == 0) {
				udl.createUser(new User(username, password, 2));
				log.info("Created user " + username);
				System.out.println("Look alive, crew, we got a new captain aboard!");
				landing();
			} else {
				System.out.println("Arr, we already got a feller by that name.");
				adminRegister();
			}
		} else {
			System.out.println("Arr, ye need a pirate name AND a secret phrase.");
			adminRegister();
		}
	}

	public static void login() {
		System.out.println(" +-+-+-+-+-+\r\n" + 
				" |l|o|g|i|n|\r\n" + 
				" +-+-+-+-+-+");
		System.out.println("Before ye come aboard, ye need ta give me yer pirate name and yer secret phrase.");
		System.out.println("What's yer pirate name?");
		String username = scan.nextLine();
		System.out.println("Aye, and what's yer secret phrase?");
		String password = scan.nextLine();
		if (!(username.isEmpty() | password.isEmpty())) {
			User confirmUser = udl.retrieveUserByUsername(username);
			if (confirmUser.getUserId() != 0) {
				if (confirmUser.getPassword().equals(password)) {
					System.out.println("That checks out, it does. Welcome aboard.");
					dashboard.welcome(confirmUser);
				} else {
					System.out.println("Arr, the name be right, but ye got the wrong phrase.");
					login();
				}
			} else {
				System.out.println("I don't think we have a feller by that name.");
				System.out.println("Mayhaps ye need to join the crew?");
				landingBanner();
			}
		} else {
			System.out.println("Come on, lad, don't just stand there slackjawed as a fish! I need yer name AND yer phrase!");
			login();
		}
	}

}
