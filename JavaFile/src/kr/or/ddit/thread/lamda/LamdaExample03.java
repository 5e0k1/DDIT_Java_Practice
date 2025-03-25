package kr.or.ddit.thread.lamda;

public class LamdaExample03 {
	public static void main(String[] args) {
		Message m1 = new Message() {
			
			@Override
			public String getMessage() {
				return "메세지 입니다.";
			}
		};
		
		Message m2 = () -> "메세지 입니다";
		
		System.out.println(m2.getMessage());
	}
}

interface Message{
	String getMessage();
}