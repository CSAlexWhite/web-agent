/**
 * An object which holds response content plus a vector of responses which 
 * have been next in the past, which expands if new responses occur
 * 
 * @author Alex
 *
 */
public class Response {

	private String content;		// the content of this response
	private int id;				// the primary key of this response
	
	/**
	 * A new response from the user, when created its content is set and it
	 * asks the ResponseList if it exists yet, if so, its ID is set to the 
	 * ID of the extant Response, if not, it's added to the end of the list
	 * @param last
	 * @param input
	 * @param training
	 */
	public Response(Response last, String input, boolean training){
		
		//if(training) Main.training.addNext(input);
		//if(!training) 
		Main.discussion.addNext(input);	// adds the content to the discussion
					
		// looks for the Response already, if there, overwrite it. 
		if(Main.dictionary.findResponse(input)){
			id = Main.dictionary.repeatID;
			Main.memory.addEdge(last.getID(),id);
		}
		
		// otherwise assigns it a unique id
		else{ 	id = Main.dictionary.getSize(); content = input;		
				Main.dictionary.add(last, this); // adds this to the list of responses
		}
		
		Main.memory.dimension = Main.dictionary.nextEmpty;		
	}
	
	public Response(String input, int idNum){
		
		content = input;
		id = idNum;
	}
	
	public String toString(){
		
		return content;
	}
	
	public int getID(){
		
		return id;
	}
	
	// TO DO 
//	public boolean equals(Response target){
//		
//		
//	}
}
