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
	
	private Response[] masterList;
	int nextEmpty = 1; 		// set the first position	
	int repeatID = 0;		// the position to reference if a repeat phrase
							// is found
	
	public ResponseList(int size){
					
		masterList = new Response[size];			// initialize the array
		masterList[0] = new Response("Hi there!", 0);	// add the first response							
	}
	
	/**
	 * Adds a new response to the list, updates the adjacency matrix
	 * for the directed graph;
	 * @param newResponse
	 */
	public void add(Response last, Response newResponse){
		
		if(findResponse(newResponse.toString())){ 	// if the response is already there
			
			Main.memory.addEdge(last, Main.dictionary.getResponseAt(repeatID)); // increment the edge between
			return;											// current and the repeat
		}

		else{
			masterList[nextEmpty] = newResponse;		// put the first response
														// into the array					
			Main.memory.addEdge(last, newResponse);
			nextEmpty++;			
			Main.memory.dimension = nextEmpty;
			return; 
		}
	}
	
	public void add(String input, int id){
		
		masterList[id] = new Response(input, id);
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
	public Boolean findResponse(String target){
		
		//System.out.println("findResponse : " + nextEmpty);
			
		for(int i=0; i<nextEmpty; i++){				// Search for the target
			//System.out.println(i);
			if(masterList[i].toString().equalsIgnoreCase(target)){
				repeatID = i;						// where the target is
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
	
	public void print(){
		
		for(int i=0; i<nextEmpty; i++){
			
			System.out.println(getResponseAt(i).getID() + ":\t" + getResponseAt(i));
		}
	}
	
	public void writeFile(String filename) throws IOException{
		
		PrintWriter newFile = new PrintWriter(filename);
		
		for(int i=0; i<nextEmpty; i++){
			
			newFile.println(masterList[i]);
		}
		
		newFile.close();
	}
	
	public void readFile(String filename) throws IOException{
		
		String nextLine = null;	
		BufferedReader inFile = new BufferedReader(new FileReader(filename));
		
		int lineNum = 0;
		while(inFile.ready()){
			
			nextLine = inFile.readLine();
			add(nextLine, lineNum);
			lineNum++;
			nextEmpty = lineNum;
		}
		
		inFile.close();
	}
}
