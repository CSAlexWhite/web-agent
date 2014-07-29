import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class Conversation {
	
	String convo= null;
	boolean training = false;
	
	public Conversation(String first){		
		convo = first.toString();
	}
	
	public void addNext(Response next){ 
		convo += "\n" + next.toString(); 
	}
	
	public void addNext(String next){ 
		convo += "\n" + next; 
	}
	
	public void readFile(String matrix, String responses) throws IOException{
		
//		String firstLine = null, nextLine = null;
//		
//		BufferedReader inFile = new BufferedReader(new FileReader(filename));
//		firstLine = inFile.readLine();
//		Response next, last = new Response(firstLine);
//		
//		while(inFile.ready()){
//			
//			nextLine = inFile.readLine();
//			next = new Response(last, nextLine, training);
//			last = next;
//			addNext(nextLine);			
//		}
//		
//		inFile.close();	
		
		String nextLine = null;
		
		BufferedReader inFile1 = new BufferedReader(new FileReader(matrix));
		BufferedReader inFile2 = new BufferedReader(new FileReader(responses));
		
		int rows = 0, cols = 0;
		
		while(inFile1.ready()){
			
			nextLine = inFile1.readLine();
			StringTokenizer dimensions = new StringTokenizer(nextLine, " ");
			
			while(dimensions.hasMoreTokens()){
				Main.memory.matrix[rows][cols++] = 
						Integer.parseInt(dimensions.nextToken());
			}
			
			rows++;
			Main.dictionary.nextEmpty++;
			Main.memory.dimension++;
		}	
		
		inFile1.close();		
		
		int lineNum = 0;
		while(inFile2.ready()){
			
			nextLine = inFile2.readLine();
			Main.dictionary.add(nextLine, lineNum);
		}
		
		inFile2.close();
	}
	
	public void writeFile(String matrix, String responses) throws IOException{
		
//		PrintWriter newFile = new PrintWriter(filename);
//		
//		StringTokenizer output = new StringTokenizer(convo, "\n");
//		
//		while(output.hasMoreElements()) newFile.println(output.nextToken());
//			
//		newFile.close();
		
		PrintWriter newFile1 = new PrintWriter(matrix);
		PrintWriter newFile2 = new PrintWriter(responses);
		
		for(int i=0; i<Main.dictionary.nextEmpty; i++){
			
			for(int j=0; j<Main.dictionary.nextEmpty; j++){
				
				newFile1.print(Main.memory.matrix[i][j] + " ");
			}
			
			newFile1.println();
		}
		
		newFile1.close();
		
		for(int i=0; i<Main.dictionary.nextEmpty; i++){
			
			newFile2.println(Main.dictionary.getResponseAt(i).toString());
		}
		
		newFile2.close();
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

