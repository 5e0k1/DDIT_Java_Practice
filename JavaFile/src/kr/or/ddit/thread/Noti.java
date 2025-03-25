package kr.or.ddit.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.or.ddit.util.ScanUtil;

public class Noti {
	NotiDao notiDao = NotiDao.getInstance();
	
	public static void main(String[] args) {
		// 프로그램 사용중에 알림 보내기.
		Noti obj = new Noti();
		obj.process();
	}
	
	public void process() {
		Message m = new Message();
		m.start();
		
		while(true) {
			System.out.println("1. 게시글 리스트");
			System.out.println("2. 게시글 작성");
			int sel = ScanUtil.select();
			
			if(sel==1) printNoti();
			if(sel==2) writeNoti();
		}
	}
	
	public synchronized void printNoti() {
		List<Map<String, Object>> list = notiDao.printList();
		
		System.out.println("번호\t제목\t내용");
		for(Map<String, Object> map : list) {
			BigDecimal noBig =(BigDecimal) map.get("NO");
			int no = noBig.intValue();
			String title =(String) map.get("TITLE");
			String content = (String) map.get("CONTENT");
			System.out.println(no+"\t"+title+"\t"+content);
		}
		
	}
	
	public synchronized void writeNoti() {
		List<Object> param = new ArrayList<>();
		
		param.add(ScanUtil.nextLine("제목 : "));
		param.add(ScanUtil.nextLine("내용 : "));
		
		int insert = notiDao.writeNoti(param);
		if(insert == 1)System.out.println("등록완료@@");
	}

}
class Message extends Thread{
	NotiDao notiDao = NotiDao.getInstance();
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			List<Map<String, Object>> list = notiDao.getList();
			
			if(list ==null) continue;
			System.out.println("신규 공지");
			for(Map<String, Object> map : list) {
				BigDecimal no = (BigDecimal) map.get("NO");
				notiDao.updateRead(no.intValue());
				
				System.out.println(map);
			}
		}
	}
}