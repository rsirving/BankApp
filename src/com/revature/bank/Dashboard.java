package com.revature.bank;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.dao.*;
import com.revature.pojo.*;
import com.revature.util.*;
import com.revature.bank.Menu;

public class Dashboard {
	static User currentUser;
	
	static Scanner scan = new Scanner(System.in);
	static AccountDaoImpl adl = new AccountDaoImpl();
	static UserDaoImpl udl = new UserDaoImpl();
	static Menu allMenus = new Menu();
	static UserAccountDaoImpl uadl = new UserAccountDaoImpl();
	static ArrayList<User> userList = new ArrayList<User>();
	static ArrayList<Account> accountList = new ArrayList<Account>();
	static final Logger log = Logger.getRootLogger();
	
	public static void welcome(User user) {
		currentUser = user;
		allMenus.welcome();
		welcomeScan();
	}
	
	public static void welcomeScan() {
		userList = udl.retrieveAllUsers();
		accountList = adl.retrieveAllAccounts();
		allMenus.dashMenu(currentUser.getPermissionLevel());
		String command = scan.nextLine();
		switch (command) {
			case "0":
				logout();
				break;
			case "1":
				viewAccounts();
				break;
			case "2":
				applyAccount();
				break;
			case "3":
				applyJointAccount();
				break;
			case "4":
				withdraw();
				break;
			case "5":
				deposit();
				break;
			case "6":
				transfer();
				break;
			case "7":
				if (currentUser.getPermissionLevel() > 0) {
					approveAccounts();
					break;
				}
			case "8":
				if (currentUser.getPermissionLevel() > 0) {
					viewAllAccounts();
					break;
				}
			case "9":
				if (currentUser.getPermissionLevel() > 0) {
					viewAllUsers();
					break;
				}
			case "x": case "X":
				if (currentUser.getPermissionLevel() > 1) {
					deleteAccount();
					break;
				}
			default:
				System.out.println("Ye'll have ta speak up, laddie. I don't know what that means.");
				welcomeScan();
		}
	}
	
	public static boolean verifyAcctForTransaction(Account a, User u) {
		// Given an account and a user, confirms that the user owns the account and the account is approved.
		// If the user has employee permissions or higher, they own every account.
		if (a.getApproval() == 1) {
			if (u.getPermissionLevel() > 0) {
				return true;
			}
			ArrayList<String> owners = udl.getUsersOwningAccount(a.getAccountId());
			for (String o : owners) {
				if (o.equals(u.getUsername())) {
					return true;
				}
			}
		}
		return false;
	}

	private static void transfer() {
		System.out.println("Ye want ta move yer booty?");
		System.out.println("Quit gigglin'.");
		System.out.println("Where were ye wantin' ta take the gold from?");
		int accountid;
		try {
			accountid = Integer.parseInt(scan.nextLine());
			Account ac = adl.retrieveAccountById(accountid);
			if (verifyAcctForTransaction(ac, currentUser)) {
				System.out.println("How much gold?");
				System.out.println("(That treasure has " + ac.getBalance() + " pieces o' eight)");
				int amount = Integer.parseInt(scan.nextLine());
				if (amount > 0) {
					if (amount <= ac.getBalance()) {
						System.out.println("Where did ye want to move that to?");
						int tAccountid = Integer.parseInt(scan.nextLine());
						Account tAc = adl.retrieveAccountById(tAccountid);
						if (verifyAcctForTransaction(tAc, currentUser)) {
							System.out.println("Aye, let's get goin'.");
							int newBalance = ac.getBalance() - amount;
							ac.setBalance(newBalance);
							adl.updateAccount(ac);
							int tNewBalance = tAc.getBalance() + amount;
							tAc.setBalance(tNewBalance);
							adl.updateAccount(tAc);
							log.info(currentUser.getUsername() + " has moved " + amount + " pieces o' eight from map #" + ac.getAccountId() + " to map #" + tAc.getAccountId());
							System.out.println("Ye've moved yer treasure!");
							welcomeScan();
						} else {
							System.out.println("Ye can't move yer treasure there!");
							transfer();
						}
					} else {
						System.out.println("That's more treasure than ye have buried there!");
						transfer();
					}
				} else {
					System.out.println("How were ye plannin' to dig up that much?");
					transfer();
				}
			} else {
				System.out.println("Ye can't get treasure from there!");
				transfer();
			}
		} catch (NumberFormatException e) {
			System.out.println("Just numbers, mate.");
			transfer();
		}
	}

