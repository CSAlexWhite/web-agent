import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;

/**
 * The class that handles input fromt input frame in the main GUI
 * @author Alex
 *
 */
public class InputListener implements ActionListener{

	/* STATIC VARIABLES ACCESSIBLE FROM WITHIN THE CLASS*/
	ResponseMatrix memory;		// THE MAIN MEMORY OBJECT
	ResponseList dictionary;	// THE MAIN DICTIONARY OBJECT
	Conversation training;		// THE TRAINING CONVERSATION
	Conversation discussion;	// THE REAL CONVERSATION
	WebAgentUI mainUI;			// THE MAIN GUI OBJECT
	
	Boolean trainingToggle = false;	// WHETHER WE ARE TRAINING OR NOT
	
	/* INITIALIZING RESPONSE VARIABLES */
	String input = null, convoName = null;
	Response last = null, next = null;
	
	/**
	 * The method which sets all the object variables for this listener, since
	 * it needs access to many of the main ones from Main, here they are.
	 * @param userInterface
	 */
	public InputListener(WebAgentUI userInterface){
		
		memory = Main.memory;
		dictionary = Main.dictionary;
		training = Main.training;
		discussion = Main.discussion;
		mainUI = userInterface;	
		last = dictionary.getResponseAt(0);
	}
	
	/**
	 * The main function of the InputListener class.  Parallells the functionality
	 * of the launchConsole() method in the Main class, initiating a back and
	 * forth conversation.
	 */
	public void actionPerformed(ActionEvent e){ // UPON HITTING THE ENTER KEY
		
		/* GETS THE CURRENT TEXT FROM THE GUIS INPUT BOX */
		input = mainUI.inputField.getText();
		
		/* RESET THE INPUT FIELD TO BEING EMPTY AFTER A RESPONSE IS ENTERED 
		 * AND THEN ADD THE PROMPT TO THE CONVERSATION AREA */
		mainUI.inputField.setText("");
		mainUI.conversationArea.append("USER->\t" + input + "\n");
		
		/* TRAINING MODE */
		if(trainingToggle == false){
					
			next = new Response(last, input, true);	// ADD RESPONSES BASED ON
			last = next;							// ONLY THE USER'S INPUT
				
		}
		
		/* CONVERSATION MODE */ 
		if(trainingToggle == true){
			
			/* ADDS THE NEXT RESPONSE TO THE CONVERSATION, GETS THE NEXT
			 * AND ADDS IT TO THE DATABASE */
			Main.discussion.addNext("USER->\t" + input);	
			next = new Response(last, input, false);
			WebAgentDB.addToConvoDB(next.getID(), true, last.getID());
			
			/* GETS THE NEXT RESPONSE FROM THE BOT AND ADDS IT TO THE DATABASE
			 * THEN ADDS IT TO THE CONVERSATION, GETS THE NEXT RESPONSE */
			last = next;	
			next = memory.getNext(last);
			WebAgentDB.addToConvoDB(next.getID(), false, last.getID());
			last = next;		
			Main.discussion.addNext("BOT->\t" + next.toString());
			
			/* MAINLY, CREATES AN OBJECT WHICH THEN PAUSES ON A DIFFERENT
			 * THREAD BEFORE RETURNING TO THIS ONE, TO SIMULATE A 
			 * DELAY IN THE RESPONSE*/
			new Thread(new BotResponse(last.toString())).start();
		}
		
		/* EVERY TIME THROUGH, WRITE ALL THE INFORMATION TO THE DATA FILES */
		try { dictionary.writeFile("list.data");} catch (IOException e1) {}
		try { memory.writeFile("matrix.data");} catch (IOException e1) {}
	}
}
