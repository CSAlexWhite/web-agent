import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileMenuListener  implements ActionListener{

	JFrame mainFrame;
	JFileChooser fileChooser;
	WebAgentUI mainUI;
	
	public FileMenuListener (WebAgentUI userInterface, JFrame inputFrame){
		
		mainFrame = inputFrame;
		mainUI = userInterface;
		fileChooser = new JFileChooser();
	}
	
	public void actionPerformed(ActionEvent e) {
	
		String menuName = e.getActionCommand();
		
		if(menuName.equals("Print Conversation")){
			
			try {Main.discussion.writeFile();} catch (IOException e1) {}
			mainUI.conversationArea.append("\tCONVERSATION SAVED.");
		}
		
		if(menuName.equals("Import Conversation")){
			
			int number = fileChooser.showOpenDialog(mainUI);
			File file = fileChooser.getSelectedFile();
			try {Main.discussion.readFile(file);} catch (IOException e1) {}
			
			try { Main.dictionary.writeFile("list.data");} catch (IOException e1) {}
			try { Main.memory.writeFile("matrix.data");} catch (IOException e1) {}
		}
		
		if(menuName.equals("Quit")){
			
			mainFrame.dispose();
			try {WebAgentDB.disconnect();} 
			catch (Exception e1) {e1.printStackTrace();}
			System.exit(0);
		}
	}

}
