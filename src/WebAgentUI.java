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
		
		/* NEW ACTION EVENT LABEL (CURRENTLY INACTLIVE) */
		actionLabel = new JLabel("");
		actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		
		/* INPUT TEXT FIELD, AND ASSOCIATED LABEL FOR THE USER */
		inputField = new JTextField(10);
			inputField.setActionCommand("What is this?");
			inputField.addActionListener(this);		
			
		JLabel inputFieldLabel = new JLabel("Input: ");
			inputFieldLabel.setLabelFor(inputField);
		
		/* CONVERSATION AREA WHICH OUTPUTS USER AND BOT RESPONSES */
		conversationArea = new JTextArea();
			conversationArea.setLineWrap(true);
			conversationArea.setWrapStyleWord(true);
			conversationArea.setEditable(false);
		
		/* SCROLL PANE: THE WRAPPER WITH SCROLL BAR FOR THE CONVERSATION AREA*/
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
			
		/* BUTTONS AND TOGGLES FOR MODES, ETC.*/
		JButton trainingMode = new JButton("TRAINING MODE");
			trainingMode.addActionListener(new TrainingToggle());
		JButton importMode = new JButton("IMPORT MODE");
		
		/* SET UP THE LEFT SUB-JPANEL */
		JPanel textControlsPane = new JPanel();				
	    GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints gbcons = new GridBagConstraints();	        
	    	gbcons.gridwidth = GridBagConstraints.REMAINDER; 
	    	gbcons.anchor = GridBagConstraints.WEST;
	    	gbcons.weightx = 1.0;
	    	
	    	textControlsPane.setLayout(gridbag);
	    	textControlsPane.add(actionLabel, gbcons);
	    	textControlsPane.setBorder(
        		BorderFactory.createCompoundBorder(
                		BorderFactory.createTitledBorder("Text Fields"),
                		BorderFactory.createEmptyBorder(5,5,5,5)));
	    	
	    /* SET UP THE LEFT (CURRENTLY INACTIVE) SUB-JPANEL */	
	    	JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    trainingMode,
                    importMode);
				splitPane.setOneTouchExpandable(false);
				splitPane.setResizeWeight(0.5);
				JPanel rightPane = new JPanel(new GridLayout(1,0));
				rightPane.add(splitPane);
				rightPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("OPTIONS"),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
        
	    /* ADDS THE INPUT FIELD AND TEXT AREA TO THE LARGER PANE*/
        JPanel leftPane = new JPanel(new BorderLayout());
	        leftPane.add(textControlsPane, BorderLayout.PAGE_START);
	        leftPane.add(areaScrollPane, BorderLayout.CENTER);
	        
	    add(rightPane, BorderLayout.LINE_END);
	    add(leftPane, BorderLayout.LINE_START);
	        
	    /* SETS UP ALL LABELS TO BE APPLIED BY ONE METHOD ALL AT ONCE */
        JLabel[] labels = {inputFieldLabel};
        JTextField[] textFields = {inputField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);
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
