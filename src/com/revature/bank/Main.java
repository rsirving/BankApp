package com.revature.bank;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class Main {
	static Scanner scan = new Scanner(System.in);
	static final Logger log = Logger.getRootLogger();
	public static Warehouse warehouse = new Warehouse();
	public static User user = null;
	static HashMap<String, User> users = new HashMap<String, User>();
	static HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	public static void main(String[] args) {
		System.out.println("Welcome to your banking app!");
		warehouse = readWare();
		user = null;
		users = warehouse.getUsers();
		accounts = warehouse.getAccounts();
		menu();
	}
	
	public static void menu() {
		System.out.println("Type 'help' for a list of commands.");
		String command = scan.nextLine();
		switch (command) {
		case "register":
			register();	
			break;
		case "login": case "log in":
			login();
			break;
		case "help":
			help();
			break;
		default:
			System.out.println("Unrecognized command.");
			menu();
		}	
	}
	
	public static void dashHelp() {
		System.out.println("Available commands: ");
		System.out.println("    >logout - log out of the system, return to the front.");
		System.out.println("    >view my accounts - view a list of the accounts you own.");
		System.out.println("    >apply - apply for a new account.");
		System.out.println("    >joint apply - apply for a joint account with another user.");
		if (user.getPermissionLevel() > 0) {
			System.out.println("    >approve accounts - view all account applications.");
			System.out.println("    >view accounts - view all active, approved accounts.");
			System.out.println("    >view all users - view all users.");
		}
		if (user.getPermissionLevel() > 1) {
			System.out.println("    >delete accounts - delete approved accounts.");
		}
		dashScan();
	}
	
	public static void help() {
		System.out.println("Available commands:");
		System.out.println("    login - log in with your user credentials.");
		System.out.println("    register - register an account.");
		menu();
	}
	
	public static void login() {
		System.out.println("Username: ");
		String username = scan.nextLine();
		System.out.println("Password: ");
		String password = scan.nextLine();
		if (users.containsKey(username)) {
			if (users.get(username).password.equals(password)) {
				user = users.get(username);
				dashboard();
			} else {
				System.out.println("Incorrect password.");
				login();
			} 
		} else {
			System.out.println("Username not found.");
			login();
		}
	}
	
	public static void register() {
		System.out.println("Thank you for choosing our banking app for your fake banking needs.");
		System.out.print("Please enter your username: ");
		String username = scan.nextLine();
		System.out.print("Please enter your password: ");
		String password = scan.nextLine();		
		if (!(username.isEmpty() | password.isEmpty())) {
			if (users.containsKey(username)) {		// Checks that the username isn't in use.
				System.out.println("Username is in use. Please try again.");
				register();
			} else {
				warehouse.newUser(username, password);	// Creates the new user in the warehouse...
				writeWare(warehouse);					// ... and then saves it to the the .dat file.
				log.info(username + " has created a new account.");	// Added to log file.
				System.out.println("Your account has been created. Please log in.");
				menu();									// And back to the main menu.
			}
		} else {
			log.warn("Username and password are required. Try again.");
			register();
		}
	}
	
	public static void dashboard() {
		// Greets the client when they successfully log in.";
		System.out.println("You have successfully logged in, " + user.getUsername());
		System.out.print("You currently have ");
		switch (user.getPermissionLevel()) {
			case 0:
				System.out.print("USER");
				break;
			case 1:
				System.out.print("EMPLOYEE");
				break;
			case 2:
				System.out.print("ADMIN");
				break;
			default:
				break;
		}
		System.out.print(" privileges.");
		System.out.println("\nFor a list of available commands, type 'help'.");
		dashScan();
	}
	
	public static void dashScan() {
		System.out.println("Enter your command, " + user.getUsername());
		String command = scan.nextLine();
		switch (command) {
		case "logout":
			logout();
			break;
		case "apply":
			accountApply();
			break;
		case "joint apply":
			jointAccountApply();
			break;
		case "deposit":
			deposit();
			break;
		case "withdraw":
			withdraw();
			break;
		case "transfer":
			transfer();
			break;
		case "approve accounts":
			if (user.permissionLevel > 0) {
				accountApprove();
			} else {
				System.out.println("Insufficient permission level.");
				dashScan();
			} 
			break;
		case "view users":
			if (user.permissionLevel > 0) {
				viewUsers();
			} else {
				System.out.println("Insufficient permission level.");
				dashScan();
			}
			break;
		case "view accounts":
			if (user.permissionLevel > 0) {
				viewAccounts();
			} else {
				System.out.println("Insufficient permission level.");
				dashScan();
			}
			break;
		case "delete account":
			if (user.permissionLevel > 1) {
				deleteAccount();
			} else {
				System.out.println("Insufficient permission level.");
				dashScan();
			}
		case "view my accounts":
			viewOwnedAccounts();
			break;
		case "help":
			dashHelp();
			break;
		default:
			System.out.println("Unrecognized command.");
			dashScan();
		}
		
	}
	
	private static void deleteAccount() {
		System.out.println("Enter the ID of the account to be deleted.");
		System.out.println("Enter 'quit' to return to dashboard.");
		String id = scan.nextLine();
		if (id.equals("quit")) {
			dashboard();
		}
		int accountId = -1;
		try {
			accountId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			log.error("ID is not valid.", e);
			System.out.println("Enter only a number. No letters or anything.");
			deleteAccount();
		} finally {
			Account a = warehouse.fetchAccount(accountId);
			if (!a.equals(null)) {
				for (String owner : a.getOwners()) {
					warehouse.removeAccountFromUser(owner, accountId);
				}
				warehouse.removeAccount(accountId);
				writeWare(warehouse);
			} else {
				log.error("Invalid account.");
				System.out.println("No account with specified ID exists. Try again.");
			}
		}
	}
	
	private static void accountApprove() {
		System.out.println("Account applications awaiting approval: ");
		for (Account a : accounts.values()) {
			if (a.approval == 0) {
				System.out.print("\nAccount #: " + a.getAccountId() + ", Owner(s): ");
				for (String o : a.getOwners()) {
					System.out.print(o + " ");
				}
				System.out.println("\nApprove? (y/n)");
				int judgement = 0;
				String approval = scan.nextLine();
				switch (approval) {			// This covers (most) possible responses to this question. 
					case "y": case "Y": case "yes": case "Yes":
						judgement = 1;
						break;
					case "n": case "N": case "no": case "No":
						judgement = 2;
						break;
					default: 
						System.out.println("Invalid response.");
						accountApprove();
				}
				warehouse.approveAccount(judgement, a.getAccountId());
				if (judgement == 1) {
					log.info("Account #" + a.getAccountId() + " was approved by " + user.username + ".");					
				} else {
					log.info("Account #" + a.getAccountId() + " was declined by " + user.username + ".");					
				}
				writeWare(warehouse);
			}
		}
		System.out.println("That's all!");
		dashScan();
	}

	private static void viewUsers() {
		// For employees and admins. Displays all users and their accounts.
		HashMap<String, User> users = warehouse.getUsers();
		// TODO format this better, and show permission levels as well.
		for (User u : users.values()) {
			System.out.print("\nUser: " + u.username + ", Owned Accounts: ");
			for (Account a : u.ownedAccounts) {
				System.out.print(a.getAccountId() + ", ");
			}
		}
		System.out.println("\n");
		dashScan();
	}

	private static void viewOwnedAccounts() {
		// Shows all accounts owned by the current user.
		for (Account a : user.ownedAccounts) {
			if (a.approval == 1) {
				System.out.print("\nAccount #: " + a.getAccountId() + " Balance: $" + a.balance + " Owner(s): ");
				for (String owner : a.getOwners()) {
					System.out.print(owner + " ");
				}
			}
			System.out.println("");
		}
		dashScan();
	}

	private static void viewAccounts() {
		// Shows all accounts, with owner(s) and balance.
		try {
			for (Account a : accounts.values()) {
				if (a.approval == 1) {
					System.out.print("\nAccount #: " + a.getAccountId() + " Balance: $" + a.balance + " Owner(s): " );
					for (String owner : a.getOwners()) {
						System.out.print(owner + " ");
					}
					System.out.println("");
				}
			}
			dashScan();			
		} catch (NullPointerException e) {
			System.out.println("There are no active, approved accounts.");
			dashScan();
		}
	}

	public static void accountApply() {
		// Applies for a single account.
		warehouse.accountApply(user.getUsername());
		log.info(user.username + " has applied for an account.");
		writeWare(warehouse);
		System.out.println("You have applied for an account with this banking app.");
		System.out.println("Check back later.");
		dashScan();
	}
	
	public static void jointAccountApply() {
		// Applies for a joint account.
		// TODO Implement joint accounts with more than two members.
		System.out.println("You are attempting to apply for a joint account with another user.");
		System.out.println("Please enter your partner's username.");		// There has got to be a better way to put this.
		String partner = scan.nextLine();
		if (!partner.equals(user.username)) {
			if (users.containsKey(partner)) {
				warehouse.jointAccountApply(new String[] {user.username, partner});
				log.info(user.username + " and " + partner + " applied for a joint account.");
				writeWare(warehouse);
				System.out.println("You have applied for a joint account with " + partner + ".");
				System.out.println("An employee will review your application.");
				dashScan();
			} else {
				System.out.println("Specified user does not exist.");
				jointAccountApply();
			}
		} else {
			System.out.println("You can't make a joint account with yourself.");
			jointAccountApply();
		}
	}
	
	public static void logout() {
		// Speaks for itself. Removes the current user from the session and returns to the menu.
		System.out.println("Logging you out.");
		user = null;
		menu();
	}
	
	public static boolean verifyAcctForTransaction(Account a, User u) {
		// Given an account and a user, confirms that the user owns the account and the account is approved.
		// If the user has employee permissions or higher, they own every account.
		// This is a very bad bank.
		if (a.getApproval() == 1) {
			if (u.getPermissionLevel() > 0) {
				return true;
			}
			if (u.ownedAccounts.contains(a)) {
				return true;
			}			
		}
		return false;
	}
	
	public static void deposit() {
		System.out.println("You are attempting to make a deposit.");
		System.out.println("Please enter the ID number of the account to deposit to.");
		int accountId;
		try {
			accountId = Integer.parseInt(scan.nextLine());
			Account a = warehouse.fetchAccount(accountId);
			if (verifyAcctForTransaction(a, user)) {
				System.out.println("Enter the amount to deposit.");
				try {
					int amount = Integer.parseInt(scan.nextLine());
					if (amount > 0) {
						// TODO Implement verification, allowing client to confirm the transaction before it goes through.
						warehouse.deposit(amount, accountId);
						log.info(user.username + " deposited $" + amount + " to account #" + accountId + ".");
						System.out.println("Deposit successful. Account balance is now $" + a.balance);
						writeWare(warehouse);
						dashScan();
					} else {
						System.out.println("You can't deposit negative money.");
						deposit();
					}
				} catch (NumberFormatException e) {
					System.out.println("Deposit amount invalid.");
					deposit();
				}
			} else {
				System.out.println("Invalid account. Either the account doesn't exist or hasn't been approved, "
						+ "you don't own it, or you don't have the necessary permissions.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter only a number.");
			deposit();
		}
	}
	
	public static void withdraw() {
		System.out.println("You are attempting to make a withdrawal.");
		System.out.println("Please enter the ID number of the account to withdraw from.");
		int accountId;
		try {
			accountId = Integer.parseInt(scan.nextLine());
			Account a = warehouse.fetchAccount(accountId);
			if (verifyAcctForTransaction(a, user)) {
				System.out.println("Enter the amount to withdraw.");
				try {
					int amount = Integer.parseInt(scan.nextLine());
					if (amount > 0) {
						if (amount > a.balance) {
							System.out.println("You are attempting to overdraw from your account. You tried to withdraw $" +
									amount + ", and your current balance is $" + a.balance + ".");
							withdraw();		// Do not pass go. Do not collect $200.
						} else {
							// Implement verification process.
							warehouse.withdraw(amount, accountId);
							log.info(user.username + " withdrew $" + amount + " from account #" + accountId + ".");
							System.out.println("Withdrawal successful. Account balance is now $" + a.balance);
							writeWare(warehouse);
							dashScan();							
						}
					} else {
						System.out.println("You can't withdraw that.");
						withdraw();
					}
				} catch (NumberFormatException e) {
					System.out.println("Withdrawal amount invalid.");
					withdraw();
				}
			} else {
				System.out.println("Invalid account. Either the account doesn't exist or hasn't been approved, "
						+ "you don't own it, or you don't have the necessary permissions.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter only a number.");
			withdraw();
		}
	}
	
	public static void transfer() {
		System.out.println("You are attempting to transfer funds from one account to another.");
		System.out.println("Please enter the ID of the account to transfer FROM.");
		int accountId;
		try {
			accountId = Integer.parseInt(scan.nextLine());
			Account a = warehouse.fetchAccount(accountId);
			if (verifyAcctForTransaction(a, user)) {
				System.out.println("Please enter the ID of the account to transfer TO.");
				int tAccountId;
				try {
					tAccountId = Integer.parseInt(scan.nextLine());
					if (tAccountId == accountId) {
						System.out.println("You cannot transfer to and from the same account.");
						transfer();
					} else {
						Account tA = warehouse.fetchAccount(tAccountId);		
						if (verifyAcctForTransaction(tA, user)){
							System.out.println("Please enter the amount to transfer.");
							int amount = Integer.parseInt(scan.nextLine());
							try {
								if (amount > a.balance) {
									System.out.println("You are attempting to overdraw from account #" + a.getAccountId());
									System.out.println("Account balance is $" + a.balance);
									transfer();
								} else {
									// TODO implement a verification process, allowing client to confirm the transaction.
									warehouse.withdraw(amount, accountId);
									warehouse.deposit(amount, tAccountId);
									log.info(user.username + " has transferred $" + amount + " from account #" + 
											accountId + " to account #" + tAccountId + ".");
									System.out.println("Transfer was successful.");
									dashboard();
								}
							} catch (NumberFormatException e) {
								System.out.println("Please enter only a number.");
								transfer();
							}
						} else {
							System.out.println("You do not have permission to transfer to this account.");
							transfer();
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Please enter only a number.");
					transfer();
				}
			} else {
				System.out.println("You do not have permission to transfer from this account.");
				transfer();
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter only a number.");
			transfer();
		}
	}

	// Warehouse serialization functions.
	
	public static Warehouse readWare() {
		// Pulls the serialized warehouse object from the warehouse.dat file.
		Warehouse warehouse = new Warehouse();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("warehouse.dat"))){
			warehouse = (Warehouse) ois.readObject();
		} catch (FileNotFoundException e) {
			// If there isn't a warehouse.dat file, create one and populate it a little bit.
			warehouse.newAdmin("admin", "password");
			warehouse.newEmployee("employee1", "password");
			warehouse.newEmployee("employee2", "password");
			warehouse.newUser("user1", "password");
			warehouse.newUser("user2", "password");
			warehouse.newUser("user3", "password");
			warehouse.accountApply("user1");
			warehouse.accountApply("user2");
			warehouse.accountApply("user3");
			warehouse.accountApply("employee1");
			warehouse.accountApply("employee2");
			warehouse.accountApply("admin");
			writeWare(warehouse);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return warehouse;
	}
	
	public static void writeWare(Warehouse warehouse) {
		// Writes the extant warehouse object to the warehouse.dat file.
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("warehouse.dat"))){
			oos.writeObject(warehouse);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}