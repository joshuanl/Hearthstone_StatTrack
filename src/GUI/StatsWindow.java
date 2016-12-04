package GUI;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.google.gson.Gson;

import Data.DeckData;
import Data.MatchData;
import Data.Stats;

public class StatsWindow extends JFrame implements Runnable{

	private JPanel centerPanel;
	private JLabel heroPortraitLabel;
	volatile private JLabel opponentPortraitLabel;
	private JLabel rankLabel;
	private JLabel winLabel;
	private JLabel lossLabel;
	private static JLabel archTypeLabel;
	private ImageIcon heroPortraitImage;
	volatile private ImageIcon opponentPortraitImage;
	private JButton addDeckButton;
	private JButton rankUpButton;
	private JButton rankDownButton;
	private JButton createDeckTypeButton;
	private JButton viewMatchupHistoryButton;
	private JButton addDeckArchTypeButton;
	private JButton gameFinishedButton;
	private JButton mainScreenButon;
	private JComboBox<String> currentDeckCB;
	private JComboBox<String> opponentDeckTypeCB;
	volatile private JComboBox<String> opponentHeroCB;
	private JMenu helpMenu;
	private JMenu windowMenu;
	private JMenuBar jmenubar;
	private JMenuItem helpMenuItem;
	private JMenuItem addDeckMenuItem;
	private JMenuItem addDeckTypeMenuItem;
	private JMenuItem addArchTypeMenuItem;
	private JMenuItem mainScreenMenuItem;
	private JTextArea helpTextArea;
	
