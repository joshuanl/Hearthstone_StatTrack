package Data;

import java.util.ArrayList;

public class MatchData {

	private String _heroName;
	private String _deckName;
	private String _opponentName;
	private String _opponentDeckType;
	private ArrayList<String> _deckArchtypes;
	private ArrayList<String> _opponentDeckArchtypes;
	private int _gamesWon;
	private int _gamesLost;
	private String _rank;
	private boolean gameWon;
	
	public MatchData(String hN, String dN, String oN, String oDT, ArrayList<String> dA, ArrayList<String>oDA,
							int gW, int gL, String r, boolean b){
		
	}//eo constructor
	
	public String getHeroName(){
		return _heroName;
	}
	
	public String getDeckName(){
		return _deckName;
	}
	
	public String getOppName(){
		return _opponentName;
	}
	
	public String getOppDeckType(){
		return _opponentDeckType;
	}
	
	public String getRank(){
		return _rank;
	}
	public ArrayList<String> getDeckArchtypes(){
		return _deckArchtypes;
	}
	
	public ArrayList<String> getOppDeckArchtypes(){
		return _opponentDeckArchtypes;
	}
	
	public int getGamesWon(){
		return _gamesWon;
	}
	
	public int getGamesLost(){
		return _gamesLost;
	}
	
	public boolean gameWon(){
		return gameWon;
	}
}//eo class


