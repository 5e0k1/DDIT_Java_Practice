package kr.or.ddit.vo;

import lombok.Data;

@Data
public class WeatherVo {
	
	private int weatherNo;
	
	private String nx;
	private String ny;
	
	private String baseDate;
	private String baseTime;
	
	private String rn1;
	private String t1h;
	private String reh;
	
}
