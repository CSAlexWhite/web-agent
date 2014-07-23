import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	public static ResponseList dictionary;
	
	public static void main(String[] args) throws Exception {
		Response next, current, last;
		
		String greeting = "Hello";
		last = new Response(greeting);
		
		String input = null;
		
		int brainSize = 100;
		
		ResponseMatrix matrix = new ResponseMatrix(brainSize);
		dictionary = new ResponseList(brainSize, last);
			
		System.out.println(greeting);
		last = new Response(greeting);
		
		/**
		 * Training Conversation
		 */
		while(input != "exit"){
		
//			input = getInput();					// get a new response from user
//			current = new Response(input);		// add it to the graph
//			last.addNext(current);				// add it as a response to the
//												// last Response
//			last = current;						// make the current response the
//												// last response	
			
			input = getInput();
			
			// search ResponseList to find if input exists already
			
			
			
		}
		
		/**
		 * Test Conversation for (correct?) retrieval
		 */
		System.out.println(greeting);
		while(input != "exit"){
			
			input = getInput();
			next = dictionary.findResponse(input);
			System.out.println(next.chooseNext());			
		}
		
	}
	
	public static void Train(){
		
		
	}
	
	public static String getInput(){
		
		String toReturn;
		Scanner input = new Scanner(System.in);
		
		toReturn = input.next();
		input.close();
		
		return toReturn;
	}
}
