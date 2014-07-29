import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TrainingToggle implements ActionListener {

	boolean toggle = true;
	
	public void actionPerformed(ActionEvent arg0) {
		
		toggle = Main.trainingToggle;
		toggle ^= true;
	}

}
