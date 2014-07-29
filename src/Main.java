import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {
	
	public static ResponseList dictionary;
	public static ResponseMatrix memory;
	public static Conversation training;
	public static Conversation discussion;
	
	static Boolean trainingToggle = false;		
	
	public static void main(String[] args) throws Exception {
		
		JFrame mainWindow;
		
		
		boolean printChoice = false;
		boolean fullDebug = false;
		
		/* INITIALIZING RESPONSE VARIABLES */
		String input = null, convoName = null;
		Response last = null, next = null;
		
		/* STUFF FOR INITIALIZING MEMORY AND HISTORY */
		int brainSize = 1000;
		dictionary = new ResponseList(brainSize);
		memory = new ResponseMatrix(brainSize);
		discussion = new Conversation("");
		
		/* RETRIEVE PREVIOUS LIST AND MATRIX AND IMPORT */
		dictionary.readFile("list.data");
		memory.readFile("matrix.data");
					
		/* STUFF FOR READING CONSOLE INPUT */
		InputStreamReader sreader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(sreader);
					
		/* START THE CONVERSATION */
		last = dictionary.getResponseAt(0);
		System.out.println(last.toString());		
		while(true){ System.out.print("USER->\t");
					
			input = reader.readLine();
			
			/*DEBUGGING OPTIONS*/
			if(input.equalsIgnoreCase("printm")){ memory.print(); continue;}
			if(input.equalsIgnoreCase("printl")){ dictionary.print(); continue;}
			if(input.equalsIgnoreCase("printd")){ discussion.print(); continue;}
			
			if(input.equalsIgnoreCase("printr")){ trainingToggle ^= true; continue;}
			if(input.equalsIgnoreCase("printc")){ printChoice ^= true; continue;}
			if(input.equalsIgnoreCase("printall")){ fullDebug ^= true; continue;}	
			
			if(input.equalsIgnoreCase("exit")){ 
				
				//convoName = discussion.writeFile(); 
				System.out.print("Writing conversation: " + convoName + " ... Done.");
				break;
			}
			
			/*TRAINING MODE*/
			if(trainingToggle == false){
				
				next = new Response(last, input, true);
				last = next;
								
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}		
			}
			
			/*CONVERSATION MODE*/ 
			if(trainingToggle == true){
				
				next = new Response(last, input, false);			
				last = next;	
				next = memory.getNext(last);// AI ALGORITHM!!
				last = next;			
				System.out.println("BOT ->\t" + next.toString());
				
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}
				
				if(printChoice == true) memory.printC();
			}
			
			dictionary.writeFile("list.data");
			memory.writeFile("matrix.data");
		}
		
		mainWindow = new JFrame("\tWebAgent\t");
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainWindow.add(new WebAgentUI());
	    mainWindow.pack();
	    mainWindow.setVisible(true);
		
		sreader.close();
		reader.close();
	}
}
