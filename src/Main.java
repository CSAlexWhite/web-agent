import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

/**
 * The Main class of the WebAgent, where everything starts and everything comes
 * together.
 * @author Alex
 *
 */
public class Main {
	
	/* MAIN STATIC VARIABLES, FOR ACCESS FROM THROUGHOUT THE PROGRAM */
	public static ResponseList dictionary;	// THE BIG LIST OF PHRASES
	public static ResponseMatrix memory;	// THE ADJACENCY MATRIX OF THE GRAPH
	public static Conversation training;	// CONVERSATION STORAGE (USER-USER)
	public static Conversation discussion;	// CONVERSATION STORAGE (USER-BOT)
	public static JFrame mainWindow;
	public static WebAgentUI mainInterface;
	
	/* STATIC VARIABLES FOR MAIN AND CONSOLE UI, FOR OPTIONS */
	public static Boolean trainingToggle = false;
	public static Boolean printChoice = false;
	public static Boolean fullDebug = false;	
	
	/**
	 * Main method of WebAgent.  Connects to the database, gets system memory
	 * for the adjacency matrix it will use, and initializes that matrix, the 
	 * dictionary, which is a list of responses, and the discussion, which is a
	 * record of the current sessions bot-human interaction.  Also imports the
	 * data files from which to recreate its memory and dictionary, then launches
	 * the GUI. 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		WebAgentDB.connect();	// CONNECT TO THE DATABASE
	
		/* STUFF FOR INITIALIZING MEMORY AND HISTORY */
		int brainSize = 10000; // NUMBER OF RESPONSES WEBAGENT CAN STORE
		
		dictionary = new ResponseList(brainSize);
		memory = new ResponseMatrix(brainSize);
		discussion = new Conversation("");
		
		/* RETRIEVE PREVIOUS LIST AND MATRIX FROM FILE AND IMPORT */
		dictionary.readFile("list.data", true);
		memory.readFile("matrix.data");
					
