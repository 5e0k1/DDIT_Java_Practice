package kr.or.ddit.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import kr.or.ddit.util.ScanUtil;

public class ExcelUtil4_1 {
	public static void main(String[] args) {
		ExcelUtil4_1 obj = new ExcelUtil4_1();
		obj.process();
	}

	public void process() {
		List<MemberVo> list = readExcel();
		
		while(true) {
			System.out.println("1. 회원 리스트 출력");
			System.out.println("2. 회원 가입");
			System.out.println("3. 회원 삭제");
			System.out.println("4. 회원 정보 수정");
			System.out.println("5. 프로그램 종료");
			System.out.println("6. 자동저장 변경 : 현재상태("+autoSave+")");
			
			int sel = ScanUtil.select();
			if(sel == 1) printList(list);
			if(sel == 2) memberJoin(list);
			if(sel == 3) memberDelete(list);
			if(sel == 4) memberUpdate(list);
			if(sel == 5) {
				saveExcel(list);
				break;
			}
			if(sel ==6) autoSave = !autoSave;
		}
	}
	
	
	
	public void memberDelete(List<MemberVo> list) {
		printList(list);
		
		int sel = ScanUtil.nextInt("삭제할 회원 번호 : ");
		
		for(MemberVo mem : list) {
			if(mem.getMemNo() == sel) list.remove(mem);
		}
		
		if(autoSave) saveExcel(list);
	}

	public void memberUpdate(List<MemberVo> list) {
		printList(list);
		
		int sel = ScanUtil.nextInt("수정할 회원 번호 : ");
		
		MemberVo member = null;
		for(MemberVo mem : list) {
			if(mem.getMemNo() ==sel) member = mem;
		}
		if(member == null) System.out.println("해당 회원번호는 없습니다.");
		else {
			member.setName(ScanUtil.nextLine("수정할 이름 : "));
			member.setAge(ScanUtil.nextInt("수정할 나이 : "));
		}
		if(autoSave) saveExcel(list);
		
		printList(list);
	}



	
	boolean autoSave = true;
	public void saveExcel(List<MemberVo> list) {
		FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("excel/data.xls");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Workbook workbook = new HSSFWorkbook();
			
			Sheet sheet = workbook.createSheet();

			Row index = sheet.createRow(0);
			
			Cell c1 = index.createCell(0);
			Cell c2 = index.createCell(1);
			Cell c3 = index.createCell(2);
			
			c1.setCellValue("회원번호");
			c2.setCellValue("이름");
			c3.setCellValue("나이");
			
			for(int i=1; i<list.size()+1;i++) {
				MemberVo member = list.get(i-1);
				Row row = sheet.createRow(i);
				
				Cell rc1 = row.createCell(0);
				Cell rc2 = row.createCell(1);
				Cell rc3 = row.createCell(2);
				
				rc1.setCellValue(member.getMemNo());
				rc2.setCellValue(member.getName());
				rc3.setCellValue(member.getAge());
			}
			
			try {
				workbook.write(fos);
				fos.close();
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
	}

	public void memberJoin(List<MemberVo> list) {
//		int no =list.get(list.size()-1).getMemNo()+1;
		int memNo = 0;
		for(MemberVo mem : list) {
			if(memNo<mem.getMemNo()) memNo =mem.getMemNo(); 
		}
		memNo++;
		String name = ScanUtil.nextLine("이름 : ");
		int age = ScanUtil.nextInt("나이 : ");
		
		MemberVo member = new MemberVo();
		member.setMemNo(memNo);
		member.setName(name);
		member.setAge(age);
		
		list.add(member);
		
		if(autoSave) saveExcel(list);
	}

	public void printList(List<MemberVo> list) {
		System.out.println("회원번호\t이름\t나이");
		for(MemberVo member : list) {
			System.out.println(member.getMemNo()+"\t"+member.getName()+"\t"+member.getAge());
		}
	}

	public List<MemberVo> readExcel(){
		FileInputStream fis = null;
		List<MemberVo> memberList = new ArrayList<>();
		
		try {
			fis = new FileInputStream("excel/data.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Workbook workbook = new HSSFWorkbook(fis);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			for(int i=1; i<=sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				int memNo = (int)row.getCell(0).getNumericCellValue();
				String name = row.getCell(1).getStringCellValue();
				int age = (int)row.getCell(2).getNumericCellValue();
				
				MemberVo member = new MemberVo();
				member.setMemNo(memNo);
				member.setName(name);
				member.setAge(age);
				
				memberList.add(member);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return memberList;
	}
}
