import java.util.Vector;


public class ResponseList {
	
	Response[] masterList;
	int nextEmpty;

	public ResponseList(int size, Response root){
			
		nextEmpty = 0;						// set the first position
		masterList = new Response[size];	// initialize the array
		this.add(root, root);				// add the first response
	}
	
	/**
	 * Adds a new response to the list, updates the adjacency matrix
	 * for the directed graph;
	 * @param newResponse
	 */
	public void add(Response currentResponse, Response newResponse){
				
		masterList[nextEmpty] = newResponse;// put the first response
											// into the array
		nextEmpty++;						// increment the next										
	}
	
	Response getResponseAt(int idNumber){
		
		return MasterList.get(idNumber);
	}
	
	Response findResponse(String target){
		
		for(int i=0; i<MasterList.size(); i++){
			
			if(MasterList.get(i).getContent() == target) 
				
				return MasterList.get(i);
		}
		
		add(new Response(target));
		
		return MasterList.lastElement();
	}
	
	public int getSize(){
		
		return MasterList.size();
	}
}
