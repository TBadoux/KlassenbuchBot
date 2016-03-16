package klassenbuchbot;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonHandler{
	
	public static ArrayList<Entry> JSONRead(String date) {
		
		ArrayList<Entry> EntryList = new ArrayList<Entry>();
			
		try{
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(new FileReader("entries.json"));
			JSONArray earr = (JSONArray) obj.get("entries");
			
			for(Object ent : earr) {
				

				JSONObject jentry = (JSONObject) ent;

				Entry e = new Entry();
				
				if (date.equals(jentry.get("date"))){
				
				
					e.type = (String) jentry.get("type");
					e.date = (String) jentry.get("date");
					e.subject = (String) jentry.get("subject");
					e.definition = (String) jentry.get("definition");
					
		
					EntryList.add(e);
				}
			}

		}catch (Exception ex) { 
			System.out.println("Config file not found!");
			ex.printStackTrace();
			
		}
		
		return EntryList;
	}
	
	@SuppressWarnings("unchecked")
	public static void JSONWrite(ArrayList<Entry> entries){
		
		JSONObject root = new JSONObject();
		JSONArray entrylist = new JSONArray();
		
		for(Entry e : entries) {
			JSONObject ent = new JSONObject();
			ent.put("type", e.type);
			ent.put("date", e.date);
			ent.put("subject", e.subject);
			ent.put("definition", e.definition);
			entrylist.add(ent);
		}
		root.put("entries", entrylist);
		
		String json = root.toString();
		FileWriter file;
		
		try {
			
			file = new FileWriter("entries.json");
			file.write(json);
			file.flush();
			file.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}