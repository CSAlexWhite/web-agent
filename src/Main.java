import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		String inputString = null;
		
		while(inputString != "exit"){
		
			//inputString = getInput();	
			
			SQLConnect.connect();	
		}	
		
	}
	
	public static String getInput(){
		
		String toReturn;
		Scanner input = new Scanner(System.in);
		
		toReturn = input.next();
		input.close();
		
		return toReturn;
	}
}
