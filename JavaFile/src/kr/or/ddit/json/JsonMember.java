package kr.or.ddit.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.or.ddit.util.ScanUtil;
import lombok.Data;

public class JsonMember {
	public static void main(String[] args) {
		JsonMember obj = new JsonMember();
		obj.init();
		obj.process();
	}
	
	List<Member> list;
	
	public void process() {
		while(true) {
			System.out.println("1. 회원 리스트 출력");
			System.out.println("2. 회원 가입");
			System.out.println("3. 회원 수정");
			System.out.println("4. 회원 삭제");
			System.out.println("5. 종료");
			int sel = ScanUtil.select();
			
			if(sel ==1) printList();
			if(sel ==2) memberJoin();
			if(sel ==3) memberUpdate();
			if(sel ==4) memberDelete();
			if(sel ==5) {
				saveJson();
				break;
			}
		}
		
		
	}
	
	
	
	public void memberUpdate() {
		printList();
		int no = ScanUtil.nextInt("변경할 회원 번호 : ");
		Member mem = null;
		for(Member m : list) {
			if(no == m.getNo())	mem = m;
		}
		
		if(mem == null) {
			System.out.println("존재하지 않는 회원");
		}else {
			mem.setName(ScanUtil.nextLine("변경할 이름 : "));
			mem.setAge(ScanUtil.nextInt("변경할 나이 : "));
			System.out.println("변경완료");
			printList();
		}
	}



	public void memberDelete() {
		printList();
		int sel = ScanUtil.nextInt("삭제할 회원 번호");
		Member del = null;
		for(Member m : list) {
			if(sel == m.getNo()) {
				del = m;
			}
		}
		if(del == null) {
			System.out.println("없는 회원정보@@");
			printList();
			return;
		}
		else {
			list.remove(del);
			System.out.println("삭제 완료@@");
		}
	}
	
	
	public void memberJoin() {
		Member mem = new Member();
		int no=0;
		for(Member m : list) {
			if(no<m.getNo()) no=m.getNo();
		}
		no++;
		mem.setNo(no);
		mem.setName(ScanUtil.nextLine("이름 : "));
		mem.setAge(ScanUtil.nextInt("나이 : "));
		
		list.add(mem);
//		saveJson();
	}
	
	
	public void printList() {
		System.out.println("===================================");
		System.out.println("회원번호\t회원명\t나이");
		for(Member mem : list) {
			System.out.println(mem.getNo()+"\t"+mem.getName()+"\t"+mem.getAge());
		}
		if(list.size()==0)System.out.println("등록된 회원 없음@@");
		System.out.println("===================================");
	}
	
	
	public void init() {
		File file = new File("json/memberjson.json");
		if(!file.exists()) {
			System.out.println("저장된 파일이 없어서 새로 생성@@");
			list= new ArrayList<Member>();
			return;
		}
		if(file.exists()) readJson();
	}
	
	public void saveJson() {
		try {
			FileWriter fw = new FileWriter("json/memberjson.json");
			JSONArray jarr = new JSONArray();
			for(Member m : list) {
				JSONObject obj = new JSONObject();
				obj.put("no", m.getNo());
				obj.put("name", m.getName());
				obj.put("age", m.getAge());
				
				jarr.add(obj);
			}
			fw.write(jarr.toJSONString());
			fw.flush();
			fw.close();
			System.out.println("저장완료@@");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void readJson() {
		list = new ArrayList<>();
		try {
			FileReader fr = new FileReader("json/memberjson.json");
			JSONParser parser = new JSONParser();
			JSONArray jarr = (JSONArray)parser.parse(fr);
			
			for(int i=0; i<jarr.size();i++) {
				Member mem = new Member();
				JSONObject jobj = (JSONObject)jarr.get(i);
				int no =(int)(long)jobj.get("no");
				String name = (String)jobj.get("name");
				int age = (int)(long)jobj.get("age");
				
				mem.setNo(no);
				mem.setName(name);
				mem.setAge(age);
				
				list.add(mem);
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}finally {
			System.out.println("불러오기 완료@@");
		}
		
	}
	
}

@Data
class Member{
	private int no;
	private String name;
	private int age;
}

