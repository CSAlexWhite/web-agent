import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class which enables Conversation objects, which hold the successive responses
 * generated during the course of a conversation with a bot or yourself.
 * @author Alex
 *
 */
public class Conversation {
	
	private Vector<String> convo;	// AN EXPANDABLE LIST OF CONVERSATION ELEMENTS
	boolean training = false;		// WHETHER OR NOT IN TRAINING MODE
	Response last, next;			// TO HOLD THE CURRENT AND LAST RESPONSES
	
	/**
	 * The constructor for a converersation, starts it with no elements.
	 * @param first
	 */
	public Conversation(String first){
		
		convo = new Vector<String>(0);
	}

	/**
	 * A method to add a new response to the conversation
	 * @param next
	 */
	public void addNext(Response next){ 
		convo.add(next.toString()); 
	}
	
	/**
	 * A method to add a simple String to the conversation.
	 * @param next
	 */
	public void addNext(String next){ 
		convo.add(next); 
	}
	
	/**
	 * A method to read in a conversation from a file.  Of great importance to
	 * the import conversation method available in the GUI.  Uses regular expressions
	 * to parse the beginning of lines of transcripts from CNN, so that only
	 * (mostly) the verbiage of the conversation is recorded.  Gets passed a file
	 * that comes from the JFileChooser.
	 * @param file
	 * @throws IOException
	 */
	public void readFile(File file) throws IOException{
		
		int linecount = 0;						// COUNT LINES TO OUTPUT PROGRESS
		last = Main.dictionary.getResponseAt(0);// SET THE FIRST RESPONSE IN THE 
												// CONVERSATION
		String nextLine = null;
		
		System.out.println("Importing: " + file.getAbsolutePath());
		
		/* GET READY TO READ IN THE FILE SPECIFIED */
		BufferedReader inFile = new BufferedReader(new FileReader(file.getAbsolutePath()));

		/* READ IN THE FILE */
		while(inFile.ready()){
						
			/* LOOK TO THE FIRST LINE, AND GET RID OF ANY PARTICIPANT LABELS
			 * E.G. "LARRY KING: "*/
			nextLine = inFile.readLine().
					replaceFirst("(^[^:>-]+[:>-]+\\s?)|(\\((.*?)\\))","");
			
			if(isBlank(nextLine)) continue;				//SKIP BLANK LINES
			
			/* THE USUAL PROCESS FOR ADDING A RESPONSE TO THE MEMORY */
			next = new Response(last, nextLine, true);
			last = next;
			
			/* COUNTING LINES TO OUTPUT PROGRESS */
			linecount++; if(linecount%25 == 0) System.out.print("|");
		}	
		
		System.out.print(" DONE |\n");
		inFile.close();		
	}
	
	/**
	 * A method which takes a conversation (the current one) and outputs it to 
	 * a new file with a date/time stamp, for record keeping and fun reading.
	 * @return
	 * @throws IOException
	 */
	public String writeFile() throws IOException{
		
		/* GET THE CURRENT DATE AND TIME */
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
	
		/* MAKE A NEW FILE USING THE DATE AND TIME */
		PrintWriter newFile = new PrintWriter(dateFormat.format(date) + ".txt");

		/* PRINT SUCCESSIVE ELEMENTS OF THE CONVERSATION TO THE FILE */
		for(int i=0; i<convo.size(); i++) newFile.println(convo.elementAt(i));
		
		/* CLOSE THE FILE AND RETURN THE NAME OF THE FILE TO */
		newFile.close(); return dateFormat.format(date).toString();
	}
	
	/**
	 * A method which checks if a String has any characters in it or not.
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String input) {
		
	    int length;
	    
	    /* OBVIOUSLY BLANK IF ITS LENGTH IS ZERO */
	    if (input == null || (length = input.length()) == 0) return true;
	    
	    /* ALSO BLANK IF THE ONLY CHARACTRS ARE WHITESPACE CHARACTERS */
	    for (int i = 0; i < length; i++) {
	        if ((Character.isWhitespace(input.charAt(i)) == false)) return false; 
	    }
	    return true;
	}
	
	/**
	 * A debugging method, prints the conversation tot he console, called in the
	 * main console method.
	 */
	public void print(){ System.out.println(convo);}
}

