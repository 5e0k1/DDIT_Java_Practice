package kr.or.ddit.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLExample02 {
	public static void main(String[] args) throws Exception {
		XMLExample02 obj = new XMLExample02();
		obj.process();
	}
	
	public void process() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.newDocument();
		
		Element root = doc.createElement("books2");
		
		Element book1 = doc.createElement("book");
		book1.setAttribute("issn", "1000");
		
		Element name1 = doc.createElement("name");
		Element writer1 = doc.createElement("writer");
		Element pub1 = doc.createElement("pub");
		
		name1.setTextContent("책1");
		writer1.setTextContent("작가1");
		pub1.setTextContent("출판사1");
		
		book1.appendChild(name1);
		book1.appendChild(writer1);
		book1.appendChild(pub1);
		
		root.appendChild(book1);

		Element book2 = doc.createElement("book");
		book2.setAttribute("issn", "1001");
		
		Element name2 = doc.createElement("name");
		Element writer2 = doc.createElement("writer");
		Element pub2 = doc.createElement("pub");
		
		name2.setTextContent("책2");
		writer2.setTextContent("작가2");
		pub2.setTextContent("출판사2");
		
		book2.appendChild(name2);
		book2.appendChild(writer2);
		book2.appendChild(pub2);
		
		root.appendChild(book2);
		
		doc.appendChild(root);
		
		TransformerFactory transformerfactory = TransformerFactory.newInstance();
		Transformer transformer = transformerfactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT	, "yes");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("xml/book2.xml"));
		
		transformer.transform(source, result);
	}
}
