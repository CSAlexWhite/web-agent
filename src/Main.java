import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	public static ResponseList dictionary;
	public static ResponseMatrix memory;
	public static Scanner inputScanner;
	
	public static void main(String[] args) throws Exception {
		
		int brainSize = 10;
		String input = null, greeting = "Hello";		
		Response current, next;

		current = new Response(greeting);
						 
		dictionary = new ResponseList(brainSize, current);
		memory = new ResponseMatrix(brainSize);	
			
		System.out.println(greeting);		
		inputScanner = new Scanner(System.in);
		while(input != "exit"){ System.out.print(": ");
					
			input = inputScanner.next();
			next = new Response(current, input);
			current = next;
			System.out.println(memory.getNext(next));	
			memory.print();
		}				
	}
}
