package Data;

import java.util.HashMap;
import java.util.Map;

 
public class Stats{
	public Map<String, Map<String, DeckData>> data;
	public Stats(){
		data = new HashMap<String, Map<String, DeckData>>();
		data.put("WARRIOR", new HashMap<String, DeckData>());
		data.put("SHAMAN", new HashMap<String, DeckData>());
		data.put("ROGUE", new HashMap<String, DeckData>());
		data.put("PALADIN", new HashMap<String, DeckData>());
		data.put("HUNTER", new HashMap<String, DeckData>());
		data.put("DRUID", new HashMap<String, DeckData>());
		data.put("WARLOCK", new HashMap<String, DeckData>());
		data.put("MAGE", new HashMap<String, DeckData>());
		data.put("PRIEST", new HashMap<String, DeckData>());
		
		System.out.println("Stats constructor finished");
	}//eo constructor
	
	public boolean deckExists(String hN, String dN){
		Map<String, DeckData> map = data.get(hN);
		return map.containsKey(dN);
	}//eo exists
	
	public Map<String, Map<String, DeckData>> getData(){
		if(data == null){
			System.out.println("RETURNING NULL DATA, CALLING CONSTRUCTOR FIRST");
		}
		return data;
	}
	
	public DeckData getDeckData(String hN, String dN){
		Map<String, DeckData> map = data.get(hN);
		return map.get(dN);
	}
}//eo class