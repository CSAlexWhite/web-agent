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

public class Conversation {
	
	private Vector<String> convo;
	boolean training = false;
	Response last, next;
	
	public Conversation(String first){
		
		convo = new Vector<String>(0);
	}

	public void addNext(Response next){ 
		convo.add(next.toString()); 
	}
	
	public void addNext(String next){ 
		convo.add(next); 
	}
	
	public void readFile(File file) throws IOException{
		
		String line2;
		int linecount = 0;
		last = Main.dictionary.getResponseAt(0);
		String nextLine = null;
		System.out.println(file.getAbsolutePath());
		BufferedReader inFile = new BufferedReader(new FileReader(file.getAbsolutePath()));

		while(inFile.ready()){
						
			nextLine = inFile.readLine().
					replaceFirst("(^[^:>-]+[:>-]+\\s?)|(\\((.*?)\\))","");
			if(isBlank(nextLine)) continue;
			next = new Response(last, nextLine, true);
			last = next;
			linecount++;
			if(linecount%43 == 0) System.out.println("Importing " + linecount);
		}	
		
		System.out.println("Done");
		inFile.close();		
	}
	
	public String writeFile() throws IOException{
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
	
		PrintWriter newFile = 
				new PrintWriter(dateFormat.format(date) + ".txt");

		for(int i=0; i<convo.size(); i++){
			newFile.println(convo.elementAt(i));
		}
					
		newFile.close();
		
		return dateFormat.format(date).toString();
	}
	
	public static boolean isBlank(String str) {
		
	    int length;
	    if (str == null || (length = str.length()) == 0) {
	        return true;
	    }
	    for (int i = 0; i < length; i++) {
	        if ((Character.isWhitespace(str.charAt(i)) == false)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public void print(){ 
		System.out.println(convo); 
	}
}

