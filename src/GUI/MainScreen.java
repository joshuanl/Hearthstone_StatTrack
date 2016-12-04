package GUI;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Data.DeckData;
import Data.Stats;

public class MainScreen extends JFrame{
	static Stats allStats;
	private JPanel centerPanel;
	private JLabel pictureLabel;
	private JLabel portraitSelectedLabel;
	private JButton portraitButtonGrid[][];
	private JButton playButton;
	private JButton viewStatsButton;
	private ImageIcon backgroundImage;
	private ImageIcon portraitSelectedImage;
	
	private int _defaultWidth = 900;
	private int _defaultHeight = 700;
	private int _buttonWidth = 180;
	private int _buttonHeight = 65;
	private int _buttonLocationOffsetX = 225;
	private int _buttonLocationOffsetY = 150;
	private int _selectionBoarderX = 105;
	private int _selectionBoarderY = 110;
	private int _selectionBoarderOffsetX = 230;
	private int _selectionBoarderOffsetY = 150;
	private int _heroSelected;
	
	public MainScreen(){
		super("Hearthstone Deck StatTracker");
		setLayout(null);
		setSize(900,700);
		setLocation(250,10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		allStats = new Stats();
		
		initVars();
		addActions();
		addComp();
		
		setVisible(true);
		DeckData.loadDeckTypes();
		DeckData.alphabetizeDeckTypes();
		DeckData.saveDeckTypes();
	}//eo contructor
	
	private void initVars(){
		_heroSelected = 4;
		centerPanel = new JPanel();
		centerPanel.setLayout(null);
		centerPanel.setOpaque(false);
		centerPanel.setSize(_defaultWidth, _defaultHeight);
		
		
		pictureLabel = new JLabel();
		pictureLabel.setSize(_defaultWidth, _defaultHeight);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("images/mainscreen.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}		
		Image dimg = img.getScaledInstance(900, 680,Image.SCALE_SMOOTH);		
		backgroundImage = new ImageIcon(dimg);
		pictureLabel.setIcon(backgroundImage);
		
		portraitSelectedLabel = new JLabel();
		portraitSelectedLabel.setSize(235, 170);
		img = null;
		try {
		    img = ImageIO.read(new File("images/portrait_selection_boarder.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}		
		dimg = img.getScaledInstance(235, 120,Image.SCALE_SMOOTH);		
		portraitSelectedImage = new ImageIcon(dimg);
		portraitSelectedLabel.setIcon(portraitSelectedImage);
		
		
		portraitButtonGrid = new JButton[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				portraitButtonGrid[i][j] = new JButton();
				portraitButtonGrid[i][j].setSize(_buttonWidth, _buttonHeight);
				//130 & 170 are the pos of the first button
				portraitButtonGrid[i][j].setLocation(131 + j*_buttonLocationOffsetX, 170 + i*_buttonLocationOffsetY);
				//System.out.println("buttonLoc: " + portraitButtonGrid[i][j].getLocation().x + ", " + portraitButtonGrid[i][j].getLocation().y);
				portraitButtonGrid[i][j].setOpaque(false);
				portraitButtonGrid[i][j].setContentAreaFilled(false);
				portraitButtonGrid[i][j].setBorderPainted(false);
				
			}
			//add image icon to button
		}//end of outer for
		
		playButton = new JButton();
		playButton.setOpaque(false);
		playButton.setContentAreaFilled(false);
		playButton.setBorderPainted(false);
		playButton.setSize(275, 13);
		playButton.setLocation(315, 26);
		viewStatsButton = new JButton();
		viewStatsButton.setOpaque(false);
		viewStatsButton.setContentAreaFilled(false);
		viewStatsButton.setBorderPainted(false);
		viewStatsButton.setSize(225, 10);
		viewStatsButton.setLocation(340, 650);
	}//end of initializing variables
	
	private void addActions(){
		
		
		pictureLabel.addMouseListener(new MouseAdapter() {
		      public void mouseClicked(MouseEvent e) {
		        System.out.println(e.getX() + ", " + e.getY());
		      }
		    });
	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int x = j;
				int y = i;
				portraitButtonGrid[i][j].addActionListener(new ActionListener(){				
					public void actionPerformed(ActionEvent ae){
						
						System.out.println("Button pressed");
						portraitSelectedLabel.setLocation(_selectionBoarderX + x*_selectionBoarderOffsetX, _selectionBoarderY + y*_selectionBoarderOffsetY);
						_heroSelected = (y*3)+x;
					}
				});
			}//end of inner for
		}//end of outer for	
		
		playButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				new StatsWindow(_heroSelected);
				closeWindow();
			}
		});
	}//end of adding actions
	
	private void addComp(){
		add(pictureLabel, 0,0);
		add(portraitSelectedLabel, 0);
		portraitSelectedLabel.setLocation(_selectionBoarderX + _selectionBoarderOffsetX, _selectionBoarderY + _selectionBoarderOffsetY);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				centerPanel.add(portraitButtonGrid[i][j]);
			}//end of inner for
		}//end of outer for	
		centerPanel.add(playButton);
		centerPanel.add(viewStatsButton);
		add(centerPanel, 0,0);
	}//end of adding components
	
	public void closeWindow(){
		this.dispose();
	}
	
	public static void main(String args[]){
		new MainScreen();
	}//end of main
}//end of class
