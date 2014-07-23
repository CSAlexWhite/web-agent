/**
 * The one-dimensional list of responses ever entered, each index corresponds
 * to the ID that the Response is assigned.
 * 
 * @author Alex White
 *
 */
public class ResponseList {
	
	Response[] masterList;
	int nextEmpty;
	
	public ResponseList(int size, Response root){
			
		nextEmpty = 0;						// set the first position
		masterList = new Response[size];	// initialize the array
		masterList[0] = root;				// add the first response
	}
	
	/**
	 * Adds a new response to the list, updates the adjacency matrix
	 * for the directed graph;
	 * @param newResponse
	 */
	public void add(Response current, Response newResponse){
		
			
		masterList[nextEmpty] = newResponse;// put the first response
											// into the array
					
		Main.memory.addEdge(current, newResponse);
		nextEmpty++;						// increment the next	
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
	Response findResponse(Response current, String target){
			
		for(int i=0; i<getSize(); i++){				// Search for the target
			if(masterList[i].getContent() == target) 			
				return masterList[i];
		}
		
		add(current, new Response(target));		// if not found, add it		
		return masterList[getSize()];			// and return that
	}
	
	/**
	 * Returns the current length of the list of responses
	 * @return
	 */
	public int getSize(){
		
		return nextEmpty;
	}
}
