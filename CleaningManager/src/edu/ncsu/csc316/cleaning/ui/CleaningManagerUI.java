package edu.ncsu.csc316.cleaning.ui;

import java.io.FileNotFoundException;

import java.util.Scanner;


import edu.ncsu.csc316.cleaning.manager.ReportManager;

/**
* The user interface used for black box testing, holds the main method
* 
* @author Lalitha Edupuganti
*
*/
public class CleaningManagerUI {

	/**
	* The main method that interacts with the user's commands and 
	* perform the actions requested
	* @param args the arguments provided
	*/
	public static void main (String [] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the Cleaning Manager System!");
		boolean alreadyChecked = false;
		String roomFile = null;
		String cleaningEventFile = null;
		while (true) {
			
		    if(!alreadyChecked) {
				System.out.print("Please enter the Room List File Name: ");
				roomFile = in.next();
	
				System.out.print("Please enter the Cleaning Events Log File Name: ");
				cleaningEventFile = in.next();
		    }
			
			try {
				ReportManager newReportManager = new ReportManager(roomFile, cleaningEventFile);
	
				boolean returnToMainMenu = false;
				
				System.out.println("Enter a number on which action you would like to take: \n"
						+ "   1. View the most frequently cleaned rooms\n   2. View a report of cleanings by room\n"
						+ "   3. View estimated remaining vaccum bag life\n"
						+ "   4. Close the program");
				System.out.print("Task: ");
				
				int action = in.nextInt();
				
				while(!returnToMainMenu) {
					switch(action) {
						case 1:
							System.out.print("Please specify the number of rooms to generate the frequency report: ");
							int count = in.nextInt();
							System.out.println(newReportManager.getFrequencyReport(count));
							returnToMainMenu = true;
							alreadyChecked = true;
							break;
						case 2:
							System.out.println(newReportManager.getRoomReport());
							returnToMainMenu = true;
							alreadyChecked = true;
							break;
						case 3:
							System.out.print("Please enter the date to be checked in format MM/DD/YYYY HH:MM:SS: ");
							String timeStamp = in.next() + " " + in.next();
							System.out.print(newReportManager.getVacuumBagReport(timeStamp) + "\n");  
							returnToMainMenu = true;
							alreadyChecked = true;
							break;
						case 4:
							System.out.println("Program is closed");
							in.close();		
							return;
						default:
							System.out.println("Please choose one of the options provided!");
							returnToMainMenu = true;
							alreadyChecked = true;
							break;
					}
					
					 if (returnToMainMenu) {
	                     break; 
	                 }
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("The specified file is does not exist, please enter a new file.");
			}
			
		}
		
	}

}
