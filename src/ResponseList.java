import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The one-dimensional list of responses ever entered, each index corresponds
 * to the ID that the Response is assigned.
 * 
 * @author Alex White
 *
 */
public class ResponseList {
	
	private Response[] masterList; 	// THE BIG LIST OF UNIQUE RESPONSES
	int nextEmpty = 1; 				// SETS THE FIRST EMPTY POSITION
	int repeatID = 0;		// THE POSITION TO REFERENCE FOR A REPEAT PHRASE
							
	Boolean afterInit = false; // TO TRACK IF THE DATABASE IS UPDATED YET
	
	/**
	 * Constructor for the response list, initiates the list with some phrase.
	 * @param size
	 */
	public ResponseList(int size){
					
		masterList = new Response[size];				// INITIALIZE THE ARRAY
		masterList[0] = new Response("Hi there!", 0);	// ADD THE FIRST RESPONSE						
	}
	
	/**
	 * Adds a new response to the list, updates the adjacency matrix
	 * for the directed graph;
	 * @param newResponse
	 */
	public void add(Response last, Response newResponse, float sensitivity){
		
		/* IF THE RESPONSE IS ALREADY THERE */
		if(findResponse(newResponse.toString(), sensitivity)){ 	
			
			/* INCREMENT THE EDGE WEIGHT BETWEEN THE CURRENT AND REPEAT RESPONSE */
			Main.memory.addEdge(last, Main.dictionary.getResponseAt(repeatID)); 
			return;										
		}

		else{
			
			/* ADD THE NEW RESPONSE TO THE BIG LIST */
			masterList[nextEmpty] = newResponse;
			
			/* THEN ADD THAT RESPONSE TO THE DATABASE IF PROGRAM HAS INITIALIZED
			 * ALREADY*/
			if(afterInit) WebAgentDB.addToDictDB(
					newResponse.toString().length(), newResponse.toString());											// into the array
			
			/* THEN ADD AN EDGE BETWEEN THE LAST AND THE NEW RESPONSE ON THE
			 * ADJACENCY MATRIX */
			Main.memory.addEdge(last, newResponse);
			nextEmpty++;							// INCREMENT END OF LIST
			Main.memory.dimension = nextEmpty;				
			return; 
		}
	}
	
	/**
	 * A method to just add new Response to the list at a specific point, based
	 * on a string input.
	 * @param input
	 * @param id
	 */
	public void add(String input, int id){
		
		masterList[id] = new Response(input, id);
	}
	
	/**
	 * A method to correctly add a new Response to the list at the right point,
	 * based on a string input.
	 * @param input
	 */
	public void add(String input){
		
		masterList[nextEmpty] = new Response(input, nextEmpty);
		nextEmpty++;
	}
	
	/**
	 * Given the response ID number, return a reference to the response
	 * 
	 * @param idNumber
	 * @return
	 */
	Response getResponseAt(int idNumber){
		
		return masterList[idNumber];
	}
	
	/**
	 * Given an input string (and the current response) return it if it exists
	 * as a previous response, else add it to the response list and return a
	 * reference to the new Response
	 * 
	 * @param current
	 * @param target
	 * @return
	 */
	public Boolean findResponse(String target, float sensitivity){
		
		/* SEARCH FOR THE TARGET IN A RANDOM MANNER, SINCE WE DON'T ALWAYS
		 * WANT WHAT THE LAST RESPONSE WAS, OR ONE IN ORDER 
		 * MAKES THE BOT SEEM CRAZIER, THOUGH */	
		for(int i=0; i<nextEmpty*10; i++){	
			int j = (int)(Math.random()*nextEmpty);
			
			/* IF THE MATCH IS BELOW THE SENSITIVITY, KEEP LOOPING */
			if(masterList[j].equals(target) >= sensitivity){
				repeatID = j;	
				return true;
			}			
		}
		
		return false;
	}
	
	/**
	 * Returns the current length of the list of responses
	 * @return
	 */
	public int getSize(){
		
		return nextEmpty;
	}
	
	/**
	 * Prints to the console the current master list of responses
	 */
	public void print(){
		
		for(int i=0; i<nextEmpty; i++){
			
			System.out.println(getResponseAt(i).getID() + ":\t" + getResponseAt(i));
		}
	}
	
	/**
	 * Writes the current master list of responses to the correct data file.
	 * @param filename
	 * @throws IOException
	 */
	public void writeFile(String filename) throws IOException{
		
		PrintWriter newFile = new PrintWriter(filename);
		for(int i=0; i<nextEmpty; i++) newFile.println(masterList[i]);		
		newFile.close();
	}
	
	/**
	 * Reads in the current correct data file so the program can function.
	 * @param filename
	 * @param first
	 * @throws IOException
	 */
	public void readFile(String filename, boolean first) throws IOException{
		
		String nextLine = null;	// THE NEXT LINE TO READ
		
		/* FIRST, ERASE THE DATABASE DICTIONARY, IT WILL BE REBUILT 
		 * THIS AVOIDS APPENDING THE DICTIONARY, AND DOUBLING IT */
		WebAgentDB.instruct("TRUNCATE TABLE dictionary");
		
		/* ONLY PRINT IMPORTING DICTIONARY AT PROGRAM LAUNCH */
		if(first) System.out.print("IMPORTING DICTIONARY ...\n");
		
		BufferedReader inFile = new BufferedReader(new FileReader(filename));	
		System.out.print("UPDATING DATABASE ...\t");	
		
		/* NOW READ IN THE FILE LINE BY LINE AND APPEND THE CONTENTS
		 * TO THE DATABSE AND TO THE LIST */
		int lineNum = 0;	
		while(inFile.ready()){
			
			nextLine = inFile.readLine();
			
			WebAgentDB.addToDictDB(nextLine.length(), nextLine); 	// ADD TO DB
			add(nextLine, lineNum); lineNum++; nextEmpty = lineNum; // ADD TO LIST
		}
		
		System.out.println("\t" + lineNum + " responses.");
		inFile.close();
	}
}
