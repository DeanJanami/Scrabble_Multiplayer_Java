/*
 *  Dean Pakravan: 757389
 *  Assignment 2: Distributed Systems - Sem2 2018
 *  Class to hold the board of scrabble and its values
 */

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	private Container contentPane;
	public Board scrabbleBoard;
	public Player[] playerList;
	public int intCurrentPlayer;
	private JPanel eastPanel, westPanel, centerPanel;
	private JButton nextTurnBtn, passBtn, wordLTRBtn, wordTTBBtn, exitBtn, ruleBtn;
	private JTextField numPlayersField, letter;
	
	
	public GUI(){
		super("Scrabble");

//-------------------------------------------------------------------------------------------

//Window setup
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		
		scrabbleBoard = new Board();
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
//--------------------------------------------------------------------------------------------
		
//Area Setup		
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(20, 20, 0, 0));
		boardUpdate();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		
		eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(eastPanel, BorderLayout.EAST);
	
		westPanel = new JPanel();
		westPanel.setLayout(new GridBagLayout());
		contentPane.add(westPanel, BorderLayout.WEST);
		
		eastPanel.setBackground(Color.getHSBColor(0.567F, 0.96F, 0.1632F));
		westPanel.setBackground(Color.getHSBColor(0.567F, 0.96F, 0.1632F));
		centerPanel.setBackground(Color.getHSBColor(0.567F, 0.96F, 0.1632F));
		
		
		//Content setup		
		setBounds(0, 0, xSize, (ySize - 50));
		setVisible(true);
		repaint();
	}
	
//--------------------------------------------------------------------------------------------
	
