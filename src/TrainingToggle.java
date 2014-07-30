import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrainingToggle implements ActionListener {

	boolean toggle;
	
	public TrainingToggle(){}
	
	public void actionPerformed(ActionEvent arg0) {
		
		Main.mainInterface.processor.trainingToggle ^= true;
		toggle = Main.mainInterface.processor.trainingToggle;
		if(toggle == true) Main.mainInterface.trainingMode.setText("BOT MODE");
		else Main.mainInterface.trainingMode.setText("USER MODE");
	}
}
