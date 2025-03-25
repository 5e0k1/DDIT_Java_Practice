package kr.or.ddit.thread;

import java.util.List;
import java.util.Map;

import kr.or.ddit.util.JDBCUtil;

public class NotiDao {
	private static NotiDao instance;
	
	private NotiDao() {
		
	}
	
	public static NotiDao getInstance() {
		if(instance == null) {
			instance = new NotiDao();
		}
		
		return instance;
		
	}
	
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int writeNoti(List<Object> param) {
		String sql ="INSERT INTO NOTI (NO,TITLE,CONTENT)\r\n"
				  + "VALUES((SELECT NVL(MAX(NO),0)+1 FROM NOTI),?,?)";
		
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> getList(){
		String sql ="SELECT *\r\n"
				  + "FROM NOTI\r\n"
				  + "WHERE READ ='N'\r\n"
				  + "ORDER BY NO";
		
		return jdbc.selectList(sql);
	}
	
	public void updateRead(int no) {
		String sql = "UPDATE NOTI\r\n"
				+ "SET READ='Y'\r\n"
				+ "WHERE NO="+no;
		
		jdbc.update(sql);
	}
	
	public List<Map<String, Object>> printList(){
		String sql ="SELECT *\r\n"
				  + "FROM NOTI\r\n"
				  + "ORDER BY NO";
		
		return jdbc.selectList(sql);
	}
	
	
}