//Listeners	

	// Listener for whenever we press the grid
	// All we want the tile press to do is highlight the green one
	private class tilePress implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ea) {
			// input is the location of the scrabble board
			JButton input =  (JButton) ea.getSource();
			for (int i = 0; i < centerPanel.getComponentCount(); i++) {
				if (centerPanel.getComponent(i).getBackground() != Color.CYAN) {
					centerPanel.getComponent(i).setBackground(Color.getHSBColor(0.147F, 0.17F, 0.14F));
				}
			}
			if (input.getBackground() != Color.CYAN) {
				input.setBackground(Color.green);
			}
			
		}
	}
	
	// Listener for whenever we add a letter to a tile on the grid
	private class LetterPress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Reset the passes
			for (int i = 0; i < playerList.length; i++) {
				playerList[i].addPass(0);
			}
			
			JButton input =  (JButton) e.getSource();
			try {
				String inputLetter = letter.getText();
			
				// Checking it is a letter from the alphabet
				char[] arr = inputLetter.toCharArray();
				int val = (int)arr[0];
				
				// Make sure we only add one letter
				if (inputLetter.length()!= 1) {
					JOptionPane optionPane = new JOptionPane("Enter only one letter!", JOptionPane.ERROR_MESSAGE);    
					JDialog dialog = optionPane.createDialog("Failure");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
				// Checking it is a letter from the alphabet
				else if(!((val >=65 && val <= 90) || (val >= 97 && val <= 122))) {
					JOptionPane optionPane = new JOptionPane("Enter one letter from the alphabet!", JOptionPane.ERROR_MESSAGE);    
					JDialog dialog = optionPane.createDialog("Failure");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
				else {
					char character = inputLetter.charAt(0);
					int j;
					for(j=0;j<centerPanel.getComponentCount();j++){
						JButton test = (JButton) centerPanel.getComponent(j);
						// This is where we retain the letter in the board
						if(test.getBackground() == Color.GREEN){
							scrabbleBoard.setSpace(j, Character.toUpperCase(character));
							letter.setEditable(false);
							// If we claim a RTL word
							if (input == wordLTRBtn) {
								VoteWordLTR(Character.toUpperCase(character), j);
							}
							else if (input == wordTTBBtn) {
								VoteWordTTB(Character.toUpperCase(character), j);
							}
							else {
								changePlayers();
							}
							break;
						}
					}
					if (j==centerPanel.getComponentCount()) {
						JOptionPane optionPane = new JOptionPane("Click a box on the grid first", JOptionPane.ERROR_MESSAGE);    
						JDialog dialog = optionPane.createDialog("Failure");
						dialog.setAlwaysOnTop(true);
						dialog.setVisible(true);
					}
					
				
				}
			// Catch if the player does not enter a letter
			} catch(ArrayIndexOutOfBoundsException e1) {
				JOptionPane optionPane = new JOptionPane("Please enter a letter", JOptionPane.ERROR_MESSAGE);    
				JDialog dialog = optionPane.createDialog("Failure");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
		}
	}
	

	// Method for when we choose how many players to start with, handles exceptions with dialog box
	private class InitialPlayers implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ea) {
			try{
				int numPlayers = Integer.parseInt(numPlayersField.getText());
				
				playerList = new Player[numPlayers];
				
				if(numPlayers<2){
					JOptionPane optionPane = new JOptionPane("More than one player is needed"
							, JOptionPane.ERROR_MESSAGE);    
					JDialog dialog = optionPane.createDialog("Failure");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
				else{
					for(int i=0; i < numPlayers; i++){
						playerList[i] = new Player();
						playerList[i].setId(i+1);

					}
					// start with player 0
					intCurrentPlayer = 0;;
					westPanelRedraw();
					eastPanelRedraw();

				}
			}
			catch(NumberFormatException ex){
				JOptionPane optionPane = new JOptionPane("Enter a number!", JOptionPane.ERROR_MESSAGE);    
				JDialog dialog = optionPane.createDialog("Failure");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}

		}
	}
	
	// Method if we pass turn
	// Also handles the case when ALL players pass - End game
	private class PassTurn implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			playerList[intCurrentPlayer].addPass(1);
			int pass = 0;
			// Check to see if all players pass
			for (int i = 0; i < playerList.length; i++) {
				if (playerList[i].getPass() != 0) {
					pass += 1;
				}
				else {
					break;
				}
			}
			// If all players pass consecutively, end game
			if (pass==playerList.length) {
				endGame(0);
				
			}
			// If not all players pass - move onto the next player
			else {
				changePlayers();
			}
		}
	}
	
	// Method to display the rules in a new JFrame
	private class displayRules implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
	      JFrame frame = new JFrame("Rules");

	      frame.getContentPane().setBackground(Color.getHSBColor(0.847F, 0.6F, 0.23F));
	      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      frame.setPreferredSize(new Dimension(700, 400));
	      
	      JLabel title = new JLabel("The Rulebook:");
	      title.setFont(new Font("Jokerman", Font.BOLD, 25));
	      title.setForeground(Color.RED);
	      frame.getContentPane().add(title, BorderLayout.NORTH);
	      
	      
	      
	      // <html> & <p> are Html coding operators that JLabel uses
	      // We use them to create a new line rather than several JLabels
	      JLabel coreGameInfo = new JLabel("<html><p>How the core game is played:</p><p>"
	      		+ "1). Click a tile</p><p>"
	      		+ "2). Then type a letter into the orange box</p><p>"
	      		+ "3). Press \"Next Player\" to add the letter to the board and change turns</p><p>"
	      		+ "4). Pass your turn if you cannot make a move</p><p>"
	      		+ "5). Continue until ALL players pass their turn consecutively.</p><p>"
	      		+ "6). The player with the highest score is the winner.</p></html>");
	      coreGameInfo.setFont(new Font("TimesNewRoman", Font.PLAIN, 15));
	      coreGameInfo.setForeground(Color.WHITE);
	      frame.getContentPane().add(coreGameInfo, BorderLayout.CENTER);
	      
	      JLabel scoringInfo = new JLabel("<html><p>Scoring:</p><p>"
		      		+ "->  Players can score in two ways.</p><p>"
		      		+ "1). By making a word that reads from left to right (LTR) across the board OR</p><p>"
		      		+ "2). By making a word that reads from top to bottom (TTB) along the board</p><p>"
		      		+ "->  Submit the correct directional button if your next letter"
		      		+ " will make a word.</p><p>"
		      		+ "->  All other players will have a chance to vote if "
		      		+ "they think the word is correct.</p><p>"
		      		+ "->  If majority of players agree, then the player recieves points "
		      		+ "based on the length of the word</p></html>");
	      scoringInfo.setFont(new Font("TimesNewRoman", Font.PLAIN, 15));
	      scoringInfo.setForeground(Color.WHITE);
	      frame.getContentPane().add(scoringInfo, BorderLayout.AFTER_LAST_LINE);
	      
	      frame.setLocation(50,50);
	      frame.pack();
	      frame.setVisible(true);
		   
		}
	}
	
	// If a player presses exit. This will need to be displayed to ALL playerss
	private class stopGame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			endGame(1);
		}
	}
