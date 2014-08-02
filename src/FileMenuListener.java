import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


public class FileMenuListener  implements ActionListener{

	JFrame mainFrame;
	
	public FileMenuListener (JFrame inputFrame){
		
		mainFrame = inputFrame;
	}
	
	public void actionPerformed(ActionEvent e) {
	
		String menuName = e.getActionCommand();
		
		if(menuName.equals("Something")){
			
			// do something
		}
		
		if(menuName.equals("Quit")){
			
			mainFrame.dispose();
			System.exit(0);
		}
	}

}
