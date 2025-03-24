package kr.or.ddit.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlExample01 {
	public static void main(String[] args) throws Exception {
		XmlExample01 obj = new XmlExample01();
		obj.process();
	}
	
	/*
	 * 
	 * DoM 방식으로 XML 읽기
	 * 
	 * 	XML 문서를 트리 구조로 메모리에 로드하여 다룸
	 * 
	 * 	XML 각 요소(Element)를 노드 형태로 저장하여 탐색 및 수정 가능.
	 * 
	 * 	장점 : xml을 반복 탐색 하거나 특정 요소를 찾기가 편함.
	 * 	단점 : xml 파일 크기가 크면 메모리 사용량이 많아짐.
	 * 
	 * Sax 방식으로 XML 읽기
	 * 	
	 * 	XML 데이터를 한줄씩 읽어서 처리하기 때문에 대용량 XML 처리에 적합
	 *  XML 읽으면서 이벤트 발생 시키므로 속도 빠름
	 *  랜덤 접근 불가능
	 *  사용이 다소 복잡.
	 * 
	 */
	
	public void process() throws Exception {
		File xmlFile = new File("xml/book.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(xmlFile);
		
		Element root = document.getDocumentElement();
		System.out.println("Root Element : "+root.getNodeName());
		
		// root 밑에 있는 자식(요소)
//		NodeList nodeList = root.getChildNodes();
//		
//		for(int i=0; i<nodeList.getLength();i++) {
//			Node node = nodeList.item(i);
//			
//			if(node.getNodeType() == Node.ELEMENT_NODE) {
//				Element book = (Element)node;
//				
//				String issn = book.getAttribute("issn");
//				System.out.println("issn : "+issn);
//				
//				String name =book.getElementsByTagName("name").item(0).getTextContent();
//				System.out.println("name : "+name);
//				
//				String writer =book.getElementsByTagName("writer").item(0).getTextContent();
//				System.out.println("writer : "+writer);
//				
//				String pub =book.getElementsByTagName("pub").item(0).getTextContent();
//				System.out.println("pub : "+pub);
//				System.out.println();
//			}
//		}
		
		NodeList bookList =root.getElementsByTagName("book");
		for(int i=0; i<bookList.getLength();i++) {
			Node node =bookList.item(i);
			
			Element book = (Element)node;
			
			NamedNodeMap atts = book.getAttributes();
			for(int j=0; j<atts.getLength();j++) {
				Node attNode = atts.item(0);
				if(attNode == null)continue;
				String att = attNode.getNodeName();
				String val = attNode.getNodeValue();
				System.out.println(att+" : "+val);
			}
		}
		
	}
}
