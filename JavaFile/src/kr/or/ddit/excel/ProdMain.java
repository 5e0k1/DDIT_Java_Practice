package kr.or.ddit.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import kr.or.ddit.util.ScanUtil;
import lombok.Data;

public class ProdMain {
	public static void main(String[] args) {
		ProdMain obj = new ProdMain();
		obj.init();
		obj.createCart();
		obj.process();
	}
	List<Prod> prodList;
	List<Cart> cartList;
	int cartNo;
	boolean autoSave = true;
	
	public void process() {
		
		int sel =0;
		while(true) {
			if(sel==0) {
				System.out.println("1. 상품");
				System.out.println("2. 구매");
				System.out.println("3. 종료");
				sel = ScanUtil.select();
				if(sel == 3) {
					saveExcel();
					break;
				}
			}
			
			if(sel==1) {
				System.out.println("0. 자동저장변경 : 현재상태("+autoSave+")");
				System.out.println("1. 상품 리스트 출력");
				System.out.println("2. 상품 추가");
				System.out.println("3. 상품 삭제");
				System.out.println("4. 상품 수정");
				System.out.println("5. 홈");
				
				int sel2 = ScanUtil.select();
				
				if(sel2 ==0) autoSave = !autoSave;
				if(sel2 ==1) printProdList();
				if(sel2 ==2) addProd();
				if(sel2 ==3) deleteProd();
				if(sel2 ==4) updateProd();
				if(sel2 ==5 ) sel=0;
			}
			if(sel==2) {
				System.out.println("1. 상품 구매");
				System.out.println("2. 구매 내역 출력");
				System.out.println("3. 총 판매 금액");
				System.out.println("4. 홈");
				
				int sel2 = ScanUtil.select();
				if(sel2 == 1) buyProd();
				if(sel2 == 2) printCart();
				if(sel2 == 3) calcPrice();
				if(sel2 == 4) sel=0;
			}
		}
	}
	
	public void calcPrice() {
		Map<Integer, Integer> prodPrice = new HashMap<>();	//상품별 가격을 저장
		int sum=0;
		
		for(Prod p : prodList) {
			int prodNo = p.getProdNo();
			int price = p.getPrice();
			prodPrice.put(prodNo, price);
		}
		
		for(Cart c : cartList) {
			if(c.getCartNo()==cartNo) {
				int prodNo = c.getProdNo();
				sum += c.getCnt()*prodPrice.get(prodNo); //같은 주문번호(+상품번호)에 대해 수량*가격
			}
		}
		
		System.out.println("주문번호 - "+cartNo+"번 님 현재 구매액"+sum+"원");
	}
	
	
	public void printCart() {
		System.out.println("주문번호\t상품번호\t수량");
		for(Cart cart : cartList) {
			System.out.println(cart.getCartNo()+"\t"+cart.getProdNo()+"\t"+cart.getCnt());
		}
	}
	
	
	public void buyProd() {
		printProdList();
		if(prodList.size()==0) {
			System.out.println("등록된 상품이 없음");
			return;
		}
		int cnt=0;
		int prodNo = ScanUtil.nextInt("구매할 상품 번호 : ");
		Cart cart =null;
		Prod prod =null;
		for(Prod p : prodList) {
			for(Cart c : cartList) { 									//해당 카트번호에 이미 동일한 상품이 존재하는지 체크
				if(c.getCartNo()==cartNo && c.getProdNo()== prodNo) {
					cart = c;											// 해당 주문에 수량을 변경하기위해 주소 참조
					cnt = c.getCnt();									// 이미 구매한 수량 적립
				}
			}
			if(p.getProdNo() == prodNo) {								//상품 존재유무 체크해서 prod에 대입
				prod =p;										
			}
		}
		if(prod==null) {
			System.out.println("존재하지 않는 상품");
			return;
		}
		cnt += ScanUtil.nextInt("구매할 수량"); 							//카트 유무와 관계없이 += 으로 새로 구매할 양을 더해줌
		if(cart!=null) {
			cart.setCartNo(cartNo);
			cart.setProdNo(prodNo);
			cart.setCnt(cnt);
			if(autoSave) saveExcel();
		}
		if(cart == null) {
			cart = new Cart(); 											//해당 상품에 대한 구매 기록이 없을때 null
			cart.setCartNo(cartNo);
			cart.setProdNo(prodNo);
			cart.setCnt(cnt);
			cartList.add(cart);
			if(autoSave) saveExcel();
		}
		
	}
	
	
	public void createCart() { 											//실행시 카트번호 생성
		int cartNo = 0;
		if(cartList.size()==0) {										//리스트가 비어있으면 1번 존재하면 마지막번호+1
			cartNo = 1;
			return;
		}
		for(Cart c : cartList) {
			if(cartNo < c.getCartNo()) cartNo = c.getCartNo(); 
		}
		cartNo++;
		this.cartNo = cartNo;
	}
	
	
	