//--------------------------------------------------------------------------------------------
	
//Functions
	
	// Update east, west and the board when we change players
	public void changePlayers() {
		if(intCurrentPlayer == playerList.length-1){
			intCurrentPlayer = 0;
		}
		else{
			intCurrentPlayer++;
		}
		westPanelRedraw();
		eastPanelRedraw();
		boardUpdate();	
	}
	
	// If the game ends
	// int end==0 if the game ends naturally
	// int end==1 if the game ends when a player presses exit
	public void endGame(int end) {
		JFrame frame = new JFrame("End Game");
		Box box = Box.createVerticalBox();
		frame.getContentPane().add(box);
		
		frame.getContentPane().setBackground(Color.getHSBColor(0.383F, 0.7879F, 0.2475F));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(400, 400));
		  
	    JLabel title = new JLabel("<html><p>The game has finished.</p><p>"
	    		+ " Thank you for playing!</p></html>");
	    title.setFont(new Font("SansSeriff", Font.BOLD, 20));
	    title.setForeground(Color.RED);
	    frame.getContentPane().add(title, BorderLayout.NORTH);
	
	    JLabel top5 = new JLabel("The Top 5 scores!");
	    top5.setFont(new Font("SansSeriff", Font.ITALIC, 15));
	    top5.setForeground(Color.MAGENTA);
	    box.add(top5);
	    

  	    // Print the top 5 scores.
	    int numScores = 5;
	    // If there are fewer than 5 players
	    if (numScores > playerList.length) {
		    numScores = playerList.length;
	    }
	  
	    Player[] sorted = playerList;
	  
	    for (int i = 0; i < playerList.length-1; i ++) {
	        int jmax = i;
		    for (int j = i+1; j < playerList.length; j++) {
			    if (playerList[j].getScore()>playerList[jmax].getScore()) {
				    jmax = j;
			    }
		    }
		    Player temp = sorted[jmax];
		    sorted[jmax] = sorted[i];
			sorted[i] = temp;	    
	    }
	  
	    
	    for (int i = 0; i < numScores; i ++) {
	        JLabel scoreInfo = new JLabel(i+1 + "). Player "
	  		  + sorted[i].getID() + " - Scored: " + sorted[i].getScore());
		      
		      scoreInfo.setFont(new Font("TimesNewRoman", Font.PLAIN, 15));
		      scoreInfo.setForeground(Color.WHITE);
		      box.add(scoreInfo);
		}
		
	  
	    frame.setLocation(50,50);
	    frame.pack();
	    frame.setVisible(true);
	    
	    passBtn.setEnabled(false);
	    wordLTRBtn.setEnabled(false);
	    wordTTBBtn.setEnabled(false);
	    nextTurnBtn.setEnabled(false);
	    exitBtn.setEnabled(false);
	    
		if (end==1) {
			Object[] options = {"Exit"};
			int x = JOptionPane.showOptionDialog(null, "A player has left the game. \n"
					+ "The game is over.", "End",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
			// x=0 if we close normally by pressing "exit"
			// x=-1 if we exit the JDialog box
			if (x == 0 || x == -1) {
				System.exit(0);
			}
		}
	}
	
	
	// When we update the board
	public void boardUpdate(){
		centerPanel.removeAll();
		char [][] tempBoard = scrabbleBoard.getObjBoard();
		for(int i = 0; i < tempBoard.length; i++)
		{
			for(int j = 0; j < tempBoard[i].length; j++)
			{
				JButton temp = new JButton();
				if(tempBoard[i][j] != 0)
				{
					temp.setText(tempBoard[i][j]+"");
					temp.setBackground(Color.CYAN);
				}
				else{
					temp.setBackground(Color.getHSBColor(0.147F, 0.17F, 0.14F));
				}
				temp.addActionListener(new tilePress());
				centerPanel.add(temp);
			}	
		}
		
		centerPanel.revalidate();
		centerPanel.repaint();
		repaint();
	}
	
	// Will need adjustments after RMI input, but okay for now
	public void startUp(){
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel askNumPlayers = new JLabel("Number of Players? ");
		askNumPlayers.setFont(new Font("Serif", Font.PLAIN, 15));
		askNumPlayers.setForeground(Color.WHITE);
		c.gridx = 0;
		c.gridy = 0;
		westPanel.add(askNumPlayers, c);
		
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		numPlayersField = new JTextField();
		numPlayersField.setBackground(Color.WHITE);
		westPanel.add(numPlayersField, c);
		numPlayersField.addActionListener(new InitialPlayers());

	}
	
	
	public void westPanelRedraw(){
		westPanel.removeAll();
		westPanel.setLayout(new GridLayout(0,1, 0, 0));
		contentPane.add(westPanel, BorderLayout.WEST);
		
		// Displaying the current player
		JLabel displayPlayer = new JLabel("Player " + (intCurrentPlayer+1) + "'s turn.");
		displayPlayer.setFont(new Font("SansSerif", Font.BOLD, 15));
		displayPlayer.setForeground(Color.WHITE);
		westPanel.add(displayPlayer);

		// Displaying player scores on the LHS - refresh after each turn
		for(int i=0; i<playerList.length; i++){
			JLabel displayScores = new JLabel("Player " + (i+1) + ": " + playerList[i].getScore(),
					JLabel.LEFT);
			displayScores.setFont(new Font("SansSerif", Font.PLAIN, 12));
			displayScores.setForeground(Color.WHITE);
			westPanel.add(displayScores);

			
		}
		
		nextTurnBtn = new JButton("Next Player");
		westPanel.add(nextTurnBtn);

		nextTurnBtn.setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		nextTurnBtn.setFont(new Font("Serif", Font.BOLD, 15));
		nextTurnBtn.setForeground(Color.white);
		
		nextTurnBtn.addActionListener(new LetterPress());
		westPanel.revalidate();
		westPanel.repaint();

	}
	
	public void eastPanelRedraw(){
		eastPanel.removeAll();
		eastPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		Font bFont = new Font("Serif", Font.BOLD, 15);
		
		passBtn = new JButton("Pass");
		c.weightx = 0.2;
		c.weighty = 1;
		c.gridy = 0;
		c.gridx = 1;
		eastPanel.add(passBtn, c);
		passBtn.addActionListener(new PassTurn());
		
		// RTL = Right to Left (word)
		wordLTRBtn = new JButton("Claim to have a word (LTR)");
		c.gridy = 1;
		c.gridx = 1;
		eastPanel.add(wordLTRBtn, c);
		wordLTRBtn.addActionListener(new LetterPress());
		
		// TTB = Top to Bottom (word)
		wordTTBBtn = new JButton("Claim to have a word (TTB)");
		c.gridy = 2;
		c.gridx = 1;
		eastPanel.add(wordTTBBtn, c);
		wordTTBBtn.addActionListener(new LetterPress());
		
		ruleBtn = new JButton("How to play (Rules)");
		c.gridy = 3;
		c.gridx = 1;
		eastPanel.add(ruleBtn, c);
		ruleBtn .addActionListener(new displayRules());
		
		JLabel enter = new JLabel("Enter a letter in the orange box");
		c.gridx = 1;
		c.gridy = 4;
		c.weighty = 0.1;
		enter.setFont(bFont);
		enter.setForeground(Color.white);
		eastPanel.add(enter, c);
		
		letter = new JTextField();
		c.weightx = 3;
		c.gridy = 5;
		c.gridx = 1;
		c.fill=GridBagConstraints.HORIZONTAL;
		// orange colour for fun
		letter.setBackground(Color.ORANGE);
		eastPanel.add(letter, c);
		letter.addActionListener(new LetterPress());
		
		exitBtn = new JButton("Exit");
		c.weightx = 0.2;
		c.weighty = 4;
		c.gridy = 6;
		c.gridx = 1;
		eastPanel.add(exitBtn, c);
		exitBtn .addActionListener(new stopGame());
	
		
		// Button customisation
		passBtn.setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		wordLTRBtn.setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		wordTTBBtn.setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		ruleBtn.setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		exitBtn.setBackground(Color.getHSBColor(0F, 0.96F, 0.59F));
		
		passBtn.setFont(bFont);
		wordLTRBtn.setFont(bFont);
		wordTTBBtn.setFont(bFont);
		ruleBtn.setFont(bFont);
		exitBtn.setFont(bFont);
		
		passBtn.setForeground(Color.white);
		wordLTRBtn.setForeground(Color.white);
		wordTTBBtn.setForeground(Color.white);
		ruleBtn.setForeground(Color.white);
		exitBtn.setForeground(Color.white);
				
		eastPanel.revalidate();
		eastPanel.repaint();
	}
	
	// These two methods will need to be done with RMI!
	// Voting when a player has a word from Top to Bottom
	public void VoteWordTTB(char letter, int j) {
		char ch;
		// starts at one since we input a new char
		int score = 1;
		// the word we vote it to be
		char[] temp = new char[20];
		temp[j/20] = letter;
		
		// We look at all the letters below our new word
		// While loop has two conditions to make sure we stay in bounds
		int bottom = j;
		while (((bottom-20) >= 0) && (ch = scrabbleBoard.getLetter(bottom-20))!='\0') {
			bottom -= 20;
			temp[bottom/20] = ch;
			score += 1;
		}
		
		// We look at all the letters above our new word
		int top = j;
		while (((top+20) < 400) && (ch = scrabbleBoard.getLetter(top+20))!='\0') {
			top += 20;
			temp[top/20] = ch;
			score += 1;
		}

		String word = new String(temp);

		int input = JOptionPane.showConfirmDialog(null, "Is " + word + " a word?", "Voting", 
				JOptionPane.YES_NO_OPTION);
		// All this does is add a value of 1 if the player votes yes, 0 of they vote no
		// It just does the value for the current player, not all. Will need to change
		if (input == JOptionPane.YES_OPTION) {
	        playerList[intCurrentPlayer].setVote(1);
	        playerList[intCurrentPlayer].setScore(playerList[intCurrentPlayer].getScore() + score);
	    }
        else {
        	playerList[intCurrentPlayer].setVote(0);
        }
		changePlayers();	
	}
	
	// Method if we find a word that is Left to Right
	public void VoteWordLTR(char letter, int j) {
		char ch;
		// the word we vote it to be
		int score = 1;
		char[] temp = new char[20];
		temp[j%20] = letter;

		// Look at the letters left of our new word
		// While loop has two conditions to make sure we stay in bounds
		int left = j;
		while (((left-1) > 0) && (left%20!=0) && (ch = scrabbleBoard.getLetter(left-1))!='\0') {
			left -= 1;
			temp[left%20] = ch;
			score += 1;
		}
		
		// Look at the letters right of our new word
		int right = j;
		while (((right+1) < 400) && ((right+1)%20!=0) && (ch = scrabbleBoard.getLetter(right+1))!='\0') {
			right += 1;
			temp[right%20] = ch;
			score += 1;
		}
		String word = new String(temp);

		int input = JOptionPane.showConfirmDialog(null, "Is " + word + " a word?", "Voting", 
				JOptionPane.YES_NO_OPTION);
		// All this does is add a value of 1 if the player votes yes, 0 of they vote no
		// It just does the value for the current player, not all. Will need to change
		if (input == JOptionPane.YES_OPTION) {
	        playerList[intCurrentPlayer].setVote(1);
	        playerList[intCurrentPlayer].setScore(playerList[intCurrentPlayer].getScore() + score);
	    }
        else {
        	playerList[intCurrentPlayer].setVote(0);
        }
		changePlayers();	
	}
	
}
