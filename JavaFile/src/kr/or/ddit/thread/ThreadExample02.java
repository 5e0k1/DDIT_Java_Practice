package kr.or.ddit.thread;

public class ThreadExample02 {
	public static void main(String[] args) {
//		Thread t1 = new Thread(new ThreadJob2());
//		
//		t1.start();
		
		Thread t2 = new Thread(()->{
			for(int i=0; i<5; i++) {
				System.out.println("동작 중");
			}
		});
		
		t2.start();
	}
}
class ThreadJob2 implements Runnable{

	@Override
	public void run() {
		for(int i=0; i<5; i++) {
			System.out.println("동작 중");
		}
	}
	
}