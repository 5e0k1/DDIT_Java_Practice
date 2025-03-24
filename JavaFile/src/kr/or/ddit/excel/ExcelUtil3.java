package kr.or.ddit.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil3 {
	public static void main(String[] args) throws InvalidFormatException, IOException {

		File file = new File("excel/data.xls");

		Workbook workbook = null;

		if (file.getName().endsWith(".xls")) {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			workbook = new HSSFWorkbook(bis);
		}
		if (file.getName().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(file);
		}
		
		Workbook copyWorkbook = new XSSFWorkbook();

		int sheetNum = workbook.getNumberOfSheets();
		System.out.println("시트수 : " + sheetNum);

		for (int i = 0; i < sheetNum; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			String sName = sheet.getSheetName();
			Sheet copySheet = copyWorkbook.createSheet(sName);

			int fRowNum = sheet.getFirstRowNum();
			int lRowNum = sheet.getLastRowNum();
			
			if(fRowNum ==-1 || lRowNum ==-1) continue;

			for (int j = fRowNum; j <= lRowNum; j++) {
				Row row = sheet.getRow(j);
				Row cRow = copySheet.createRow(j);

				for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
					Cell cell = row.getCell(k);
					Cell cCell = cRow.createCell(k);
					if (cell.getCellType() == CellType.STRING) {
						cCell.setCellValue(cell.getStringCellValue());
					}
					if (cell.getCellType() == CellType.NUMERIC) {
						cCell.setCellValue(cell.getNumericCellValue());
					}
					if (cell.getCellType() == CellType.FORMULA) {
						cCell.setCellValue(cell.getCellFormula());
					}
				}
			}
		}
		
		FileOutputStream fos = new FileOutputStream("excel/copy.xlsx");
		workbook.write(fos);
		

	}
}
