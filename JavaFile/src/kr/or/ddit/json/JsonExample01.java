package kr.or.ddit.json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonExample01 {
	public static void main(String[] args) {
		// map과 같은 형태로 key, value로 이루어짐
		JSONObject jobj = new JSONObject();
		jobj.put("data1", "값1");
		jobj.put("data2", "값2");
		
		System.out.println(jobj.get("data1"));
		System.out.println(jobj.get("data2"));
		
		if(jobj.containsKey("data1")) {
			System.out.println("data1이 포함됨");
		}
		if(!jobj.containsKey("data3")) {
			System.out.println("data3 없음");
		}
		
		Iterator it = jobj.keySet().iterator();
		
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = (String)jobj.get(key);
			System.out.println(key+" : "+value);
		}
		
		JSONArray jArr = new JSONArray();
		jArr.add(1);
		jArr.add("Abc");
		jArr.add(true);
		
		JSONObject member = new JSONObject();
		member.put("name", "홍길동");
		member.put("age", 20);
		jArr.add(member);
		
		jobj.put("배열", jArr);
		
		System.out.println(jobj);
		System.out.println(jArr);
		
		try {
			FileWriter fw = new FileWriter("json/test.json");
			fw.write(jobj.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
