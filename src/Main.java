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
		
		/*INITIALIZING RESPONSE VARIABLES*/
		String input = null;
		Response last = null, next = null;
		
		/*STUFF FOR READING CONSOLE INPUT*/
		InputStreamReader sreader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(sreader);
		
		/*STUFF FOR INITIALIZING MEMORY AND HISTORY*/
		int brainSize = 1000;
		dictionary = new ResponseList(brainSize);
		memory = new ResponseMatrix(brainSize);
		training = new Conversation("", true);
		training.readFile("data.txt");
		discussion = new Conversation("", false);
		last = dictionary.getResponseAt(0);
					
		/*START THE CONVERSATION*/
		System.out.println(last.toString());		
		while(true){ System.out.print(": ");
					
			input = reader.readLine();
			
			/*DEBUGGING OPTIONS*/
			if(input.equalsIgnoreCase("printm")){ 
				memory.print();
				continue;
			}
			if(input.equalsIgnoreCase("printl")){ 
				dictionary.print();
				continue;
			}
			if(input.equalsIgnoreCase("printr")){ 
				responseFlag ^= true;
				continue;
			}
			if(input.equalsIgnoreCase("printc")){ 
				printChoice ^= true;
				continue;
			}
			if(input.equalsIgnoreCase("printd")){ 
				discussion.print();
				continue;
			}				
			if(input.equalsIgnoreCase("printall")){ 
				fullDebug ^= true;
				continue;
			}	
			if(input.equalsIgnoreCase("exit")){
				
				training.writeFile("data.txt");
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
				System.out.println(next.toString());
				
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}
				
				if(printChoice == true) memory.printC();
			}
		}
		
		sreader.close();
		reader.close();
	}
}