	public static String _deckList[];
	protected static ArrayList<String> archTypes;
	protected static ArrayList<String> displayedArchTypes;
	private String _heroName;
	private int _heroSelected;
	private int _rank;
	private int _winAmount;
	private int _lossAmount;
	private boolean _isLegendRank;
	
	
	public StatsWindow(int heroSelected){	//change to Stats obj later
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(400,245);
		setLocation(500,250);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //SAVE EVERYTHING
            	new MainScreen();
            	closeWindow();
            }

        });
		
		_heroSelected = heroSelected;	
		
		initVars(heroSelected);
		loadArchTypes();
		addActions();
		addComp();
		
		setVisible(true);
	}//eo constructor
		
	public void initVars(int heroSelected){
		_rank = 25; 
		_winAmount = 10;
		_lossAmount = 7;
		_isLegendRank = false;
		archTypes = new ArrayList<String>();
		displayedArchTypes  = new ArrayList<String>();
		
		centerPanel = new JPanel();
		centerPanel.setLayout(null);
		
		
		heroPortraitLabel = new JLabel();
		heroPortraitLabel.setSize(115, 92);
		heroPortraitLabel.setLocation(0, 0);	//top left corner
		BufferedImage img = null;
		Image dimg = null;
		try {
		    img = ImageIO.read(new File(getPortraitFile(heroSelected)));
		} catch (IOException e) {
		    e.printStackTrace();
		}		
		dimg = img.getScaledInstance(115, 92,Image.SCALE_SMOOTH);	// 50% of original size	
		heroPortraitImage = new ImageIcon(dimg);
		heroPortraitLabel.setIcon(heroPortraitImage);
		
		opponentPortraitLabel = new JLabel();
		opponentPortraitLabel.setSize(115, 92);
		opponentPortraitLabel.setLocation(280, 85);	
		updateOpponentPortrait(1);								//DEBUGGING
		
		
		rankLabel = new JLabel("RANK - " + _rank);
		rankLabel.setFont(new Font("Serif", Font.BOLD, 22));
		rankLabel.setAlignmentX(CENTER_ALIGNMENT);
		rankLabel.setSize(120, 30);
		rankLabel.setLocation(120, 25);
			
		//==============================================	REMOVED	
		rankUpButton = new JButton("+");
		rankUpButton.setFont(new Font("Serif", Font.BOLD, 24));
		rankUpButton.setSize(80, 30);
		rankUpButton.setLocation(120, 55);
		
		rankDownButton = new JButton("-");
		rankDownButton.setFont(new Font("Serif", Font.BOLD, 24));
		rankDownButton.setSize(80, 30);
		rankDownButton.setLocation(200, 55);
		
		addDeckButton = new JButton("Edit Deck");
		addDeckButton.setFont(new Font("Serif", Font.BOLD, 16));
		addDeckButton.setSize(120, 25);
		addDeckButton.setLocation(280, 0);
		
		viewMatchupHistoryButton = new JButton("History");
		viewMatchupHistoryButton.setFont(new Font("Serif", Font.BOLD, 16));
		viewMatchupHistoryButton.setSize(120, 25);
		viewMatchupHistoryButton.setLocation(280, 25);
		
		winLabel = new JLabel("WON: " + _winAmount);
		winLabel.setFont(new Font("Serif", Font.BOLD, 18));
		winLabel.setAlignmentX(MAXIMIZED_HORIZ);
		winLabel.setAlignmentY(MAXIMIZED_VERT);
		winLabel.setSize(80, 45);
		winLabel.setLocation(120, 75); //120, 50
		
		lossLabel = new JLabel("LOST: " + _lossAmount);
		lossLabel.setFont(new Font("Serif", Font.BOLD, 18));
		lossLabel.setAlignmentX(MAXIMIZED_HORIZ);
		lossLabel.setAlignmentY(MAXIMIZED_VERT);
		lossLabel.setSize(80, 45);
		lossLabel.setLocation(200, 75);	//200, 50
		
		gameFinishedButton = new JButton("Game Ended");
		gameFinishedButton.setFont(new Font("Serif", Font.BOLD, 15));
		gameFinishedButton.setSize(120, 35);
		gameFinishedButton.setLocation(280, 50);
		
		String heroList[] = {"Choose Opponent","DRUID","HUNTER","MAGE","PALADIN","PRIEST","ROGUE","SHAMAN", "WARLOCK","WARRIOR"};
		opponentHeroCB = new JComboBox<String>(heroList);
		opponentHeroCB.setSize(165, 30);
		opponentHeroCB.setLocation(115, 115);
		opponentHeroCB.setFont(new Font("Serif", Font.BOLD, 15));
		((JLabel)opponentHeroCB.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		//============================================== REDO TO USE JSON FILES
		loadDeckList(heroSelected);
		currentDeckCB = new JComboBox<String>(_deckList);
		currentDeckCB.setSize(160, 25);
		currentDeckCB.setLocation(120, 0);
		currentDeckCB.setFont(new Font("Serif", Font.BOLD, 11));
		
		opponentDeckTypeCB = new JComboBox<String>(DeckData.getDeckTypesArray());
		opponentDeckTypeCB.setSize(115, 30);
		opponentDeckTypeCB.setLocation(0, 115);
		opponentDeckTypeCB.setFont(new Font("Serif", Font.BOLD, 12));
		((JLabel)opponentDeckTypeCB.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		//==============================================
		
		archTypeLabel = new JLabel("ARCHTYPES: ");
		
		createDeckTypeButton = new JButton("Add Deck Type");
		createDeckTypeButton.setSize(115, 30);
		createDeckTypeButton.setLocation(0, 145);
		createDeckTypeButton.setFont(new Font("Serif", Font.BOLD, 12));
		createDeckTypeButton.setAlignmentX(CENTER_ALIGNMENT);
		
		addDeckArchTypeButton = new JButton("Set Arch Types");
		addDeckArchTypeButton.setSize(165, 30);
		addDeckArchTypeButton.setLocation(115, 145);
		addDeckArchTypeButton.setFont(new Font("Serif", Font.BOLD, 15));
		addDeckArchTypeButton.setAlignmentX(CENTER_ALIGNMENT);
		
		jmenubar = new JMenuBar();
		windowMenu = new JMenu("Window");
		helpMenu = new JMenu("Help");
		
		helpMenuItem = new JMenuItem("Help");
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
		helpMenuItem.setMnemonic('H');
		
		addDeckMenuItem = new JMenuItem("Add Deck");
		addDeckMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.CTRL_MASK));
		addDeckMenuItem.setMnemonic('D');
		
		addDeckTypeMenuItem = new JMenuItem("Add Deck Type");
		addDeckTypeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,ActionEvent.CTRL_MASK));
		addDeckTypeMenuItem.setMnemonic('T');
		
		addArchTypeMenuItem = new JMenuItem("Add Arch Type");
		addArchTypeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		addArchTypeMenuItem.setMnemonic('A');
		
		mainScreenMenuItem = new JMenuItem("Back To Main Screen");
		mainScreenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));
		mainScreenMenuItem.setMnemonic('B');

		//helpTextArea = loadHelpTextFile();
	}//eo initvars
	
	public void addActions(){
		rankUpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				_rank++;
				if(_rank > 25 && !_isLegendRank){
					_rank = 25;
					rankLabel.setText("RANK - " + _rank);
				}
				else if(_rank > 100 && _isLegendRank){
					int stillLegend =  JOptionPane.showConfirmDialog(StatsWindow.this, "Still Legend Rank?", "Confirmation", JOptionPane.YES_NO_OPTION);
					System.out.println("choice: " + stillLegend);
					System.out.println(JOptionPane.YES_OPTION);
					switch(stillLegend){
						case JOptionPane.YES_OPTION:
							_rank = Integer.parseInt(JOptionPane.showInputDialog(null, "Input Legends Rank"));
							rankLabel.setText("RANK - L " + _rank);
							break;
						case JOptionPane.NO_OPTION:
							_rank = 1;
							rankLabel.setText("RANK - " + _rank);
							rankLabel.setFont(new Font("Serif", Font.BOLD, 26));
							_isLegendRank = false;
							break;	
					}//eo switch	
				}//eo else if	
				else if(_isLegendRank){
					rankLabel.setText("RANK - L " + _rank);
				}
				else{
					rankLabel.setText("RANK - " + _rank);
				}
			}//eo action performed
		});
		rankDownButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				_rank--;
				if(_rank < 1 && !_isLegendRank){
					_isLegendRank = true;
					_rank = Integer.parseInt(JOptionPane.showInputDialog(null, "Input Legends Rank"));
					rankLabel.setText("RANK - L " + _rank);
					rankLabel.setFont(new Font("Serif", Font.BOLD, 20));
				}
				else if (_rank < 1 && _isLegendRank){
					_rank = 1;
					rankLabel.setText("RANK - L " + _rank);
				}
				else if (_isLegendRank && _rank > 100){
					_rank = Integer.parseInt(JOptionPane.showInputDialog(null, "Input Legends Rank"));
					rankLabel.setText("RANK - L " + _rank);
				}
				else if(_isLegendRank){
					rankLabel.setText("RANK - L " + _rank);
				}
				else{
					rankLabel.setText("RANK - " + _rank);
				}
				
			}
		});
		
		addDeckButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(currentDeckCB.getSelectedItem().toString().equals("NEW DECK")){
					newDeckDialoge();
				}
				else{
					//OPEN EDIT DECK WINDOW
					System.out.println("OPEN EDIT DECK WINDOW");
				}
			}
		});
		
		helpMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new HelpInfoWindow();
			}
		});
		
		addDeckArchTypeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new Thread(new ArchTypeWindow()).start();
			}
		});
		
		gameFinishedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(currentDeckCB.getSelectedItem().toString().equals("NEW DECK")){
			    	JOptionPane.showMessageDialog(null, "No deck selected for player! ", "GAME NOT RECORDED", JOptionPane.ERROR_MESSAGE);
			    	return;
				}
				else if(opponentHeroCB.getSelectedItem().toString().equals("Choose Opponent")){
					JOptionPane.showMessageDialog(null, "Opponent Hero not set! ", "GAME NOT RECORDED", JOptionPane.ERROR_MESSAGE);
			    	return;
				}
				else if(opponentDeckTypeCB.getSelectedItem().toString().equals("DECK TYPE")){
					JOptionPane.showMessageDialog(null, "No deck type selected for opponent! ", "GAME NOT RECORDED", JOptionPane.ERROR_MESSAGE);
			    	return;
				}
				Object [] options = {"Won", "Lost"};
				int value = JOptionPane.showOptionDialog(StatsWindow.this,
				"Outcome of the game?",
				"Recording Game Outcome",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, // icon to display
				options,
				options[0]);
				String hN = getHeroName(_heroSelected);
				String dN = currentDeckCB.getSelectedItem().toString();
				String oN = getHeroName(opponentHeroCB.getSelectedIndex());
				String oDT = opponentDeckTypeCB.getSelectedItem().toString(); 
