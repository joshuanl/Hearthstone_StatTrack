package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import Data.DeckData;

public class StatsWindow extends JFrame{

	private JPanel centerPanel;
	private JLabel heroPortraitLabel;
	private JLabel opponentPortraitLabel;
	private JLabel rankLabel;
	private JLabel winLabel;
	private JLabel lossLabel;
	private JLabel archTypeLabel;
	private ImageIcon heroPortraitImage;
	private ImageIcon opponentPortraitImage;
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
	private JComboBox<String> opponentHeroCB;
	private JMenu helpMenu;
	private JMenu windowMenu;
	private JMenuBar jmenubar;
	private JMenuItem helpMenuItem;
	private JMenuItem addDeckMenuItem;
	private JMenuItem addDeckTypeMenuItem;
	private JMenuItem addArchTypeMenuItem;
	private JMenuItem mainScreenMenuItem;
	private JTextArea helpTextArea;
	
	private String _deckList[];
	private ArrayList<String> archTypes;
	private int _rank;
	private int _winAmount;
	private int _lossAmount;
	private boolean _isLegendRank;
	
	
	public StatsWindow(int heroSelected){	//change to Stats obj later
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(400,245);
		setLocation(500,250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		//_heroSelected = heroSelected;	
		
		initVars(heroSelected);
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
		
		winLabel = new JLabel("WIN: " + _winAmount);
		winLabel.setFont(new Font("Serif", Font.BOLD, 18));
		winLabel.setAlignmentX(MAXIMIZED_HORIZ);
		winLabel.setAlignmentY(MAXIMIZED_VERT);
		winLabel.setSize(80, 45);
		winLabel.setLocation(120, 75); //120, 50
		
		lossLabel = new JLabel("LOSE: " + _lossAmount);
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
		
		archTypeLabel = new JLabel("ARCH TYPES: ");
		
		createDeckTypeButton = new JButton("Add Deck Type");
		createDeckTypeButton.setSize(115, 30);
		createDeckTypeButton.setLocation(0, 145);
		createDeckTypeButton.setFont(new Font("Serif", Font.BOLD, 12));
		createDeckTypeButton.setAlignmentX(CENTER_ALIGNMENT);
		
		addDeckArchTypeButton = new JButton("Add Arch Types");
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
				String userInput = JOptionPane.showInputDialog(StatsWindow.this, "Input DeckTpye", "Add New Deck", JOptionPane.QUESTION_MESSAGE);
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
							break;
						case JOptionPane.NO_OPTION:
							break;
					}//eo switch
				}//eo else if
			}
		});
		
		helpMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new HelpInfoWindow();
			}
		});
	}//eo addactions
	
	public void loadHelpTextFile(){
		
	}//end of loadHelpTextFile
	
	public boolean isDuplicate(String input, String array[]){
		for(int i=0; i < array.length; i++){
			if(array[i].equalsIgnoreCase(input)){
				return true;
			}//eo if
		}//eo for
		return false;
	}//eo method
	
	public boolean isDuplicate(String input, ArrayList<String> array){
		for(int i=0; i < array.size(); i++){
			if(array.get(i).equalsIgnoreCase(input)){
				return true;
			}//eo if
		}//eo for
		return false;
	}//eo method
	
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
		_deckList = new String[listSize+1];
		_deckList[0] = "NEW DECK";
		for(int i=1; i <= listSize; i++){
			try {
				deckName = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_deckList[i] = deckName;
		}
		
	}//eo loading decklist into combo box
	
	public void updateOpponentPortrait(int heroSelected){
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
	}//eo method
	
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
		
	}//eo opening help window
	
	private class ArchTypeWindow extends JFrame{
		
		public ArchTypeWindow(){
			super("Hearthstone Deck StatTracker");
			setLayout(null);
			setSize(400,200);
			setLocation(500,250);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			
			
			
//			initVars();
//			addActions();
//			addComp();
			
			setVisible(true);
		}//end of constructor
		
		
	}//end of inner class
}//eo class
