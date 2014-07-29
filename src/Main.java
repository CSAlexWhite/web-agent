import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	public static ResponseList dictionary;
	public static ResponseMatrix memory;
	public static Conversation training;
	public static Conversation discussion;
			
	public static void main(String[] args) throws Exception {
		
		boolean responseFlag = false;
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
			
			if(input.equalsIgnoreCase("printr")){ responseFlag ^= true; continue;}
			if(input.equalsIgnoreCase("printc")){ printChoice ^= true; continue;}
			if(input.equalsIgnoreCase("printall")){ fullDebug ^= true; continue;}	
			
			if(input.equalsIgnoreCase("exit")){ 
				
				convoName = discussion.writeFile(); 
				System.out.print("Writing conversation: " + convoName + " ... Done.");
				break;
			}
			
			/*TRAINING MODE*/
			if(responseFlag == false){
				
				next = new Response(last, input, true);
				//System.out.println(last + " " + next + " " + dictionary.nextEmpty);
				last = next;
								
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}		
			}
			
			/*CONVERSATION MODE*/ 
			if(responseFlag == true){
				
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
		
		sreader.close();
		reader.close();
	}
}