		System.out.println("LAUNCHING GRAPHICAL INTERFACE ...");
		launchGUI();		
	}
	
	/**
	 * A method that can is called by a GUI element, returns the user to a console
	 * environment in which there is advanced functionality, including access 
	 * to printing the machine's memory and dictionary, its latest conversation,
	 * and also access to the database which allows queries of previous conversations
	 * and the overall dictionary according to mySQL syntax. 
	 * @throws IOException
	 */
	public static void launchConsole() throws IOException{
			
		/* INITIALIZE RESPONSE VARIABLES */
		String input = null, convoName = null;
		Response last = null, next = null;
		
		/* STUFF FOR READING CONSOLE INPUT */
		InputStreamReader sreader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(sreader);
					
		/* START THE [CONSOLE-BASED] CONVERSATION */
		last = dictionary.getResponseAt(0);
		System.out.println("LAUNCHING CONSOLE INTERFACE...");
		System.out.println("\n\t*****************************************");
		System.out.println("\t|   Welcome to the WebAgent console. \t|\n\t| "
							  + "Press help at any time to receive it.\t|");
		System.out.println("\t*****************************************");
		System.out.println("BOT->\t" + last.toString());		
		
		/* THE CONVERSATION LOOP FOR THE CONSOLE */
		while(true){ 
			
			System.out.print("USER->\t");	// USER PROMPT			
			input = reader.readLine();
				
			/*OPTIONS*/
			if(input.equalsIgnoreCase("help")){ printHelp(); continue;}
			if(input.equalsIgnoreCase("printm")){ memory.print(); continue;}
			if(input.equalsIgnoreCase("printl")){ dictionary.print(); continue;}
			if(input.equalsIgnoreCase("printd")){ discussion.print(); continue;}			
			if(input.equalsIgnoreCase("printr")){ trainingToggle ^= true; continue;}
			if(input.equalsIgnoreCase("printc")){ printChoice ^= true; continue;}
			if(input.equalsIgnoreCase("printall")){ fullDebug ^= true; continue;}
			if(input.equalsIgnoreCase("querydb")){ queryMode();}			
			if(input.equalsIgnoreCase("exit")){ 
				System.out.print("Writing conversation: " + convoName + " ... Done.");
				break;
			}
			
			/* TRAINING MODE : BETWEEN THE HUMAN AND HIM/HER SELF */
			if(trainingToggle == false){
				
				next = new Response(last, input, true);	// CREATE A NEW RESPONSE
				last = next;							// NOW THE PREVIOUS
								
				/* for debugging purposes*/
				if(fullDebug == true){memory.print(); dictionary.print();}		
			}
			
			/*CONVERSATION MODE : BETWEEN THE HUMAN AND THE BOT */ 
			if(trainingToggle == true){
				
				next = new Response(last, input, false);// CREATE A NEW RESPONSE		
				last = next;							// GIVEN USER INPUT
				next = memory.getNext(last); // MAIN RESPONSE ALGORITHM CHOOSES
											 // THE NEXT THING THE BOT SAYS
				last = next;				 
				
				System.out.println("BOT ->\t" + next.toString()); // BOT OUTPUT
				
				/* for debugging purposes*/
				if(fullDebug == true){ memory.print(); dictionary.print();}			
				if(printChoice == true) memory.printC();
			}
			
			/* EVERY TIME THROUGH THE RESPONSE CYCLE, SAVE THE DATA TO FILE*/
			dictionary.writeFile("list.data");
			memory.writeFile("matrix.data");		
		}
		
		discussion.writeFile();	// WRITE SAVE THE WHOLE DISCUSSION TO A NEW FILE
		sreader.close();		// FOR RECORD KEEPING PURPOSES
		reader.close();
	}
	
	/**
	 * A method which initializes the Database query mode of the console.  Takes
	 * mySQL queries and returns results right to the console output. Can print 
	 * the result set to a file if necessary.
	 * @throws IOException
	 */
	public static void queryMode() throws IOException{
		
		/* INITIALIZE SEPARATE READERS FOR THIS PHASE OF THE INTERACTION */
		InputStreamReader Qsreader = new InputStreamReader(System.in);
		BufferedReader Qreader = new BufferedReader(Qsreader);
		
		/* GIVE THE USER SOME INFORMATION */
		System.out.println("Please enter your query, the result "
				+ "set will be printed below.\n"
				+ "Available tables include \"CONVERSATIONS\" and \"DICTIONARY\"\n"
				+ "Enter exit to return to previous mode"
				+ "Enter print for a prompt to save results to file");
		
		/* INITIALIZE VARIABLES AND OBJECTS FOR THE QUERY LOOP*/
		String input = null, filename = null;
		ResultSet tempSet = null;
		while(true){
			
			input = Qreader.readLine();
			
			/* OPTIONAL STATEMENTS, TO EXIT AND PRINT*/
			if(input.equalsIgnoreCase("exit")){ break;}
			if(input.equalsIgnoreCase("print")){
		
					System.out.println(
							"Please enter the filename to output result set:");	
					filename = Qreader.readLine();
					
					/* CALLS THE DATABASE FILE-WRITING METHOD, TO EXPORT THE RESULTS*/
					try {WebAgentDB.rsToFile(tempSet, filename);} 
					catch (SQLException e) { System.out.println(
							"Result set not available");}
					System.out.println(filename + ".txt written");
					continue;
				}
			
			/*MAKE SURE TO HANDLE EXCEPTION IF QUERY IS MISTYPED */
			try {tempSet = WebAgentDB.queryDB(input);} 
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Please format your query correctly");
			}		
		}
	}
	
	/**
	 * A method to instantiate the right objects for the GUI to be available to view.
	 */
	public static void launchGUI(){
		
		mainWindow = new JFrame("\tWebAgent\t");
		mainInterface = new WebAgentUI();			
		mainWindow.setLocation(200, 200);	// THE LOCATION ON THE SCREEN
		
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainWindow.add(mainInterface);
	    mainWindow.pack();
	    mainWindow.setVisible(true);
	}
	
	/**
	 * A method to print a help dialog to the console user.
	 */
	public static void printHelp(){
		
		System.out.println("WebAgent help:");
		System.out.println(
		 "printr\t-\tTo change modes, Bot vs. Training\n" +
		 "querydb\t-\tTo pass a mySQL query to the database\n" +
		 "printm\t-\tTo print WebAgent's memory\n" +
		 "printl\t-\tTo print WebAgent's dictionary\n" +
		 "printd\t-\tTo reprint the current discussion\n" );
	}
}
