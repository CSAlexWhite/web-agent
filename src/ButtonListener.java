import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The class which listens for buttons to be pressed.
 * @author Alex
 *
 */
public class ButtonListener implements ActionListener {

	// Whether some feature should be on or off.
	boolean toggle;
	
	/**
	 * The constructor of the button listener, takes only the training button
	 * as a parameter (for now)
	 * @param trainingMode
	 */
	public ButtonListener(JButton trainingMode){}
	
	/**
	 * What to do when the training button is pressed.
	 */
	public void actionPerformed(ActionEvent event) {
		
		/* TOGGLE THE TRAINING MODE FROM WHEREVER IT WAS */
		Main.mainInterface.processor.trainingToggle ^= true;
		toggle = Main.mainInterface.processor.trainingToggle;
		
		/* CHANGE THE LABEL OF THE BUTTON TO REFLECT THE CURRENT MODE */
		if(toggle == true) Main.mainInterface.trainingMode.setText("BOT MODE");
		else Main.mainInterface.trainingMode.setText("USER MODE");
	}
}
