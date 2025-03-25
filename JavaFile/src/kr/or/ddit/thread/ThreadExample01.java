package kr.or.ddit.thread;

public class ThreadExample01 {
	public static void main(String[] args) {
		
		System.out.println("메인 쓰레드 시작");
		
		System.out.println("출력 쓰레드 시작");
		
		ThreadJob1 t = new ThreadJob1("쓰레드1");
		t.start();
		
		ThreadJob1 t2 = new ThreadJob1("쓰레드2");
		t2.start();
		
		
		System.out.println("메인 쓰레드 종료");
	}
}
class ThreadJob1 extends Thread{
	
	String name;
	
	public ThreadJob1(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		System.out.println(name+" 시작");
		for(int i=1; i<=10; i++) {
			System.out.println(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(name+" 끝");
	}
	
}
