import java.util.Vector;
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
	final float highSensitivity = (float).9;
	final float lowSensitivity = (float).05;
	
	/**
	 * A new response from the user, when created its content is set and it
	 * asks the ResponseList if it exists yet, if so, its ID is set to the 
	 * ID of the extant Response, if not, it's added to the end of the list
	 * @param last
	 * @param input
	 * @param training
	 */
	public Response(Response last, String input, boolean training){
			
		if(training){ // ONLY MATCH THE RESPONSES THAT ARE EXACTLY THE SAME
			
			/* LOOKS FOR THE RESPONSE, IF IT EXISTS RETURN A REFERENCE TO IT*/
			if(Main.dictionary.findResponse(input, highSensitivity)){
				id = Main.dictionary.repeatID;
				Main.memory.addEdge(last.getID(),id); // ADD AN EDGE TO THE GRAPH
			}
			
			/* OTHERWISE, CREATE IT AND GIVE IT A UNIQUE ID NUMBER 
			 * THEN USE THAT INFORMATION TO ADD IT TO THE DICTIONARY */
			else{ 	id = Main.dictionary.getSize(); content = input;		
					Main.dictionary.add(last, this, highSensitivity); 
			}
			
			/*RESET THE DIMENSION VARIABLE TO THE DICTIONARY LENGTH*/
			Main.memory.dimension = Main.dictionary.nextEmpty;	
		}
		
		else{ // MATCH RESPONSES THAT MATCH IN ONLY SOME RESPECTS, LOW SENSITIVITY
			
			/* LOOKS FOR THE RESPONSE, IF IT EXISTS RETURN A REFERENCE TO IT*/
			if(Main.dictionary.findResponse(input, lowSensitivity)){
				id = Main.dictionary.repeatID;
				Main.memory.addEdge(last.getID(),id); // ADD AN EDGE TO THE GRAPH
			}
			
			/* OTHERWISE, CREATE IT AND GIVE IT A UNIQUE ID NUMBER 
			 * THEN USE THAT INFORMATION TO ADD IT TO THE DICTIONARY */
			else{ 	id = Main.dictionary.getSize(); content = input;		
					Main.dictionary.add(last, this, lowSensitivity); // adds this to the list of responses
			}
			
			/*RESET THE DIMENSION VARIABLE TO THE DICTIONARY LENGTH*/
			Main.memory.dimension = Main.dictionary.nextEmpty;	
		}
			
	}
	
	/**
	 * Used to construct a temporary Response for certain purposes.
	 * @param input
	 * @param idNum
	 */
	public Response(String input, int idNum){
		
		content = input;
		id = idNum;
	}
	
	/**
	 * Get method for Response content.
	 */
	public String toString(){ return content;}
	
	/**
	 * Get method for Response ID number
	 * @return
	 */
	public int getID(){	return id;}
	
	/**
	 * Important method, uses regular expressions and some other criteria to 
	 * determine if two Responses match each other.  The regex removes all
	 * special characters, and then the different words of the two responses
	 * are compared. A calculation of the number of equal words is done, and
	 * that calculation is the rate of match.  This number is used when
	 * requiring certain sensitivity in the above new Response constructor.
	 * @param target
	 * @return
	 */
	public float equals(String target){
		
		/* LOCAL VARIABLE DECLARATIONS */
		String thisString = content;	// TO COMPARE FROM
		String thatString = target;		// TO COMPARE TO
		
		/* NEW VECTORS TO HOLD THE STRING OF WORDS IN A RESPONSE */
		Vector <String> inWords = new Vector<String>(0);
		Vector <String> outWords = new Vector<String>(0);
		
		/* THE REGEX PATTERN THAT MATCHES ONLY THE INDIVIDUAL WORDS */
		Pattern pattern =
				
				Pattern.compile("\\b(?i)[^!?'.,\\s]+(?-i)\\b");
		
		Matcher thisMatcher = pattern.matcher(thisString); // COMPARE FROM
		Matcher thatMatcher = pattern.matcher(thatString); // COMPARE TO
		
		/* ADD INDIVIDUAL WORDS TO THE IN-VECTOR */
		while(thisMatcher.find()){
			inWords.add(thisMatcher.group());
			} 
		
		/* ADD INDIVIDUAL WORDS TOT HE OUT-VECTOR */
		while(thatMatcher.find()){
			outWords.add(thatMatcher.group());		
		} 
 
		/* COUNT HOW MANY WORDS MATCH BETWEEN THE TWO, DOUBLE FOR LOOP */
		int match = 0, total = inWords.size() + outWords.size();
		for(int i=0; i<inWords.size(); i++){			
			for(int j=0; j<outWords.size(); j++){			
				if(inWords.elementAt(i).equalsIgnoreCase(outWords.elementAt(j)))
					match++;
			}
		}
		
		/* CALCULATE THE HITRATE AS THE RATIO OF MATCHES TO TOTAL WORDS 
		 * SENSITIVITY WILL LATER COMPARE TO HITRATE WHEN PRODUCING A RESPONSE
		 * FROM THE BOT */
		if(total==0) return total;
		float hitrate = (float) match/total;
		return hitrate;
	}
}
