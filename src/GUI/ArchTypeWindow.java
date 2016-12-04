package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ArchTypeWindow extends JFrame implements Runnable{
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private JPanel bottomCenterPanel;
	private JLabel descriptionLabel;
	private JButton addArchtypeButton;
	private JButton undoButton;
	private JButton clearButton;
	private JButton doneButton;
	private JButton deleteButton;
	private JTextArea jta;
	private JScrollPane scrollpane;
	
	private ArrayList<JButton> buttonList;
	private ArrayList<Integer> undoIndexList;
	private ArrayList<String> archtypeList;
	
	public ArchTypeWindow(){
		super("Hearthstone Deck StatTracker");
		setLayout(new BorderLayout());
		setSize(500,300);
		setLocation(500,250);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				closeWindow();
			}
		});

		
		initVars();
		addActions();
		addComp();
		setVisible(true);
	}//end of constructor
	
	public void initVars(){
		centerPanel = new JPanel();
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomCenterPanel = new JPanel();
		bottomCenterPanel.setLayout(new GridLayout(1, 5));
		
		descriptionLabel = new JLabel("Click on the Buttons to add Archtype");
		undoIndexList = new ArrayList<Integer>();
		archtypeList = new ArrayList<String>();
		buttonList = new ArrayList<JButton>();
		for(int i=0; i < StatsWindow.archTypes.size(); i++){
			buttonList.add(new JButton(StatsWindow.archTypes.get(i)));
		}//eo for

		
		addArchtypeButton = new JButton("Add New");
		undoButton = new JButton("Undo");
		doneButton = new JButton("Done");
		clearButton = new JButton("Clear");
		deleteButton = new JButton("Delete");
		
		jta = new JTextArea();
		jta.setSize(200, 10);
		jta.setEditable(false);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		
		scrollpane = new JScrollPane(jta);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
	}//eo init vars
	
	public void addActions(){
		for(int i=0; i < StatsWindow.archTypes.size(); i++){
			int x = i;
			buttonList.get(i).addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					//System.out.println("adding to undoIndexList size: " + undoIndexList.size());
					undoIndexList.add(x);
					archtypeList.add(buttonList.get(x).getText());
					buttonList.get(x).setEnabled(false);
					updateText();
				}
			});
		}//eo for
		
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				int buttonIndex = undoIndexList.get(undoIndexList.size()-1);
				buttonList.get(buttonIndex).setEnabled(true);
				archtypeList.remove(archtypeList.size()-1);
				undoIndexList.remove(undoIndexList.size()-1);						
				updateText();	
			}
		});
		
		addArchtypeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String userInput = JOptionPane.showInputDialog(ArchTypeWindow.this, "Input Archtpye", "Add New Archtype", JOptionPane.QUESTION_MESSAGE);
				userInput = userInput.toUpperCase();
				if(StatsWindow.isDuplicate(userInput, archtypeList)){
					JOptionPane.showMessageDialog(ArchTypeWindow.this, "Duplicate archtype name entered!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}//eo if
				else{
					int userChoice = -1;
					try{
						userChoice = JOptionPane.showConfirmDialog(ArchTypeWindow.this, "Add the archtype: \""+ userInput.toUpperCase() + "\"?", "Add New Archtype", JOptionPane.YES_NO_OPTION);
					}catch(NullPointerException npe){
						System.out.println("NPE: " + npe.getMessage());
					}
					
					switch(userChoice){
						case JOptionPane.YES_OPTION:
							//archtypeList.add(userInput);
							buttonList.add(new JButton(userInput));
							buttonList.get(buttonList.size()-1).addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ae){
									archtypeList.add(buttonList.get(buttonList.size()-1).getText());
									undoIndexList.add((buttonList.size()-1));
									buttonList.get(buttonList.size()-1).setEnabled(false);
									updateArchtypeFile();
								}
							});
							centerPanel.add(buttonList.get(buttonList.size()-1));
							centerPanel.revalidate();
							centerPanel.repaint();
							updateText();
							//add(centerPanel, BorderLayout.CENTER);
							
							break;
						case JOptionPane.NO_OPTION:
							break;
					}//eo switch
				}//eo else if
			}
		});
		
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				undoIndexList.clear();
				for (int i = 0; i < buttonList.size(); i++) {
					buttonList.get(i).setEnabled(true);
					archtypeList.clear();
					updateText();
				}//eo for
			}
		});
		
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				int userInput = JOptionPane.showConfirmDialog(ArchTypeWindow.this, "Delete archtype: " + buttonList.get(buttonList.size()-1).getText(), "Delete Last Clicked Archtype", JOptionPane.YES_NO_OPTION);
				switch(userInput){
					case JOptionPane.YES_OPTION:
						centerPanel.remove(buttonList.get(buttonList.size()-1));
						buttonList.remove(buttonList.size()-1);
						archtypeList.remove(archtypeList.size()-1);
						updateText();
						centerPanel.revalidate();
						centerPanel.repaint();
						updateArchtypeFile();
					case JOptionPane.NO_OPTION:
						break;
				}//eo switch
			}	
		});
		
		doneButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				StatsWindow.displayedArchTypes = archtypeList;
				StatsWindow.updateDisplayedArchTypes();
				closeWindow();
			}
		});
	}//end of add actions
	
	public void addComp(){
		for(int i=0; i < StatsWindow.archTypes.size(); i++){
			centerPanel.add(buttonList.get(i));
		}//eo for
		
		bottomCenterPanel.add(addArchtypeButton);
		bottomCenterPanel.add(deleteButton);
		bottomCenterPanel.add(undoButton);
		bottomCenterPanel.add(doneButton);
		bottomCenterPanel.add(clearButton);
		bottomPanel.add(bottomCenterPanel, BorderLayout.CENTER);
		bottomPanel.add(jta, BorderLayout.SOUTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		add(descriptionLabel, BorderLayout.NORTH);
	}//end of add comp
	
	public void closeWindow(){
		this.dispose();
	}//eo closewindow
	
	public void updateArchtypeFile(){
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			fw = new FileWriter("src/Data/archTypeDemo.txt");
			pw = new PrintWriter(fw);
			for(int i=0; i < buttonList.size(); i++){
				pw.println(buttonList.get(i).getText());
				
			}
			
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		finally{
			pw.close();
			//fw.close();
		}
		
	}//eo saving file
	
	public void updateText(){
		jta.setText("");
		for(int i=0; i < archtypeList.size(); i++){
			if(i == archtypeList.size()-1){
				jta.append(archtypeList.get(i));
			}
			else{
				jta.append(archtypeList.get(i) + ", ");
			}
		}//eo for
	}//eo updating text
	
	public void run(){
		int n = 0;
		while(true){
			n = undoIndexList.size();
			if(n == 0){
				undoButton.setEnabled(false);
			}
			else{
				undoButton.setEnabled(true);
			}
			System.out.println();
		}//eo while
	}//eo run
}//end of class
