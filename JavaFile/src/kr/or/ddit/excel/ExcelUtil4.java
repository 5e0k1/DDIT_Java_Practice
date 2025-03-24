package kr.or.ddit.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import kr.or.ddit.util.ScanUtil;
import lombok.Data;

public class ExcelUtil4 {
	public static void main(String[] args) {
		ExcelUtil4 obj = new ExcelUtil4();
		obj.process();
	}
	
	List<MemberVo> list = new ArrayList<>();
	public void process() {
		// list에 엑셀에서 값 읽어오기.
		
		readExcel();
		while(true) {
			System.out.println("0. 저장");
			System.out.println("1. 회원 리스트 출력");
			System.out.println("2. 회원 가입");
			System.out.println("3. 회원 삭제");
			System.out.println("4. 회원 정보 수정");
			System.out.println("5. 프로그램 종료");
			System.out.print("선택 : ");
			int sel = ScanUtil.select();
			
			if(sel ==0) saveExcel();
			if(sel ==1) list();
			if(sel ==2) join();
			if(sel ==3) delete();
			if(sel ==4) update();
			if(sel ==5) {
				saveExcel();
				break;
			}
		}
	}
	
	
	public void saveExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		
		
		for(int i=0; i<list.size(); i++) {
			MemberVo member = list.get(i);
			Row row = sheet.createRow(i);
			for(int j=0;j<3;j++) {
				Cell cell = row.createCell(j);
				if(j==0) cell.setCellValue(member.getMemNo());
				if(j==1) cell.setCellValue(member.getName());
				if(j==2) cell.setCellValue(member.getAge());
			}
		}
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream("excel/test.xlsx");
			workbook.write(fos);
			
			workbook.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("저장완료@@");
	}
	
	
	public void update() {
		list();

		int sel = ScanUtil.nextInt("수정할 번호")-1;
		String name = ScanUtil.nextLine("변경할 이름: ");
		int age = ScanUtil.nextInt("변경할 나이 : ");
		MemberVo mem = list.get(sel);
		mem.setName(name);
		mem.setAge(age);
		
		System.out.println("수정완료");
		list();
	}
	
	
	public void delete() {
		list();
		
		int delNo =ScanUtil.nextInt("삭제할 번호 선택 :")-1;
		MemberVo delMem =list.remove(delNo);
		System.out.println(delMem.getName()+"님 정보 삭제완료");
		
		list();
	}
	
	public void join() {
		String name = ScanUtil.nextLine("이름 : ");
		int age = ScanUtil.nextInt("나이 : ");
		int no = list.get(list.size()-1).getMemNo()+1;
		
		MemberVo member = new MemberVo();
		member.setMemNo(no);
		member.setName(name);
		member.setAge(age);
		
		list.add(member);
		System.out.println("등록완료");
	}
	
	
	public void list() {
		System.out.println("선택번호\t회원번호\t이름\t나이");
		int no=1;
		for(MemberVo mem : list) {
			System.out.println(no+++"\t"+mem.getMemNo()+"\t"+mem.getName()+"\t"+mem.getAge());
		}
	}
	
	
	public void readExcel() {
		File file = new File("excel/text.xlsx");
		
		try {
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);
			
			int fRow = sheet.getFirstRowNum();
			int lRow = sheet.getLastRowNum();
			
			for(int i=fRow+1; i<=lRow; i++) {
				Row row = sheet.getRow(i);
				MemberVo member = new MemberVo();
				
				for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					CellType type = cell.getCellType();
					String val="";
					if(type == CellType.STRING) {
						val = cell.getStringCellValue();
					}
					if(type == CellType.NUMERIC) {
						val = (int)cell.getNumericCellValue()+"";
					}
					if(type == CellType.FORMULA) {
						val = cell.getCellFormula();
					}
					
					if(j==0) member.setMemNo(Integer.parseInt(val));
					if(j==1) member.setName(val);
					if(j==2) member.setAge(Integer.parseInt(val));
				}
				list.add(member);
			}
			
			workbook.close();
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}




@Data
class MemberVo{
	private int memNo;
	private String name; 
	private int age; 
}