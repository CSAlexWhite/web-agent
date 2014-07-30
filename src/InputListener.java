import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;


public class InputListener implements ActionListener{

	ResponseMatrix memory;
	ResponseList dictionary;
	
	Conversation training;
	Conversation discussion;
	
	WebAgentUI mainUI;
	
	Boolean trainingToggle = false;
	
	/* INITIALIZING RESPONSE VARIABLES */
	String input = null, convoName = null;
	Response last = null, next = null;
	
	public InputListener(WebAgentUI userInterface){
		
		memory = Main.memory;
		dictionary = Main.dictionary;
		training = Main.training;
		discussion = Main.discussion;
		mainUI = userInterface;
		
		last = dictionary.getResponseAt(0);
	}

	public void actionPerformed(ActionEvent e){
		
		input = mainUI.inputField.getText();
		mainUI.inputField.setText("");
		mainUI.conversationArea.append("USER->\t" + input + "\n");
		
		/*TRAINING MODE*/
		if(trainingToggle == false){
			
			next = new Response(last, input, true);
			last = next;
							
			if(Main.fullDebug == true){
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
			
			new Thread(new BotResponse(next.toString())).start();
			
			if(Main.fullDebug == true){
				memory.print();
				dictionary.print();
			}
			
			if(Main.printChoice == true) memory.printC();
		}
		
		try { dictionary.writeFile("list.data");} catch (IOException e1) {}
		try { memory.writeFile("matrix.data");} catch (IOException e1) {}
	}
}
