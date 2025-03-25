package kr.or.ddit.thread;

import java.util.Date;

public class ThreadExample03 {
	
	static int endThread;
	
	public static void main(String[] args) {
		
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("사용 가능 CPU 코어 수 : " + cores);
		
		
		// 1~1억까지의 합을 구하고 걸리는 시간을 구하시오.
		// 100000000
		
		long sum = 0;
		
		long last = 10000000000L;
		
		Date d1 = new Date();
		
		int threadNum = cores*2;
		
		ThreadSum[] threadList = new ThreadSum[threadNum];
		
		for(int i=1;i<=threadNum;i++) {
			long start = last/threadNum*(i-1)+1;
			long end = last/threadNum*i;
			
			ThreadSum t = new ThreadSum(start, end);
			threadList[i-1] = t;
			t.start();
		}
		
		while(true) {
			int cnt=0;
			for(int i=0; i<threadList.length;i++) {
				ThreadSum t = threadList[i];
				if(t.exit) cnt++;
			}
			if(cnt ==threadList.length)break;
		}
		
		Date d2 = new Date();
		
		long time = d2.getTime()-d1.getTime();
		
		System.out.println(time);
	}
}

class ThreadSum extends Thread{
	
	long sum;
	long start;
	long end;
	
	boolean exit = false;
	
	public ThreadSum(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void run() {
		for(long i= start; i<=end; i++) {
			sum += i;
		}
		exit = true;
	}
	
	public long getSum() {
		return sum;
	}
}
