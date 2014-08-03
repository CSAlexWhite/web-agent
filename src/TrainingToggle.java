import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class TrainingToggle implements ActionListener {

	boolean toggle;
	
	public TrainingToggle(JButton trainingMode){}
	
	public void actionPerformed(ActionEvent event) {
		
		Main.mainInterface.processor.trainingToggle ^= true;
		toggle = Main.mainInterface.processor.trainingToggle;
		if(toggle == true) Main.mainInterface.trainingMode.setText("BOT MODE");
		else Main.mainInterface.trainingMode.setText("USER MODE");
	}
}
