package GUI;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpInfoWindow extends JFrame{

	private JPanel centerPanel;
	private JTextArea jta;
	private JScrollPane scrollPane;
	
	public HelpInfoWindow(){
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(600,300);
		setLocation(500,250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		
		
		initVars();
		addComp();
		
		setVisible(true);
	}//eo constructor
	
	private void initVars() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		jta = new JTextArea();
		jta.setEditable(false);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		loadHelpText();
		
		scrollPane = new JScrollPane(jta);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	}//eo initvars
	
	private void addComp() {
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}//eo add comp

	private void loadHelpText(){
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader("src/Data/HelpInfo.txt");
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null){
				jta.append(line + "\n");
				line = br.readLine();
			}//eo while
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		
	}//eo of reading file info


}//eo class
