import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	public static ResponseList dictionary;
	public static ResponseMatrix memory;
	public static Conversation discussion;
		
	public static void main(String[] args) throws Exception {
		
		boolean responseFlag = false;
		boolean printChoice = false;
		
		/*INITIALIZING RESPONSE VARIABLES*/
		String input = null;
		Response last, next = null;
		
		/*STUFF FOR READING CONSOLE INPUT*/
		InputStreamReader sreader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(sreader);
		
		/*STUFF FOR INITIALIZING MEMORY AND HISTORY*/
		int brainSize = 1000;
		dictionary = new ResponseList(brainSize);
		memory = new ResponseMatrix(brainSize);	
		discussion = new Conversation("");
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
			if(input.equalsIgnoreCase("exit")) break;
			
			/*TRAINING MODE*/
			if(responseFlag == false){
				
				// add a new Response, based on the last Response and the input
				next = new Response(last, input);
				//discussion.addNext(next);
				last = next;					// that response is the new stimulus
			}
			
			/*CONVERSATION MODE*/ // is it doing anything with input?
			if(responseFlag == true){
				
				//next = new Response(last, input);
				
				// get a new Response based on the last Response
//				next = memory.getNext(last);	// AI ALGORITHM!!
//				
//				discussion.addNext(next);
//				System.out.println(next.toString());
//				last = next;
//				next = new Response(last, input);
//				discussion.addNext(next);
				
				discussion.addNext(input);
				next = new Response(last, input);
				last = next;
				next = memory.getNext(last);
				System.out.println(next.toString());
				discussion.addNext(next);
				last = next;
				
				if(printChoice == true) memory.printC();
			}
		}
		
		sreader.close();
		reader.close();
	}
}
