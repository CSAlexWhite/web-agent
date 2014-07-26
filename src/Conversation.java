import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class Conversation {
	
	String convo= null;
	boolean training = false;
	
	public Conversation(String first, boolean training){		
		convo = first.toString();
		this.training = training;
	}
	
	public void addNext(Response next){ 
		convo += "\n" + next.toString(); 
	}
	
	public void addNext(String next){ 
		convo += "\n" + next; 
	}
	
	public void readFile(String filename) throws IOException{
		
		String firstLine = null, nextLine = null;
		
		BufferedReader inFile = new BufferedReader(new FileReader(filename));
		firstLine = inFile.readLine();
		Response next, last = new Response(firstLine);
		
		while(inFile.ready()){
			
			nextLine = inFile.readLine();
			next = new Response(last, nextLine, training);
			last = next;
			addNext(nextLine);			
		}
		
		inFile.close();	
	}
	
	public void writeFile(String filename) throws IOException{
		
		PrintWriter newFile = new PrintWriter(filename);
		
		StringTokenizer output = new StringTokenizer(convo, "\n");
		
		while(output.hasMoreElements()) newFile.println(output.nextToken());
			
		newFile.close();
	}
	
	public void print(){ 
		System.out.println(convo); 
	}

}

/*
 * Project 3b: 2DArrayInput.cpp
 * by Alex White
 * Prepared for Professor Phillips
 * CS 313, Queens College
 * February 13, 2014
 */
/* 
import java.io.*;
import java.util.*;

public class ReadFileArray {

	public static void main(String[] args) throws IOException, NumberFormatException{
		
		String 	filename = "data.txt", 
				firstLine = null, 
				nextLine = null; 
		
		int rows = 0, cols = 0, i=0, j=0;
		
		BufferedReader inFile = new BufferedReader(new FileReader(filename));

		while(inFile.ready()){
						
			try{
			
			firstLine = inFile.readLine();
			StringTokenizer dimensions = new StringTokenizer(firstLine, " ");
			
			if(dimensions.hasMoreTokens()) 
				rows = Integer.parseInt(dimensions.nextToken(" "));
			if(dimensions.hasMoreTokens())
				cols = Integer.parseInt(dimensions.nextToken(" "));
			
			}
			
			catch(IOException error){ 
				
				System.out.println("An error has occurred");				
			}
		}
		
		int[][] numbers = new int[rows][cols];
		for(int k=0; k<rows; k++) numbers[k] = new int[cols];
				
		while(inFile.ready()){
			
			try{
				
				nextLine = inFile.readLine();
				StringTokenizer arrayLine = new StringTokenizer(nextLine," ");
				
				try{
				
					while(arrayLine.hasMoreTokens())
						
						numbers[i][j++]  = Integer.parseInt(arrayLine.nextToken());				
				}
				
				catch(NumberFormatException error){ continue; }					
			}
			
			catch(IOException error){
				
				System.out.println("An error has occurred");
			}
			
			j=0;
			i++;
		}
	
		for(i=0; i<rows; i++){
			for(j=0; j<cols; j++)
				System.out.print(numbers[i][j] + " ");
			System.out.println();
		}
		
	}	
}
*/

