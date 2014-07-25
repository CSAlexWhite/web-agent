/**
 * An object which holds response content plus a vector of responses which 
 * have been next in the past, which expands if new responses occur
 * 
 * @author Alex
 *
 */
public class Response {

	private String content;		// the content of this response
	private final int id;		// the primary key of this response
	
	public Response(Response last, String input){
		
		content = input;	// adds the response content to the object
		id = Main.dictionary.getSize();		// assigns it a unique id number
		Main.dictionary.add(last, this);	// adds this response to the
											// big list of responses		
	}
	
	/**
	 * for the first entry in the graph
	 * @param input
	 */
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
