package Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class DeckData {
	private static ArrayList<String> _allDeckTypes;
	private String _deckName;
	private String _heroName;
	private String _mainDeckType;
	private int _winCount;
	private int _lossCount;
	private ArrayList<String> _archtypes;
	private ArrayList<MatchData> _matchDataList;
	private Map<String, Pair<Integer, Integer>> _statsAgainstType; 
	private Map<String, Pair<Integer, Integer>> _statsAgainstHero; 
	
	public DeckData(String deckName, String heroName){
		_deckName = deckName;
		_heroName = heroName;
		_archtypes = new ArrayList<String>();
		_matchDataList = new ArrayList<MatchData>();
		_statsAgainstHero = new HashMap<String, Pair<Integer, Integer>>();
		_statsAgainstType = new HashMap<String, Pair<Integer, Integer>>();
		
	}//eo constructor
	
	public void addMatch(MatchData m){
		_matchDataList.add(m);
		String oHN = m.getOppName();
		String oD = m.getOppDeckType();
		boolean gW = m.gameWon();

		if(_statsAgainstHero.containsKey(oHN)){
			Pair<Integer, Integer> p = _statsAgainstHero.get(oHN);
			if(gW){
				_statsAgainstHero.put(oHN, new Pair<Integer, Integer>(p.getKey()+1, p.getValue()));
			}//if won
			else{
				_statsAgainstHero.put(oHN, new Pair<Integer, Integer>(p.getKey(), p.getValue()+1));
			}
		}//eo if contains oHN
		else{
			if(gW){
				_statsAgainstHero.put(oHN, new Pair<Integer, Integer>(1, 0));
			}
			else{
				_statsAgainstHero.put(oD, new Pair<Integer, Integer>(0, 1));
			}		
		}//eo else doesnt contain oHN
			
		if(_statsAgainstType.containsKey(oD)){
			Pair<Integer, Integer> p = _statsAgainstType.get(oD);
			if(gW){
				_statsAgainstType.put(oD, new Pair<Integer, Integer>(p.getKey()+1, p.getValue()));
			}//if won
			else{
				_statsAgainstType.put(oD, new Pair<Integer, Integer>(p.getKey(), p.getValue()+1));
			}
		}//eo if contains oD
		else{
			if(gW){
				_statsAgainstType.put(oD, new Pair<Integer, Integer>(1, 0));
			}
			else{
				_statsAgainstType.put(oD, new Pair<Integer, Integer>(0, 1));
			}		
		}//eo else doesnt contain oD

	}//eo adding match data
	
	public void setDeckType(String s){
		_mainDeckType = s;
	}
	
	public void setArchTypes(ArrayList<String> s){
		_archtypes = s;
	}
	
	
	public void addWin(){
		_winCount++;
	}
	
	public void addLoss(){
		_lossCount++;
	}
	
	public String getDeckName(){
		return _deckName;
	}
	
	public String getHeroName(){
		return _heroName;
	}
	
	public String getDeckType(){
		return _mainDeckType;
	}
	
	public int getWinCount(){
		return _winCount;
	}
	
	public int getLossCount(){
		return _lossCount;
	}
	
	public ArrayList<String> getArchTypes(){
		return _archtypes;
	}
	
	public ArrayList<MatchData> getMatchDataList(){
		return _matchDataList;
	}
	
	public Map<String, Pair<Integer, Integer>> getMapStatsAgainstType(){
		return _statsAgainstType;
	}
	
	public Map<String, Pair<Integer, Integer>> getMapStatsAgainstHero(){
		return _statsAgainstHero;
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
