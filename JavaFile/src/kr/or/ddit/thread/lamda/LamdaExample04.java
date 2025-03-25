package kr.or.ddit.thread.lamda;

import java.util.ArrayList;
import java.util.List;

public class LamdaExample04 {
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		list.forEach( (str) -> System.out.println(str) );
	}
}
