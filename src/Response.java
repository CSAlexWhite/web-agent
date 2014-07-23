import java.util.Random;
import java.util.Vector;

/**
 * An object which holds response content plus a vector of responses which 
 * have been next in the past, which expands if new responses occur
 * 
 * @author Alex
 *
 */
public class Response {

	private String content;					// the content of this response
	//private Vector <Response> nexts;		// responses which can be next
	//private Vector <Integer> outWeights;	// frequency of use of that response
	private final int id;					// the primary key of this response
	
	public Response(Response last, String input){
		
		content = input;	// adds the response content to the object
		
		//nexts = new Vector<Response>(0);	// creates the vector of objects 
											// this response connects to
		
		//outWeights = new Vector<Integer>(0);// initilize outWeights vector
		
		Main.dictionary.add(last, this);			// adds this response to the
											// big list of responses
		
		id = Main.dictionary.getSize();		// gives it a unique id number
	}
	
	public void addNext(Response input){
		
		nexts.add(input);
	}
	
	/**
	 * Takes an input string, searches next for it
	 * If no such string, adds it to nexts and returns "Hello"
	 * @param input
	 * @return
	 */
	public Response searchNexts(String input){
		
		if 
		
		return 
	}
	
	public Response chooseNext(){
		
		// CURRENT RESPONSE CHOICE ALGORITHM!
		Random rand = new Random();
		
		int index = rand.nextInt(nexts.size());	
		return nexts.get(index);
	}
	
	public String getContent(){
		
		return content;
	}
	
	public int getID(){
		
		return id;
	}
}
