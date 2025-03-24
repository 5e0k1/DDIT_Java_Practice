package kr.or.ddit.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		
		File file = new File("excel/data.xls");
		// 문서
		Workbook workbook =  null ;
		
		if(file.getName().endsWith(".xls")) {
			// 97~2003 버전(.xls)
			FileInputStream fis = new FileInputStream(file);
			workbook = new HSSFWorkbook(fis);
		}
		else if(file.getName().endsWith(".xlsx")) {
			// 2007 버전(.xlsx)
			workbook = new XSSFWorkbook(file);
		}
		else {
			System.out.println("잘못된 확장자");
			return ;
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		
//		Row  row  = sheet.getRow(0);
//		Cell cell = row.getCell(0);
//		Cell cell2 = row.getCell(1);
//		
//		String str = cell.getStringCellValue();
//		String str2 = cell2.getStringCellValue();
//		System.out.println(str+"\t"+str2);
		
		int firstNum = sheet.getFirstRowNum();
		int lastNum = sheet.getLastRowNum();
		
		for(int i=firstNum; i<=lastNum; i++) {
			Row row = sheet.getRow(i);
			for(int j= 0; j<row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
//				String val = cell.getStringCellValue();
				String val = getCellValue(cell);
				System.out.print(val+"\t");
			}
			System.out.println();
		}
		
		
		
	}
	
	
	public static String getCellValue(Cell cell) {
		CellType type = cell.getCellType();
		
		if(type == CellType.STRING) {
			return cell.getStringCellValue();
		}
		if(type == CellType.NUMERIC) {
			// 날짜, 숫자
			// 정수 체크
			if(DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 시분초 처리 00:00:00 일때 
				if (date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0) {
				} else {
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
				return sdf.format(date);
			}
			double d = cell.getNumericCellValue();
			if (d == Math.floor(d)) {
				return (int) d + "";
			}
			
			return cell.getNumericCellValue()+"";
		}
		if(type == CellType.FORMULA) {
			return cell.getCellFormula();
		}
		
		return "";
	}
	
}
