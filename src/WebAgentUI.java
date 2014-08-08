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

/**
 * A class which mainly puts together and instantiates GUI elements for the main
 * GUI interface.
 * @author Alex
 *
 */
public class WebAgentUI extends JPanel implements ActionListener{
	
	JFrame mainWindow;			// THE MAIN GUI CONTAINER
	InputListener processor;	// THE LISTENER FOR ALL RESPONSE ENTRIES
	
	JTextArea mainScreen;		// THE FIRST INTERIOR CONTAINER
	JTextArea conversationArea;	// WHERE THE CONVERSATION AND PROMPTS ARE OUTPUT
	JTextField inputField;		// WHERE THE USER ENTERS HIS NEXT RESPONSE
	
	JMenuBar menuBar;			// THE MENU BAR FOR ALL THE MENU OPTIONS
	JButton trainingMode;		// A BUTTON TO SWITCH BETWEEN CONVO MODES
	JButton consoleMode;		// A BUTTON TO SEND THE USER TO THE CONSOLE UI
	ButtonListener mode;		// A LISTENER FOR THE TRAINING TOGGLE
	ButtonListener console;		// A LISTENER FOR THE CONSOLE ACTIVATION
	
	JLabel actionLabel;			// A LABEL FOR THE TEXTAREA
	String input;				// THE CURRENT INPUT FROM THE INPUT FIELD
			
	/**
	 * This is the main method of the Graphical User Interface for WebAgent.  Its
	 * main purpose is to instantiate and bring together disparate GUI elements
	 * into one nice package, which is then displayed to the user.
	 */
	public WebAgentUI(){
			
		/* INSTANTIATE CONTAINERS*/
		mainWindow = Main.mainWindow;
		processor = new InputListener(this);
		menuBar = new JMenuBar();
		createFileMenu();
		
		/* SET THE LAYOUT MANAGER */
		setLayout(new BorderLayout());
				
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
		mode = new ButtonListener(trainingMode);	
			trainingMode.addActionListener(mode);

		/* GIVES THE CONSOLE BUTTON THE POWER TO CLOSE THE WINDOW */
		@SuppressWarnings("serial")
		JButton consoleMode = new JButton(new AbstractAction("CONSOLE MODE") {
	        public void actionPerformed( ActionEvent e ) {
	            Main.mainWindow.dispose();
	            try {Main.launchConsole();} 
	            catch (IOException e1) {}
	        }
	    });
			
		/* SET UP THE LEFT SUB-JPANEL, INCLUDING THE PARAMETERES FOR THE
		 * GRID BAG LAYOUT THAT'S IN USE, BORDERS, ETC. */
		JPanel textControlsPane = new JPanel();				
	    GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();	        
	    	constraints.gridwidth = GridBagConstraints.REMAINDER; 
	    	constraints.anchor = GridBagConstraints.WEST;
	    	constraints.weightx = 1.0;
	    	
	    	textControlsPane.setLayout(gridbag);
	    	textControlsPane.add(actionLabel, constraints);
	    	textControlsPane.setBorder(
        		BorderFactory.createCompoundBorder(
                		BorderFactory.createTitledBorder(""),
                		BorderFactory.createEmptyBorder(10, 20, 20, 20)));
	    	
	    /* SET UP THE RIGHT SUB-JPANEL, WHICH IS SIMPLER WITH ONLY BUTTONS */	
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
        
	    /* ADDS THE INPUT FIELD AND TEXT AREA TO THE WHOLE LEFT PANE*/
        JPanel leftPane = new JPanel(new BorderLayout());
	        leftPane.add(textControlsPane, BorderLayout.PAGE_START);
	        leftPane.add(areaScrollPane, BorderLayout.CENTER);
	    
	    /* ADD BOTH PANES TO THE WHOLE LAYOUT MANAGER */    
	    add(rightPane, BorderLayout.LINE_END);
	    add(leftPane, BorderLayout.LINE_START);
	        
	    /* SETS UP ALL LABELS TO BE APPLIED BY ONE METHOD ALL AT ONCE */
        JLabel[] labels = {inputFieldLabel};
        JTextField[] textFields = {inputField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);
        
        /* DISPLAY THE WELCOME MESSAGE, AFTER EVERYTHING IS DONE */
        displayWelcome();
	}
	
	/**
	 * A method to handle actions it's listening for, currently unused
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		input = this.inputField.getText(); 
		conversationArea.append(input + "\n");		
	}
	
	/**
	 * A method to put the right labels in the right places on the GUI
	 * @param labels
	 * @param textFields
	 * @param gridbag
	 * @param container
	 */
    private void addLabelTextRows(JLabel[] labels, JTextField[] textFields,
        GridBagLayout gridbag, Container container){
    	
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.anchor = GridBagConstraints.EAST;
			int numLabels = labels.length;
			
			/* GO THROUGH ALL THE LABELS AND PUT THEM IN THE RIGHT PLACES */
			for (int i = 0; i < numLabels; i++) {
				constraints.gridwidth = GridBagConstraints.RELATIVE; 
				constraints.fill = GridBagConstraints.NONE;      
				container.add(labels[i], constraints);
				
				constraints.gridwidth = GridBagConstraints.REMAINDER;     
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.weightx = 1.0;
				container.add(textFields[i], constraints);
			}
    	}
    
    /**
     * A method to instantiate and add menu items to the File Menu, here including
     * print and import conversations, and quit.
     */
    private void createFileMenu(){
    	
    	JMenuItem item;
    	JMenu fileMenu = new JMenu("FILE");
    	
    	/* FML IS THE FILE MENU LISTENER THAT KNOWS WHAT TO DO WITH ALL
    	 * THE FILE MENU COMMANDS, GO THERE FOR MORE INFORMATION */
    	FileMenuListener fml = new FileMenuListener(this, mainWindow);
    	
    	fileMenu.addSeparator();		// ADDS A GRAPHICAL SEPARATOR
    	
    	/* PRINT CONVERSATION OPTION */
    	item = new JMenuItem("Print Conversation");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	fileMenu.addSeparator();
    	
    	/* IMPORT CONVERSATION OPTION */
    	item = new JMenuItem("Import Conversation");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	fileMenu.addSeparator();
    	
    	/* QUIT OPTION */
    	item = new JMenuItem("Quit");
    	item.addActionListener(fml);
    	fileMenu.add(item);
    	
    	/* FINALLY, ADD EVERYTHING TO THE WHOLE PICTURE */
    	mainWindow.setJMenuBar(menuBar);
    	menuBar.add(fileMenu);  	
    }
    
    /**
     * A method that simply displays a Welcome method to the user when the GUI
     * is instantiated.
     */
    private void displayWelcome(){
    	
    	String t = 
    			"Hello, and welcome to WebAgent, Version 0.1.1\n" +
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
    			"advanced functions, debugging tools, and database\n" +
    			"queries, press the Console Mode\" button. Have fun!\n\n";
    	
    	conversationArea.setText(t);
    }
}
