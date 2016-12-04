package GUI;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import Data.DeckData;

public class HistoryWindow extends JFrame{
	private JPanel centerPanel;
	private JTextArea jta;
	private JScrollPane scrollPane;
	private JTable matchDataTable;
	
	public HistoryWindow(String hN, String dN, String oppHN, String oppDT, DeckData dData){
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(600,300);
		setLocation(500,250);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	closeWindow();
            }

        });
		
		
		initVars();
		addComp();
		loadStats(hN,dN, oppHN, oppDT, dData);
		
		setVisible(true);
	}//eo constructor
	private void initVars() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		jta = new JTextArea();
		jta.setEditable(false);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		
		scrollPane = new JScrollPane(jta);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	}//eo initvars
	
	private void addComp() {
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}//eo add comp

	public void closeWindow(){
		this.dispose();
	}
	
	public void loadStats(String hN, String dN, String oppHN, String oppDT, DeckData dData){

		if(!oppDT.equals("NULL")){
			jta.setText("===================DATA AGAINST OPPONENT'S DECK TYPE==============================\n");
			jta.append(dN + "  VS  " + oppDT + "\n");
			jta.append("\tGAMES WON: " + dData.getMapStatsAgainstType().get(oppDT).getKey() + "\n");
			jta.append("\tGAMES LOST: " + dData.getMapStatsAgainstType().get(oppDT).getValue() + "\n");
			jta.append("\n");
			jta.append("==================================================================================\n");
			jta.append(dN + "  VS  " + oppHN + "\n");
			jta.append("\tGAMES WON: " + dData.getMapStatsAgainstHero().get(oppHN).getKey() + "\n");
			jta.append("\tGAMES LOST: " + dData.getMapStatsAgainstHero().get(oppHN).getValue() + "\n");
			jta.append("\n");
			jta.append("==================================================================================\n");
		}//eo if all data given
		else{
			jta.setText("============================DATA AGAINST HERO=====================================\n");
			jta.append(dN + "  VS  " + oppHN + "\n");
			jta.append("\tGAMES WON: " + dData.getMapStatsAgainstHero().get(oppHN).getKey() + "\n");
			jta.append("\tGAMES LOST: " + dData.getMapStatsAgainstHero().get(oppHN).getValue() + "\n");
			jta.append("\n");
			jta.append("==================================================================================\n");
		}
	}//eo loading stats to text area

}//eo class
