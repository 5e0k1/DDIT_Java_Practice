package kr.or.ddit.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonExample02 {
	public static void main(String[] args) {
		
		try {
			FileReader fr = new FileReader("json/test.json");
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(fr); 
//			JSONArray jarray = (JSONArray)parser.parse("[1,2,3]");
			
			System.out.println(jobj);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