	private static void withdraw() {
		System.out.println("Ye want to dig up yer treasure? Good on ye.");
		System.out.println("Which map were ye gonna use?");
		int accountid;
		try {
			accountid = Integer.parseInt(scan.nextLine());
			Account ac = adl.retrieveAccountById(accountid);
			if (ac.getAccountId() == 0) {
				System.out.println("We ain't got a map with that number.");
				withdraw();
			} else {
				if (verifyAcctForTransaction(ac, currentUser)) {
					System.out.println("Let me grab me trusty shovel.");
					System.out.println("How much did ye want to dig up?");
					System.out.println("(Yer current balance is " + ac.getBalance() + " pieces o' eight)");
					int amount = Integer.parseInt(scan.nextLine());
					if (amount > 0) {
						if (amount <= ac.getBalance()) {
							System.out.println("Aye, let's get ta diggin'.");
							int newBalance = ac.getBalance() - amount;
							System.out.println("Ye'll have " + newBalance + " pieces o' eight buried now.");
							ac.setBalance(newBalance);
							adl.updateAccount(ac);
							log.info(currentUser.getUsername() + " has dug up " + amount + " pieces o' eight in treasure #" + ac.getAccountId());
							welcomeScan();
						} else {
							System.out.println("Arr, that's more gold than ye got in here!");
							withdraw();
						}
					} else {
						System.out.println("How were ye going to dig that up?");
						withdraw();
					}	
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("Only use numbers, lad.");
			withdraw();
		}
	}

	private static void deposit() {
		System.out.println("Aye, t'ain't nothing like buryin' yer treasure in the sand.");
		System.out.println("Which o' yer maps do ye want t' use?");
		int accountid; 
		try {
			accountid = Integer.parseInt(scan.nextLine());
			Account ac = adl.retrieveAccountById(accountid);
			if (ac.getAccountId() == 0) {
				System.out.println("There ain't no map like that!");
				deposit();
			} else {
				if (verifyAcctForTransaction(ac, currentUser)) {
					System.out.println("Aye, and how many pieces o' eight will ye be burying?");
						int amount = Integer.parseInt(scan.nextLine());
						if (amount > 0) {
							System.out.println("Aye, we'll get that socked away.");
							int newBalance = ac.getBalance() + amount;
							System.out.println("Ye'll have " + newBalance + " pieces o' eight buried now.");
							ac.setBalance(newBalance);
							adl.updateAccount(ac);
							log.info(currentUser.getUsername() + " has buried " + amount + " pieces o' eight in treasure #" + ac.getAccountId());
							welcomeScan();
						} else {
							System.out.println("How were ye plannin' on buryin' that?");
							deposit();
						}
				} else {
					System.out.println("That ain't yer map, matey.");
				System.out.println("Pirates don't steal. From each other. Usually.");
				deposit();
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("Only use numbers, lad.");
			deposit();
		}
		
	}

	private static void deleteAccount() {
		// TODO Auto-generated method stub
	}

	private static void viewAllUsers() {
		System.out.println("Here be all the scallywags in our happy lil' family.\n");
		ArrayList<Account> ownedAccounts = new ArrayList<Account>();
		for (User user : userList) {
			System.out.println(user.toStringNoPsw());
			ownedAccounts = adl.retrieveAccountsByUserId(user.getUserId());
			System.out.println("Treasure Maps:");
			for (Account ac : ownedAccounts) {
				System.out.println(ac.toString());
			}
			System.out.println("~~~---~~~---~~~---~~~---~~~---~~~---~~~---");
		}
		welcomeScan();
	}

	private static void viewAllAccounts() {
		System.out.println("Here be all the treasures buried by the crew.\n");
		accountList = adl.retrieveAllAccounts();
		for (Account ac : accountList) {
			if (ac.getApproval() == 1) {
				System.out.println(ac.toString());
				System.out.println("~~~---~~~---~~~---~~~---~~~---~~~---~~~---");				
			}
		}
		welcomeScan();
	}

	private static void approveAccounts() {
		System.out.println("Here's the crewmembers who want somewhere t' bury their treasure.");
		ArrayList<String> owners = new ArrayList<String>();
		accountList = adl.retrieveAllAccounts();
		for (Account ac : accountList) {
			System.out.println(ac.getApproval());
			if (ac.getApproval() == 0) {
				System.out.println(ac.toString());
				owners = udl.getUsersOwningAccount(ac.getAccountId());
				System.out.println("Owner(s): ");
				for (String owner : owners) {
					System.out.println("> " + owner);
				}
				System.out.println("Do ye want to approve it? (y/n)");
				String answer = scan.nextLine();
				switch (answer) {
					case "y": case "Y": case "Yes": case "yes": case "Aye": case "aye":
						ac.setApproval(1);
						log.info(currentUser.getUsername() + " has approved treasure map #" + ac.getAccountId());
						break;
					case "n": case "N": case "No": case "no": case "Nay": case "nay":
						ac.setApproval(2);
						log.info(currentUser.getUsername() + " has denied treasure map #" + ac.getAccountId());
						break;
					default:
						System.out.println("Don't get smart, ye landlubber.");
						approveAccounts();
						break;
				}
				adl.updateAccount(ac);
				System.out.println("Alright, here's the next one: ");
			}
		} 
		System.out.println("That's the lot o' them.");
		welcomeScan();
	}

	private static void applyJointAccount() {
		System.out.println("So, ye want ta bury yer treasure with another pirate?");
		System.out.println("It be your choice, but don't come wailin' ta me when yer 'partner' robs ye blind.");
		System.out.println("What's the name o' the bloke ye want ta 'trust'?");
		String username = scan.nextLine();
		User partner = udl.retrieveUserByUsername(username);
		if (partner.getUsername() == null) {
			System.out.println(username + "? Don't think we have anyone by that name.");
		} else if (username.equals(currentUser.getUsername())) {
			adl.createAccount(new Account(0, 0), currentUser);
//			uadl.linkUserToNewAccount(currentUser);
			log.info(currentUser.getUsername() + " has applied to bury treasure with, uh, " + currentUser.getUsername() + "."
					+ " I'm not going to ask why.");
			System.out.println("Yarr, mate, ye can't - why would ye want to share yer map with yerself? It don't make a lick of -");
			System.out.println("- Arr, forget it. I'll let the cap'n or a lieutenant know.");
			System.out.println("Jus' get outta here, ya grog-addled halfwit.");
			welcomeScan();
		} else {
			adl.createAccount(new Account(0, 0), currentUser);
			uadl.linkUserToNewAccount(partner);
			log.info(currentUser.getUsername() + " has applied to bury treasure with " + partner.getUsername() + ".");
			System.out.println(partner.getUsername() + "? Yer sure? Alright, 's yer funeral.");
			System.out.println("I'll let the cap'n or a lieutenant know. They'll get a good laugh outta this...");
			welcomeScan();
		}
	}

	private static void applyAccount() {
		adl.createAccount(new Account(0, 0), currentUser);
		log.info(currentUser.getUsername() + "requested a place to bury treasure.");
		System.out.println("Aye, ye want a place ta bury yer treasure?");
		System.out.println("I'll let the captain or a lieutenant know.");
		System.out.println("They'll decide whether or not yer worthy.");
		System.out.println("Check back later.");
		welcomeScan();
	}

	private static void viewAccounts() {
		System.out.println("Here be all the treasures ye have buried.\n");
		ArrayList<Account> ownedAccounts = adl.retrieveAccountsByUserId(currentUser.getUserId());
		for (Account ac : ownedAccounts) {
			if (ac.getApproval() == 1) {
				System.out.println(ac.toString());
				System.out.println("~~~---~~~---~~~---~~~---~~~---~~~---~~~---\n");
			}
		}
		System.out.println("That's the lot o' them.");
		welcomeScan();
	}

	private static void logout() {
		System.out.println("Ah, be a shame ta see ye go.");
		System.out.println("Come back soon, ye scurvy dog.");
		currentUser = null;
		// TODO Implement.
	}
}
