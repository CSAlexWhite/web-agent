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
		masterList[0] = new Response("Hi there!");	// add the first response							
	}
	
	/**
	 * Adds a new response to the list, updates the adjacency matrix
	 * for the directed graph;
	 * @param newResponse
	 */
	public void add(Response last, Response newResponse){
		
		if(findResponse(newResponse.toString())){ 	// if the response is already there
			
			Main.memory.addEdge(last, Main.dictionary.getResponseAt(repeatID)); // increment the edge between
			return;// Main.dictionary.getResponseAt(repeatID);											// current and the repeat
		}

		else{
			masterList[nextEmpty] = newResponse;		// put the first response
														// into the array					
			Main.memory.addEdge(last, newResponse);
			nextEmpty++;			
			Main.memory.dimension = nextEmpty;
			return; // newResponse;
		}
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
		
		repeatID = 0;
		System.out.println("findResponse : " + nextEmpty);
			
		for(int i=0; i<nextEmpty; i++){				// Search for the target
			System.out.println(i);
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
			
			System.out.println(getResponseAt(i) + ":\t" + getResponseAt(i).getID());
		}
	}
}
