package com.revature.bank;

import java.util.Scanner;

public class Menu {
		
	public static void mainMenuBanner() {
		// sysout ascii art
		System.out.println(" __        __  ___                       __   __   __                __             __   ___ \r\n" + 
				"/  `  /\\  |__)  |   /\\  | |\\ |     |\\/| /  \\ |__) / _`  /\\  |\\ |    /  ` |__|  /\\  /__` |__  \r\n" + 
				"\\__, /~~\\ |     |  /~~\\ | | \\|     |  | \\__/ |  \\ \\__> /~~\\ | \\|    \\__, |  | /~~\\ .__/ |___ ");
		System.out.println("Yo ho, yo ho, a pirate's bank for me...");
	}
	
	public static void mainMenu() {
		System.out.println("+--+--------------+");
		System.out.println("|# | what ye want |");
		System.out.println("|--+--------------|");
		System.out.println("|1 | get on board |");
		System.out.println("|2 |join the crew |");
		System.out.println("+--+--------------+");
	}
	
	public static void dashMenu(int permissionLevel) {
		System.out.println("+--+-------------------+");
		System.out.println("|# | what ye want      |");
		System.out.println("|--+-------------------|");
		System.out.println("|0 | walk the plank    |");
		System.out.println("|1 | view yer treasure |");
		System.out.println("|2 | ask fer a map     |");
		System.out.println("|3 | pool yer treasure |");
		System.out.println("|4 | dig up treasure   |");
		System.out.println("|5 | bury treasure     |");
		System.out.println("|6 | move yer booty    |");
		if (permissionLevel > 0) {
			System.out.println("|7 | grant maps        |");
			System.out.println("|8 | check all treasure|");
			System.out.println("|9 | check all pirates |");
		}
		if (permissionLevel > 1) {
			System.out.println("|x | destroy treasure  |");			
		}
		System.out.println("+--+-------------------+");
	}
	
	public static void welcome() {
		System.out.println("\r\n" + 
				".  .   .              \r\n" + 
				"|  | _ | _. _ ._ _  _ \r\n" + 
				"|/\\|(/,|(_.(_)[ | )(/,\r\n" + 
				"                      \r\n" + 
				"");
	}
}