//=================================================NEED TO SET dA IN EDIT DECK BUTTON 						
				ArrayList<String> dA = new  ArrayList<String>();
				ArrayList<String>oDA = displayedArchTypes;
				String r = rankLabel.getText();
				int gW = _winAmount;
				int gL = _lossAmount;
				MatchData matchData;
				Map<String, Map<String, DeckData>> data = MainScreen.allStats.getData();	//added for readability of next lines
				Map<String, DeckData> deckMap = data.get(hN);
				DeckData dData = deckMap.get(dN);
				if(dData == null){
					System.out.println("dData is null");
				}
				
				switch(value){
					case JOptionPane.YES_OPTION:
						matchData = new MatchData(hN, oN, dN, oDT, new ArrayList<String>(), oDA, gW, gL, r, true);				
						dData.addMatch(matchData);
						deckMap.put(dN, dData);
						MainScreen.allStats.data.put(hN, deckMap);
				    	JOptionPane.showMessageDialog(null, "Game Recorded!  Be sure to reset information for the next game! ", "GAME RECORDED", JOptionPane.INFORMATION_MESSAGE);
				    	_winAmount++;
				    	winLabel.setText("WON: " + _winAmount);
						break;
					case JOptionPane.NO_OPTION:
						matchData = new MatchData(hN, oN, dN, oDT, new ArrayList<String>(), oDA, gW, gL, r, false);
						dData.addMatch(matchData);
						deckMap.put(dN, dData);
						MainScreen.allStats.data.put(hN, deckMap);
				    	JOptionPane.showMessageDialog(null, "Game Recorded!  Be sure to reset information for the next game! ", "GAME RECORDED", JOptionPane.INFORMATION_MESSAGE);
				    	_lossAmount++;
				    	lossLabel.setText("LOST: " + _lossAmount);
						break;
				}//eo switch
				data.put(hN, deckMap);
			}
		});
		
		viewMatchupHistoryButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String hN = getHeroName(_heroSelected);
				String dN = currentDeckCB.getSelectedItem().toString();
				String oN = getHeroName(opponentHeroCB.getSelectedIndex());
				String oDT = opponentDeckTypeCB.getSelectedItem().toString(); 
				Map<String, Map<String, DeckData>> data = MainScreen.allStats.getData();	//added for readability of next lines
				Map<String, DeckData> deckMap = data.get(hN);
				DeckData dData = deckMap.get(dN);
				if(currentDeckCB.getSelectedItem().toString().equals("NEW DECK")){
			    	JOptionPane.showMessageDialog(null, "No deck selected for player! ", "CANT VIEW HISTORY", JOptionPane.ERROR_MESSAGE);
			    	return;
				}
				else if(opponentHeroCB.getSelectedItem().toString().equals("Choose Opponent")){
					JOptionPane.showMessageDialog(null, "Opponent Hero not set! ", "CANT VIEW HISTORY", JOptionPane.ERROR_MESSAGE);
			    	return;
				}
				else if(opponentDeckTypeCB.getSelectedItem().toString().equals("DECK TYPE")){
					oDT = "NULL";
				}
				
				new HistoryWindow(hN, dN, oN, oDT, dData);
			}
		});
		
		createDeckTypeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String userInput = JOptionPane.showInputDialog(StatsWindow.this, "Input deck type", "Add New Deck Type", JOptionPane.QUESTION_MESSAGE);
				if(isDuplicate(userInput, DeckData.getDeckTypesArray())){
					JOptionPane.showMessageDialog(StatsWindow.this, "Duplicate deck type entered!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}//eo if
				else{
					int userChoice = -1;
					try{
						userChoice = JOptionPane.showConfirmDialog(StatsWindow.this, "Add the deck: \""+ userInput.toUpperCase() + "\"?", "Add New Deck", JOptionPane.YES_NO_OPTION);
					}catch(NullPointerException npe){
						System.out.println("NPE: " + npe.getMessage());
					}
					
					switch(userChoice){
						case JOptionPane.YES_OPTION:
							String newList[] = new String[DeckData.getDeckTypesArray().length+1];
							for(int i=0; i < DeckData.getDeckTypesArray().length; i++){
								newList[i] = DeckData.getDeckTypesArray()[i];
							}//eo for
							newList[DeckData.getDeckTypesArray().length] = userInput.toUpperCase();
							DeckData.newDeckTypes(newList);
							DeckData.saveDeckTypes();
							opponentDeckTypeCB.addItem(userInput.toUpperCase());
							opponentDeckTypeCB.setFont(new Font("Serif", Font.BOLD, 11));
							
							break;
						case JOptionPane.NO_OPTION:
							break;
					}//eo switch
				}//eo else if
			}
		});
	}//eo addactions
	
	public void addComp(){
		centerPanel.add(heroPortraitLabel);
		centerPanel.add(opponentPortraitLabel);
		centerPanel.add(rankLabel);
		centerPanel.add(addDeckButton);
		centerPanel.add(viewMatchupHistoryButton);
		centerPanel.add(winLabel);
		centerPanel.add(lossLabel);
		centerPanel.add(rankUpButton);
		centerPanel.add(rankDownButton);
		centerPanel.add(gameFinishedButton);
		centerPanel.add(createDeckTypeButton);
		centerPanel.add(addDeckArchTypeButton);	
		centerPanel.add(currentDeckCB);
		centerPanel.add(opponentHeroCB);
		centerPanel.add(opponentDeckTypeCB);
		
		helpMenu.add(helpMenuItem);
		windowMenu.add(addDeckMenuItem);
		windowMenu.add(addDeckTypeMenuItem);
		windowMenu.add(addArchTypeMenuItem);
		windowMenu.add(mainScreenMenuItem);
		jmenubar.add(helpMenu);
		jmenubar.add(windowMenu);

		setJMenuBar(jmenubar);
		add(centerPanel, BorderLayout.CENTER);
		add(archTypeLabel, BorderLayout.SOUTH);
	}//eo addcomp

	
	public void newDeckDialoge(){
		String userInput = JOptionPane.showInputDialog(StatsWindow.this, "Input deck name", "Add New Deck", JOptionPane.QUESTION_MESSAGE);
		if(isDuplicate(userInput, _deckList)){
			JOptionPane.showMessageDialog(StatsWindow.this, "Duplicate deck name entered!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}//eo if
		else{
			int userChoice = -1;
			try{
				userChoice = JOptionPane.showConfirmDialog(StatsWindow.this, "Add the deck: \""+ userInput.toUpperCase() + "\"?", "Add New Deck", JOptionPane.YES_NO_OPTION);
			}catch(NullPointerException npe){
				System.out.println("NPE: " + npe.getMessage());
			}
			
			switch(userChoice){
				case JOptionPane.YES_OPTION:
					String newList[] = new String[_deckList.length+1];
					for(int i=0; i < _deckList.length; i++){
						newList[i] = _deckList[i];
					}//eo for
					newList[_deckList.length] = userInput.toUpperCase();
					_deckList = newList;
					currentDeckCB.addItem(userInput.toUpperCase());
					currentDeckCB.setFont(new Font("Serif", Font.BOLD, 11));
					
					Map<String, Map<String, DeckData>> data = MainScreen.allStats.data;	//added for readability of next lines
					Map<String, DeckData> deckMap = data.get(getHeroName(_heroSelected));
					deckMap.put(userInput.toUpperCase(), new DeckData(userInput.toUpperCase(), getHeroName(_heroSelected)));
					saveDeckList();
					MainScreen.allStats.data.put(getHeroName(_heroSelected), deckMap);
					break;
				case JOptionPane.NO_OPTION:
					break;
			}//eo switch
		}//eo else if
	}//eo newDeckDialoge()
	
	public static void updateDisplayedArchTypes(){
		archTypeLabel.setText("ARCHTYPES: ");
		for (int i = 0; i < displayedArchTypes.size(); i++) {
			if(i == displayedArchTypes.size()-1){
				archTypeLabel.setText(archTypeLabel.getText() + displayedArchTypes.get(i));
			}
			else{
				archTypeLabel.setText(archTypeLabel.getText() + displayedArchTypes.get(i) + ", ");
			}
			
		}//eo for
	}//eo updateDisplayedArchTypes
	
	public String getHeroName(int index){
		String heroName = "NULL";
		switch(index){
		case 0:
			heroName = "WARRIOR";
			break;
		case 1:
			heroName = "SHAMAN";
			break;
		case 2:
			heroName = "ROGUE";
			break;
		case 3:
			heroName = "PALADIN";
			break;
		case 4:
			heroName = "HUNTER";
			break;
		case 5:
			heroName = "DRUID";
			break;
		case 6:
			heroName = "WARLOCK";
			break;
		case 7:
			heroName = "MAGE";
			break;
		case 8:
			heroName = "PRIEST";
			break;
		}//eo switch
		return heroName;
	}//eo getting hero name from index
	
	public static void loadArchTypes(){
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader("src/Data/archTypeDemo.txt");
			br = new BufferedReader(fr);
			String deckType = br.readLine();
			while(deckType != null){
				archTypes.add(deckType);
				deckType = br.readLine();
			}//eo while
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
	}//end of loadHelpTextFile
	
	public static boolean isDuplicate(String input, String array[]){
		for(int i=0; i < array.length; i++){
			if(array[i].equalsIgnoreCase(input)){
				return true;
			}//eo if
		}//eo for
		return false;
	}//eo method
	
	public static boolean isDuplicate(String input, ArrayList<String> array){
		for(int i=0; i < array.size(); i++){
			if(array.get(i).equalsIgnoreCase(input)){
				return true;
			}//eo if
		}//eo for
		return false;
	}//eo method
	
	public void loadDeckList(int heroSelected){
		int listSize = 0;
		String deckName = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("src/data/testDeckList.txt"));
			deckName = br.readLine();
			if(deckName != null){
				listSize = Integer.parseInt(deckName);
			}
			else{
				System.out.println("File is empty");
			}
		} catch (FileNotFoundException e) {
			System.out.println("FNE: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOE: " + e.getMessage());
		}
		_deckList = new String[listSize];
		//_deckList[0] = "NEW DECK";
		for(int i=0; i < listSize; i++){
			try {
				deckName = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_deckList[i] = deckName;
			
		}//eo for
		
	}//eo loading decklist into combo box
	
	public static void saveDeckList(){
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			fw = new FileWriter("src/Data/testDeckList.txt");
			pw = new PrintWriter(fw);
			pw.println(_deckList.length);
			for(int i=0; i < _deckList.length; i++){
				pw.println(_deckList[i]);
			}
			
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		finally{
			pw.close();
			//fw.close();
		}
	}
	
	public void updateOpponentPortrait(int heroSelected){
		if(heroSelected  >= 0){
			BufferedImage img = null;
			Image dimg = null;
			try {
			    img = ImageIO.read(new File(getPortraitFile(heroSelected)));
			} catch (IOException e) {
			    e.printStackTrace();
			}		
			dimg = img.getScaledInstance(115, 92,Image.SCALE_SMOOTH);	// 50% of original size	
			opponentPortraitImage = new ImageIcon(dimg);
			opponentPortraitLabel.setIcon(opponentPortraitImage);
			System.out.println("updated opponent portrait");
		}
		
	}//eo method
	
	public String getPortraitFile(String hN){
		String fileName = "";
		if(hN.equals("WARRIOR")){
			fileName = "images/WarriorPortrait.png";
		}
		else if(hN.equals("SHAMAN")){
			fileName = "images/ShamanPortrait.png";
		}
		else if(hN.equals("ROGUE")){
			fileName = "images/RoguePortrait.png";
		}
		else if(hN.equals("PALADIN")){
			fileName = "images/PaladinPortrait.png";
		}
		else if(hN.equals("HUNTER")){
			fileName = "images/HunterPortrait.png";
		}
		else if(hN.equals("DRUID")){
			fileName = "images/DruidPortrait.png";
		}
		else if(hN.equals("WARLOCK")){
			fileName = "images/WarlockPortrait.png";
		}
		else if(hN.equals("MAGE")){
			fileName = "images/MagePortrait.png";
		}
		else if(hN.equals("PRIEST")){
			fileName = "images/PriestPortrait.png";
		}
		
		return fileName;
	}
	
	public String getPortraitFile(int index){
		String fileName = "";
		switch(index){
			case 0:
				fileName = "images/WarriorPortrait.png";
				break;
			case 1:
				fileName = "images/ShamanPortrait.png";
				break;
			case 2:
				fileName = "images/RoguePortrait.png";
				break;
			case 3:
				fileName = "images/PaladinPortrait.png";
				break;
			case 4:
				fileName = "images/HunterPortrait.png";
				break;
			case 5:
				fileName = "images/DruidPortrait.png";
				break;
			case 6:
				fileName = "images/WarlockPortrait.png";
				break;
			case 7:
				fileName = "images/MagePortrait.png";
				break;
			case 8:
				fileName = "images/PriestPortrait.png";
				break;
		}//eo switch
		return fileName;
	}//eo method
	
	public static void openHelpWindow(){
		System.out.println("need to finish");
	}//eo opening help window

	public void closeWindow(){
		
		Gson gs = new Gson();
		String jsonString = gs.toJson(MainScreen.allStats.data);
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			fw = new FileWriter("src/Data/allstats.json");
			pw = new PrintWriter(fw);
			pw.println(jsonString);
			
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		finally{
			pw.close();
			//fw.close();
		}		
		this.dispose();
	}//eo closing window
	
	public void run(){
		String fileName = "";
		String oppHero;
		while(true){
			oppHero = opponentHeroCB.getSelectedItem().toString();
			if(!oppHero.equals("Choose Opponent")){
				fileName = getPortraitFile(oppHero);
				BufferedImage img = null;
				Image dimg = null;
				try {
				    img = ImageIO.read(new File(fileName));
				} catch (IOException e) {
				    e.printStackTrace();
				}		
				dimg = img.getScaledInstance(115, 92,Image.SCALE_SMOOTH);	// 50% of original size	
				opponentPortraitImage = new ImageIcon(dimg);
				opponentPortraitLabel.setIcon(opponentPortraitImage);
			}
			
		}//eo while
	}//end of run method
	
}//eo class
