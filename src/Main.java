import java.io.*;

import javax.swing.JFrame;

public class Main {
	
	public static ResponseList dictionary;
	public static ResponseMatrix memory;
	public static Conversation training;
	public static Conversation discussion;
	
	static Boolean trainingToggle = false;
	static Boolean printChoice = false;
	static Boolean fullDebug = false;	
	
	static JFrame mainWindow;
	static WebAgentUI mainInterface;
	
	public static void main(String[] args) throws Exception {
		
		WebAgentDB.connect();
	
		/* STUFF FOR INITIALIZING MEMORY AND HISTORY */
		int brainSize = 10000;
		dictionary = new ResponseList(brainSize);
		memory = new ResponseMatrix(brainSize);
		discussion = new Conversation("");
		
		/* RETRIEVE PREVIOUS LIST AND MATRIX AND IMPORT */
		dictionary.readFile("list.data");
		memory.readFile("matrix.data");
					
		System.out.println("LAUNCHING USER INTERFACE ...");
		launchGUI();	
		WebAgentDB.disconnect();	
	}
	
	public static void launchConsole() throws IOException{
			
		/* INITIALIZING RESPONSE VARIABLES */
		String input = null, convoName = null;
		Response last = null, next = null;
		
		/* STUFF FOR READING CONSOLE INPUT */
		InputStreamReader sreader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(sreader);
					
		/* START THE [CONSOLE-BASED] CONVERSATION */
		last = dictionary.getResponseAt(0);
		System.out.println("\t*****************************************");
		System.out.println("\t|   Welcome to the WebAgent console. \t|\n\t| "
							  + "Press help at any time to receive it.\t|");
		System.out.println("\t*****************************************");
		System.out.println("BOT->\t" + last.toString());		
		while(true){ System.out.print("USER->\t");
					
			input = reader.readLine();
				
			/*DEBUGGING OPTIONS*/
			if(input.equalsIgnoreCase("printm")){ memory.print(); continue;}
			if(input.equalsIgnoreCase("printl")){ dictionary.print(); continue;}
			if(input.equalsIgnoreCase("printd")){ discussion.print(); continue;}
			
			if(input.equalsIgnoreCase("printr")){ trainingToggle ^= true; continue;}
			if(input.equalsIgnoreCase("printc")){ printChoice ^= true; continue;}
			if(input.equalsIgnoreCase("printall")){ fullDebug ^= true; continue;}	
			
			if(input.equalsIgnoreCase("exit")){ 
				System.out.print("Writing conversation: " + convoName + " ... Done.");
				break;
			}
			
			if(input.equalsIgnoreCase("help")){ printHelp(); continue;}
			
			/*TRAINING MODE*/
			if(trainingToggle == false){
				
				next = new Response(last, input, true);
				last = next;
								
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}		
			}
			
			/*CONVERSATION MODE*/ 
			if(trainingToggle == true){
				
				next = new Response(last, input, false);			
				last = next;	
				next = memory.getNext(last);// AI ALGORITHM!!
				last = next;			
				System.out.println("BOT ->\t" + next.toString());
				
				if(fullDebug == true){
					memory.print();
					dictionary.print();
				}
				
				if(printChoice == true) memory.printC();
			}
			
			dictionary.writeFile("list.data");
			memory.writeFile("matrix.data");		
		}
		
		discussion.writeFile();
		sreader.close();
		reader.close();
	}
	
	public static void launchGUI(){
		
		mainWindow = new JFrame("\tWebAgent\t");
		mainInterface = new WebAgentUI();
				
		mainWindow.setLocation(200, 200);
	    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainWindow.add(mainInterface);
	    mainWindow.pack();
	    mainWindow.setVisible(true);
	}
	
	public static void printHelp(){
		
		System.out.println("WebAgent help:");
		System.out.println(
		 "printr\t-\tTo change modes, Bot vs. Training\n" +
		 "printm\t-\tTo print WebAgent's memory\n" +
		 "printl\t-\tTo print WebAgent's dictionary\n" +
		 "printd\t-\tTo reprint the current discussion\n" );
	}
}
