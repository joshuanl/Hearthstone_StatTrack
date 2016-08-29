package GUI;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainScreen extends JFrame{
	private JPanel centerPanel;
	private JLabel pictureLabel;
	private JButton portraitButtonGrid[];
	private JButton playButton;
	private JButton viewStatsButton;
	private ImageIcon backgroundImage;
	
	public MainScreen(){
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(900,700);
		setLocation(250,10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		initVars();
		addActions();
		addComp();
		
		setVisible(true);
	}//eo contructor
	
	private void initVars(){
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(3,3));
		
		pictureLabel = new JLabel();
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("images/mainscreen.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Image dimg = img.getScaledInstance(900, 680,
		        Image.SCALE_SMOOTH);
		
		backgroundImage = new ImageIcon(dimg);
		pictureLabel.setIcon(backgroundImage);
		
		portraitButtonGrid = new JButton[9];
		for (int i = 0; i < 9; i++) {
			portraitButtonGrid[i] = new JButton("Hero.name");
			//add image icon to button
		}//end of outer for
		
		playButton = new JButton("Play");
		viewStatsButton = new JButton("View Stats");
	}//end of initializing variables
	
	private void addActions(){
		add(pictureLabel, BorderLayout.NORTH);
	}//end of adding actions
	
	private void addComp(){
		
	}//end of adding components
	
	public static void main(String args[]){
		new MainScreen();
	}//end of main
}//end of class
