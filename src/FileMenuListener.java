import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * The class that listens to the inputs from the File menu of the GUI, and 
 * knows what to do for each menu element.
 * @author Alex
 *
 */
public class FileMenuListener  implements ActionListener{

	/* STATIC VARIABLES ACCESSED FROM WITHIN THE CLASS */
	JFrame mainFrame;			// THE MAIN GUI CONTAINER
	JFileChooser fileChooser;	// THE FILE CHOOSER FOR OPENING TRANSCRIPTS
	WebAgentUI mainUI;			// THE MAIN GUI OBJECT
	
	/**
	 * The FileMenuListener constructur, sets the local variables, which
	 * include relevent GUI elements and the fileChooser object.
	 * @param userInterface
	 * @param inputFrame
	 */
	public FileMenuListener (WebAgentUI userInterface, JFrame inputFrame){
		
		mainFrame = inputFrame;
		mainUI = userInterface;
		fileChooser = new JFileChooser();
	}
	
	/**
	 * When a menu button is clicked, this method knows what to do.
	 */
	public void actionPerformed(ActionEvent e) {
	
		/* FIRST, GET THE NAME OF THE MENU ITEM THAT WAS CLICKED */
		String menuName = e.getActionCommand();
		
		/* HERE, PRINT THE CURRENT CONVERSATION TO A FILE */
		if(menuName.equals("Print Conversation")){
			
			try {Main.discussion.writeFile();} catch (IOException e1) {}
			mainUI.conversationArea.append("\tCONVERSATION SAVED.");
		}
		
		/* CALL THE RIGHT METHODS TO IMPORT A CONVERSATION FROM A TRANSCRIPT */
		if(menuName.equals("Import Conversation")){
			
			/* GETS A VALUE FROM THE JFILE CHOOSER ABOUT */
			int number = fileChooser.showOpenDialog(mainUI);
			
			/* GETS THE FILE THE JFILE CHOOSER SELECTS */
			File file = fileChooser.getSelectedFile();
			
			/* PASSES THAT TO THE CONVERSATION CLASS, WHICH READS THE FILEL
			 * AND ADDS ITS CONTENTS TO THE RESPONSE LIST AND THE DATABASE */
			try {Main.discussion.readFile(file);} catch (IOException e1) {}
			
			/* WRITES THE NEW INFORMATION TO THE RELEVANT DATA FILES */
			try { Main.dictionary.writeFile("list.data");} catch (IOException e1) {}
			try { Main.memory.writeFile("matrix.data");} catch (IOException e1) {}
			
			/* READS BACK IN THE LIST DATA FILE TO UPDATE THE DATABASE*/
			try { Main.dictionary.readFile("list.data", false);} catch (IOException e1) {}
		}
		
		/* QUITS EVERYTHING */
		if(menuName.equals("Quit")){
			
			mainFrame.dispose();
			try {WebAgentDB.disconnect();} 
			catch (Exception e1) {e1.printStackTrace();}
			System.exit(0);
		}
	}

}
