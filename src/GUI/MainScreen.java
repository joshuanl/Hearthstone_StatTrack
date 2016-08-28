package GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainScreen extends JFrame{
	private JPanel centerPanel;
	private JButton portraitButtonGrid[];
	private JButton playButton;
	private JButton viewStats;
	
	public MainScreen(){
		super("HearthStone Deck StatTrack");
		setLayout(new BorderLayout());
		setSize(900,700);
		setLocation(250,10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		setVisible(true);
	}//eo contructor\
	
	public static void main(String args[]){
		new MainScreen();
	}//end of main
}//end of class
