import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
	
	public boolean equals(String target){
		
		String thisString = content;
		String thatString = target;
		String thisOutString = "";
		String thatOutString = "";
		
		Pattern pattern =
				Pattern.compile("(?i)[^aAeEiIoOuU!?'.,\\s]*(?-i)");
		
		Matcher thisMatcher = pattern.matcher(thisString);
		Matcher thatMatcher = pattern.matcher(thatString);
		
		while(thisMatcher.find()){
			thisOutString += thisMatcher.group();		
		} System.out.println("this: " + thisOutString);
		
		while(thatMatcher.find()){
			thatOutString += thatMatcher.group();			
		} System.out.println("that: " + thatOutString);
 
		if(thisOutString.contains(thatOutString.toLowerCase())) return true;
		if(thatOutString.contains(thisOutString.toLowerCase())) return true;
		
		return false;
	}
}
