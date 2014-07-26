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
	
	public Response(Response last, String input, boolean training){
		
		if(training) Main.training.addNext(input);
		if(!training) Main.discussion.addNext(input);	// adds the content to the discussion
					
		// looks for the Response already, if there, overwrite it. 
		if(Main.dictionary.findResponse(input)) id = Main.dictionary.repeatID;					
		
		// otherwise assigns it a unique id
		else{ id = Main.dictionary.getSize(); content = input;}
		
		Main.dictionary.add(last, this); // adds this to the list of responses
		
		Main.memory.dimension = Main.dictionary.nextEmpty;		
	}
	
	public Response(String input){
		
		content = input;
		id = 0;
	}
	
	public String toString(){
		
		return content;
	}
	
	public int getID(){
		
		return id;
	}
}
