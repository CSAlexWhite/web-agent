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
import java.io.IOException;

import javax.print.attribute.standard.Sides;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class WebAgentUI extends JPanel implements ActionListener{
	
	JFrame mainWindow;
	
	JMenuBar menuBar;
	
	JTextArea mainScreen;
	JTextArea conversationArea;
	JLabel actionLabel;
	JTextField inputField;
	
	JButton trainingMode;
	JButton consoleMode;

	String input;
	
	InputListener processor;
	
	TrainingToggle mode;
	TrainingToggle console;
	
	public WebAgentUI(){
			
		mainWindow = Main.mainWindow;
		menuBar = new JMenuBar();
		createFileMenu();
				
		setLayout(new BorderLayout());
		processor = new InputListener(this);
		
		/* NEW ACTION EVENT LABEL (CURRENTLY INACTLIVE) */
		actionLabel = new JLabel("");
		actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		
		/* INPUT TEXT FIELD, AND ASSOCIATED LABEL FOR THE USER */
		inputField = new JTextField(10);
			inputField.setActionCommand("What is this?");
			inputField.addActionListener(processor);	
			
		JLabel inputFieldLabel = new JLabel("Input: ");
			inputFieldLabel.setLabelFor(inputField);
		
		/* CONVERSATION AREA WHICH OUTPUTS USER AND BOT RESPONSES */
		conversationArea = new JTextArea();
			conversationArea.setLineWrap(true);
			conversationArea.setFont(new Font("Consolas", Font.TRUETYPE_FONT, 16));
			conversationArea.setWrapStyleWord(true);
			conversationArea.setEditable(false);
			DefaultCaret caret = (DefaultCaret)conversationArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			conversationArea.setText("BOT->\tHi there!\n");
		
		/* SCROLL PANE: THE WRAPPER WITH SCROLL BAR FOR THE CONVERSATION AREA*/
		JScrollPane areaScrollPane = new JScrollPane(conversationArea);
			areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			areaScrollPane.setPreferredSize(new Dimension(500, 400));
			areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
		                BorderFactory.createCompoundBorder(
		                		BorderFactory.createTitledBorder("Conversation History"),
		                		BorderFactory.createEmptyBorder(5,5,5,5)),
		    areaScrollPane.getBorder()));
			
		/* BUTTONS AND TOGGLES FOR MODES, ETC.*/		
		trainingMode = new JButton("USER MODE");
		mode = new TrainingToggle(trainingMode);	
			trainingMode.addActionListener(mode);

		@SuppressWarnings("serial")
		JButton consoleMode = new JButton(new AbstractAction("CONSOLE MODE") {
	        public void actionPerformed( ActionEvent e ) {
	            Main.mainWindow.dispose();
	            try {Main.launchConsole();} 
	            catch (IOException e1) {}
	        }
	    });
			
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
                		BorderFactory.createTitledBorder(""),
                		BorderFactory.createEmptyBorder(10, 20, 20, 20)));
	    	
	    /* SET UP THE RIGHT SUB-JPANEL */	
	    	JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    trainingMode,
                    consoleMode);
				splitPane.setOneTouchExpandable(false);
				splitPane.setResizeWeight(0.5);
				JPanel rightPane = new JPanel(new GridLayout(4,0));
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
        
        displayWelcome();
	}
	

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
				c.gridwidth = GridBagConstraints.RELATIVE; 
				c.fill = GridBagConstraints.NONE;      
				container.add(labels[i], c);
				
				c.gridwidth = GridBagConstraints.REMAINDER;     
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1.0;
				container.add(textFields[i], c);
			}
    	}
    
    private void createFileMenu(){
    	
    	JMenuItem item;
    	JMenu fileMenu = new JMenu("FILE");
    	
    	FileMenuListener fml = new FileMenuListener(this, mainWindow);
    	
    	fileMenu.addSeparator();
    	
    	item = new JMenuItem("Print Conversation");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	fileMenu.addSeparator();
    	
    	item = new JMenuItem("Import Conversation");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	fileMenu.addSeparator();
    	
    	item = new JMenuItem("Quit");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	mainWindow.setJMenuBar(menuBar);
    	menuBar.add(fileMenu);  	
    }
    
    private void displayWelcome(){
    	
    	String t = 
    			"Hello and welcome to WebAgent, Version 0.1.1\n" +
    			"Your main activity here will be to chat with\n" +
    			"the bot.  Please, if you would like to chat with\n" + 
    			"him click the button that says \"User Mode\".\n" +
    			"It will switch now to \"Bot Mode\".  Remain in\n" + 
    			"training mode if you would like to input a\n" +  
    			"preselected course of conversation, in which you\n" +
    			"play both Sides. You can also load in a transcript\n" +
    			"using File/Import Conversation.  If you would like\n" +
    			"to save your banter, use File/Print Conversation.\n" + 
    			"If you would like to open a command line for more\n" + 
    			"advanced functions and debugging tools, press the\n" + 
    			"\"Console Mode\" button. Have fun!\n\n";
    	
    	conversationArea.setText(t);
    }
}
