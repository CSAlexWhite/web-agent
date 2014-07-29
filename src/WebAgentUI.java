import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WebAgentUI extends JPanel implements ActionListener{
	
	JFrame mainWindow;
	
	JTextArea mainScreen;
	JTextArea conversationArea;
	JLabel actionLabel;
	JTextField inputField;

	String input;
	
	public WebAgentUI(){
		
		setLayout(new BorderLayout());
		
		actionLabel = new JLabel("");
		actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		
		inputField = new JTextField(10);
		//inputField.setActionCommand("What is this?");
		inputField.addActionListener(this);
						
		JLabel inputFieldLabel = new JLabel("Input: ");
		inputFieldLabel.setLabelFor(inputField);
				
		JPanel textControlsPane = new JPanel();
	        GridBagLayout gridbag = new GridBagLayout();
	        GridBagConstraints c = new GridBagConstraints();
	        
	    textControlsPane.setLayout(gridbag);
	    textControlsPane.setLayout(gridbag);

        JLabel[] labels = {inputFieldLabel};
        JTextField[] textFields = {inputField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);

        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        textControlsPane.add(actionLabel, c);
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                		BorderFactory.createTitledBorder("Text Fields"),
                		BorderFactory.createEmptyBorder(5,5,5,5)));
		
		conversationArea = new JTextArea();
		conversationArea.setLineWrap(true);
		conversationArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(conversationArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(250, 250));
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
		                BorderFactory.createCompoundBorder(
		                		BorderFactory.createTitledBorder("Conversation History"),
		                		BorderFactory.createEmptyBorder(5,5,5,5)),
		                areaScrollPane.getBorder()));
		
		 JPanel leftPane = new JPanel(new BorderLayout());
	        leftPane.add(textControlsPane, 
	                     BorderLayout.PAGE_START);
	        leftPane.add(areaScrollPane,
	                     BorderLayout.CENTER);

	        add(leftPane, BorderLayout.LINE_START);
		}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		input = this.inputField.getText();
		
		conversationArea.append(input + "\n");		
	}
	
    private void addLabelTextRows(JLabel[] labels, JTextField[] textFields,
        GridBagLayout gridbag, Container container){
    	
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.EAST;
			int numLabels = labels.length;
			
			for (int i = 0; i < numLabels; i++) {
				c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
				c.fill = GridBagConstraints.NONE;      //reset to default
				c.weightx = 0.0;                       //reset to default
				container.add(labels[i], c);
				
				c.gridwidth = GridBagConstraints.REMAINDER;     //end row
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1.0;
				container.add(textFields[i], c);
			}
    	}

}
