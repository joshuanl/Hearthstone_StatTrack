package Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DeckData {
	private static ArrayList<String> _allDeckTypes;
	private String _deckName;
	private String _heroName;
	private String _mainDeckType;
	private ArrayList<String> _deckTypes;
	
	public DeckData(String deckName, String heroName){
		_deckName = deckName;
		_heroName = heroName;
		_allDeckTypes = new ArrayList<String>();
		_deckTypes = new ArrayList<String>();
			
		
	}
	
	public void addType(int index){
		
	}
	
	public static void createDeckType(String type){
		
	}
	
	public void addWin(DeckData opponent){
		
	}
	
	public void addLoss(DeckData opponent){
		
	}
	
	public static String[] getDeckTypesArray(){
		loadDeckTypes();
		alphabetizeDeckTypes();
		String list[] = new String[_allDeckTypes.size()+1];
		list[0] = "DECK TYPE";
		for(int i=0; i < _allDeckTypes.size(); i++){
			list[i+1] = _allDeckTypes.get(i);	
		}
		return list;
	}
	
	/**
	 * Method - loadDeckTypes reads list of deck types as Strings and stores into an ArrayList
	 */
	public static void loadDeckTypes(){
		_allDeckTypes = new ArrayList<String>();
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader("src/Data/allDeckTypes.txt");
			br = new BufferedReader(fr);
			String deckType = br.readLine();
			while(deckType != null){
				_allDeckTypes.add(deckType);
				deckType = br.readLine();
			}//eo while
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		
	}//eo loading and reading file
	
	/**
	 * Method - alphabetizeDeckTypes alphabetizes strings within an ArrayList 
	 */
	public static void alphabetizeDeckTypes(){
		String currentType, stringTemp;
		int smallestUnicode, smallestIndex, temp, sameCharIndex;
		boolean sameChar;
		for(int i=0; i < _allDeckTypes.size(); i++){
			currentType = _allDeckTypes.get(i);
			smallestUnicode = _allDeckTypes.get(i).charAt(0);
			smallestIndex = i;
			for(int j=i+1; j < _allDeckTypes.size(); j++){			
				sameCharIndex = 0;
				sameChar = true;				
				while(sameCharIndex < _allDeckTypes.get(j).length() && sameCharIndex < currentType.length() && sameChar){	
					if((int)_allDeckTypes.get(j).charAt(sameCharIndex) < (int)currentType.charAt(sameCharIndex)){
						smallestIndex = j;
						smallestUnicode = _allDeckTypes.get(j).charAt(sameCharIndex);
						currentType = _allDeckTypes.get(j);
						sameChar = false;
					}//eo if
					else if((int)_allDeckTypes.get(j).charAt(sameCharIndex) == (int)currentType.charAt(sameCharIndex)){
						sameCharIndex++;
					}//eo else if
					else{
						sameChar = false;
					}
				}//eo while
			}//eo inner for
			stringTemp = _allDeckTypes.get(i);
			_allDeckTypes.set(i, currentType);
			_allDeckTypes.set(smallestIndex, stringTemp);
		}//eo outer for
	}//eo method
	
	/**
	 * Method - printDeckTypes prints all the strings in _allDeckTypes
	 */
	public static void printDeckTypes(){
		for(String type : _allDeckTypes){
			System.out.println(type);
		}//eo for
	}//eo method
	
	
	//=========================== NOT WRITING TO FILE
	/**
	 * Method - saveDeckTypes saves deck types back into file
	 */
	public static void saveDeckTypes(){
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			fw = new FileWriter("src/Data/allDeckTypes.txt");
			pw = new PrintWriter(fw);
			for(int i=0; i < _allDeckTypes.size(); i++){
				pw.println(_allDeckTypes.get(i));
			}
			
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		finally{
			pw.close();
			//fw.close();
		}
	}//eo method
	
}//end of class
