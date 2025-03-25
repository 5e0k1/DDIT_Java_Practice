package kr.or.ddit.thread.lamda;

public class LamdaExample02 {
	public static void main(String[] args) {
		
		Printer printer = m ->System.out.println("출력 : "+m);
		
		printer.print("ㅎㅇ");
	}
}

interface Printer{
	void print(String message);
}
