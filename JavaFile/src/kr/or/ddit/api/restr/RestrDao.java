package kr.or.ddit.api.restr;

import java.util.List;

import kr.or.ddit.util.JDBCUtil;
import kr.or.ddit.vo.RestrVo;

public class RestrDao {
	private static RestrDao instance;
	
	private RestrDao() {
		
	}
	
	public static RestrDao getInstance() {
		if (instance == null) {
			instance = new RestrDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int insertRestr(List<Object> param) {
		String sql ="INSERT INTO RESTRS\r\n"
				  + "VALUES ((SELECT NVL(MAX(RESTRNT_NO),0)+1 FROM RESTRS),?,?,?,?,?,?,?,?,?,?,?,?)";
		
		return jdbc.update(sql, param);
	}
	
	public List<RestrVo> getList(List<Object> param){
		String sql="WITH TABLE_NAME AS (\r\n"
				+ "    SELECT ROWNUM RN, A.*\r\n"
				+ "     FROM\r\n"
				+ "         (SELECT *\r\n"
				+ "          FROM RESTRS\r\n"
				+ "          ORDER BY RESTRNT_NO)A\r\n"
				+ ")\r\n"
				+ "SELECT *\r\n"
				+ "FROM TABLE_NAME\r\n"
				+ "WHERE RN >=? AND RN <=?";
		
		
		return jdbc.selectList(sql, param, RestrVo.class);
	}
	
	
}
