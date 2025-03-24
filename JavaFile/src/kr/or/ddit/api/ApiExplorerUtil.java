package kr.or.ddit.api;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.or.ddit.vo.WeatherVo;

public class ApiExplorerUtil {
	static List<WeatherVo> list;
	static String serviceKey = "z5XDXN23w4UGvjSkQaGqg1jn6AXmhjVSwFNAa76AEpmxixj%2BWwIk6UVi7BEL%2BmXhbuPi%2BEXA0aclFqOs5VoIpw%3D%3D";
	static String filePath = "excel/weather.xlsx";
	
	public static String httpConnect(String urlBuilder) {
		try {
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			System.out.println("Response code: " + conn.getResponseCode());
			
			StringBuilder sb = new StringBuilder();
			String line;
	        BufferedReader rd;
	        
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        while ((line = rd.readLine()) != null) {
	            sb.append(line +"\n");
	        }
	        rd.close();
	        conn.disconnect();
	        return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static WeatherVo getApi() {
		return getApi("68","100");
	}
	
	public static WeatherVo getApi(String nx, String ny) {
		WeatherVo weather = createTimeDate();
		
		return getApi(weather.getBaseDate(), weather.getBaseTime(), nx, ny);
	}
	
	public static WeatherVo createTimeDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
		// baseTime 30분 이전인 경우 한시간 뺀 값
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		
		if(date.getMinutes()<30) {
			cal.add(Calendar.HOUR, -1);
			date = cal.getTime();
		}
		
		String baseDate = sdf.format(date);
		String baseTime = sdf2.format(date)+"00";
		
		WeatherVo weather = new WeatherVo();
		weather.setBaseDate(baseDate);
		weather.setBaseTime(baseTime);
		
		return weather;
	}
	
	
	public static WeatherVo getApi(String base_date, String base_time, String nx, String ny) {
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 					/*페이지번호*/
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); 			/*한 페이지 결과 수*/
			urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); 				/*요청자료형식(XML/JSON) Default: XML*/
			urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8")); 			/*‘21년 6월 28일 발표*/
			urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8")); 			/*06시 발표(정시단위) */
			urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); 						/*예보지점의 X 좌표값*/
			urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); 						/*예보지점의 Y 좌표값*/
			
			String str= httpConnect(urlBuilder.toString());
			WeatherVo weather = jsonParser(str);
			
			weather.setBaseDate(base_date);
			weather.setBaseTime(base_time);
			weather.setNx(nx);
			weather.setNy(ny);
			
			return weather;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static WeatherVo jsonParser(String jsonStr) {
		
		 JSONParser jparser = new JSONParser();
	        try {
				JSONObject jobj =(JSONObject)jparser.parse(jsonStr);
				
				String rn1 ="";
				String t1h ="";
				String reh ="";
				
				JSONObject response = (JSONObject) jobj.get("response");
				JSONObject body =(JSONObject) response.get("body");
				JSONObject items = (JSONObject) body.get("items");
				JSONArray item = (JSONArray) items.get("item");
				
				for(int i=0; i<item.size();i++) {
					JSONObject data = (JSONObject) item.get(i);
					String category = (String) data.get("category");
					
					if(category.equals("RN1")) {
						rn1 = (String) data.get("obsrValue");
					}
					if(category.equals("T1H")) {
						t1h = (String) data.get("obsrValue");
					}
					if(category.equals("REH")) {
						reh = (String) data.get("obsrValue");
					}
				}
				WeatherVo weather = new WeatherVo();
				
				weather.setRn1(rn1);
				weather.setT1h(t1h);
				weather.setReh(reh);
				return weather;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static void readExcelData() {
		if(list != null) return;
		
			try {
				File file = new File(filePath);
				list = new ArrayList<>();
				if(file.exists()) {
					FileInputStream fis = new FileInputStream(file);
					Workbook workbook = new XSSFWorkbook(fis);
					
					Sheet sheet = workbook.getSheetAt(0);
					
//					private int weatherNo;
//					
//					private String nx;
//					private String ny;
//					
//					private String baseDate;
//					private String baseTime;
//					
//					private String rn1;
//					private String t1h;
//					private String reh;
					
					for(int i=0; i<=sheet.getLastRowNum();i++) {
						Row row = sheet.getRow(i);
						int index =0;
						int weatherNo = (int)row.getCell(index++).getNumericCellValue();  
		                
						String nx = row.getCell(index++).getStringCellValue(); 
						String ny = row.getCell(index++).getStringCellValue();      
						                
						String baseDate = row.getCell(index++).getStringCellValue();
						String baseTime = row.getCell(index++).getStringCellValue();
						                
						String rn1 = row.getCell(index++).getStringCellValue();     
						String t1h = row.getCell(index++).getStringCellValue();     
						String reh = row.getCell(index++).getStringCellValue();
						
						WeatherVo weather = new WeatherVo();
						weather.setWeatherNo(weatherNo);
						weather.setNx(nx);
						weather.setNy(ny);
						weather.setBaseDate(baseDate);
						weather.setBaseTime(baseTime);
						weather.setReh(reh);
						weather.setRn1(rn1);
						weather.setT1h(t1h);
						
						list.add(weather);
					}
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	public static void saveExcelData() {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			Workbook workbook = new XSSFWorkbook();
			
			Sheet sheet = workbook.createSheet();
			
			for(int i=0; i<list.size();i++) {
				Row row = sheet.createRow(i);
				WeatherVo weather = list.get(i);
				int index = 0;
				row.createCell(index++).setCellValue(weather.getWeatherNo());
				row.createCell(index++).setCellValue(weather.getNx());
				row.createCell(index++).setCellValue(weather.getNy());
				row.createCell(index++).setCellValue(weather.getBaseDate());
				row.createCell(index++).setCellValue(weather.getBaseTime());
				row.createCell(index++).setCellValue(weather.getRn1());
				row.createCell(index++).setCellValue(weather.getT1h());
				row.createCell(index++).setCellValue(weather.getReh());
			}
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static WeatherVo getData() {
		return getData("68","100");
	}
	
	public static WeatherVo getData(String nx, String ny) {
		readExcelData();
		
		WeatherVo w1 = createTimeDate();
		w1.setNx(nx);
		w1.setNy(ny);
		
		for(WeatherVo w2 : list) {
			if(w1.getNx().equals(w2.getNx()) && w1.getNy().equals(w2.getNy())
					&& w1.getBaseDate().equals(w2.getBaseDate())&& w1.getBaseTime().equals(w2.getBaseTime())) {
				return w2;
			}
		}
		w1 = getApi(nx,ny);
		// 엑셀 저장 코드 추가
		w1.setWeatherNo(list.size()+1);
		list.add(w1);
		saveExcelData();
		
		return w1;
	}
	
	
    public static void main(String[] args) throws IOException {
//    	String html = httpConnect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=api%EB%9E%80");
//    	String[] line = html.split("\n");
//    	for(String l : line) {
//    		try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    		System.out.println(l);
//    	}
    	
    	WeatherVo weather = getData("60","100");
    	System.out.println(weather);
    }
}