	public void updateProd() {
		printProdList();
		int prodNo = ScanUtil.nextInt("변경할 상품 번호 : ");
		Prod prod = null;
		for(Prod p : prodList) {
			if(p.getProdNo()==prodNo) prod=p;
		}
		if(prod == null) System.out.println("해당 상품이 존재하지 않음");
		else {
			prod.setName(ScanUtil.nextLine("변경할 상품명 : "));
			prod.setPrice(ScanUtil.nextInt("변경할 가격 : "));
			System.out.println("변경 완료@@");
			if(autoSave) saveExcel();
		}
	}
	
	
	public void deleteProd() {
		printProdList();
		int prodNo =ScanUtil.nextInt("삭제할 상품번호 : ");
		for(Prod p : prodList) {
			if(prodNo==p.getProdNo()) {
				prodList.remove(p);
				System.out.println("삭제완료@@");
				if(autoSave) saveExcel();
				return;
			}
		}
		System.out.println("해당 상품이 존재하지 않음");
	}
	
	
	public void addProd() {
		int prodNo = 0;
		for(int i=0; i<prodList.size();i++) {
			if(prodNo<prodList.get(i).getProdNo()) prodNo =prodList.get(i).getProdNo(); 
		}
		prodNo++;
		Prod prod = new Prod();
		prod.setProdNo(prodNo);
		prod.setName(ScanUtil.nextLine("상품명 : "));
		prod.setPrice(ScanUtil.nextInt("상품가격 : "));
		
		prodList.add(prod);
		
		if(autoSave) saveExcel();
	}
	
	public void printProdList() {
		System.out.println("상품번호\t상품명\t상품가격");
		if(prodList.size()==0) {
			System.out.println("등록된 상품이 없습니다@@");
			return;
		}
		for(Prod p : prodList) {
			System.out.println(p.getProdNo()+"\t"+p.getName()+"\t"+p.getPrice());
		}
	}
	
	public void saveExcel() {
		Workbook workbook = new XSSFWorkbook(); 		//엑셀파일 오픈
		
		Sheet sheet1 = workbook.createSheet("prod"); 	//prod시트 생성
		Row index1 = sheet1.createRow(0);				//prod첫줄 (컬럼) 생성
		Cell prodI1 =  index1.createCell(0);
		Cell prodI2 =  index1.createCell(1);
		Cell prodI3 =  index1.createCell(2);
		
		prodI1.setCellValue("상품번호");
		prodI2.setCellValue("상품명");
		prodI3.setCellValue("가격");
		
		for(int i=1;i<prodList.size()+1;i++) {			//prodList의 prod들 각 셀에 저장
			Prod prod = prodList.get(i-1);
			Row prodR = sheet1.createRow(i);
			
			Cell prodC1 =prodR.createCell(0);
			Cell prodC2 =prodR.createCell(1);
			Cell prodC3 =prodR.createCell(2);
			
			prodC1.setCellValue(prod.getProdNo());
			prodC2.setCellValue(prod.getName());
			prodC3.setCellValue(prod.getPrice());
		}
		Sheet sheet2 = workbook.createSheet("cart");	//cart 시트 생성
		Row index2 = sheet2.createRow(0);				//cart 첫줄(컬럼) 생성
		Cell cartI1 = index2.createCell(0);
		Cell cartI2 = index2.createCell(1);
		Cell cartI3 = index2.createCell(2);
		
		cartI1.setCellValue("주문번호");
		cartI2.setCellValue("상품번호");
		cartI3.setCellValue("수량");
		
		for(int i=1; i<cartList.size()+1;i++) {			//cartList의 cart들 각 셀에 저장
			Cart cart = cartList.get(i-1);
			Row cartR = sheet2.createRow(i);
			
			Cell cartC1 =cartR.createCell(0);
			Cell cartC2 =cartR.createCell(1);
			Cell cartC3 =cartR.createCell(2);
			
			cartC1.setCellValue(cart.getCartNo());
			cartC2.setCellValue(cart.getProdNo());
			cartC3.setCellValue(cart.getCnt());
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("excel/prod.xlsx");
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("저장완료@@");
		}
	}
	
	
	public void init() {
		// 엑셀 읽어서 list 불러오기.
		// 엑셀 파일이 없다면 new ArrayList();
		
		// 엑셀 파일명 excel/prod.xlsx
		File file = new File("excel/prod.xlsx");
		if(!file.exists()) {
			prodList = new ArrayList<Prod>();
			cartList = new ArrayList<Cart>();
			return;
		}
		prodList = new ArrayList<Prod>();
		cartList = new ArrayList<Cart>();
		try {
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet1 =workbook.getSheetAt(0);//prod 시트 불러오기
			for(int i=1;i<=sheet1.getLastRowNum();i++) {
				Row row = sheet1.getRow(i);
				Prod prod = new Prod();

				int prodNo  = (int)row.getCell(0).getNumericCellValue();
				String name = row.getCell(1).getStringCellValue();
				int price   = (int)row.getCell(2).getNumericCellValue();
				
				prod.setProdNo(prodNo);
				prod.setName(name);
				prod.setPrice(price);
				
				prodList.add(prod);
			}
			
			Sheet sheet2 =workbook.getSheetAt(1);//cart 시트 불러오기
			for(int i=1;i<=sheet2.getLastRowNum();i++) {
				Row row = sheet2.getRow(i);
				Cart cart = new Cart();
				
				int cartNo = (int)row.getCell(0).getNumericCellValue();
				int prodNo = (int)row.getCell(1).getNumericCellValue();
				int cnt    = (int)row.getCell(2).getNumericCellValue();
				
				cart.setCartNo(cartNo);
				cart.setProdNo(prodNo);
				cart.setCnt(cnt);
				
				cartList.add(cart);
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
class Cart{
	private int cartNo;
	private int prodNo;
	private int cnt;
}

@Data
class Prod{
	private int prodNo;
	private String name;
	private int price;
}