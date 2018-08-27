package core;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseReview {
	String path = "";
	public ParseReview() {
		
	}
	
public Document[] chooseAndParse() throws ParserConfigurationException{
		
JFileChooser chooser = new JFileChooser();//creates file chooser Object
chooser.setMultiSelectionEnabled(true);//gives possibility to select more that 1 SR file
		
File[] files = null;//declare and initialising the file array
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			 files = chooser.getSelectedFiles();
		}
		//File review = chooser.getSelectedFile().getAbsoluteFile();
			Document[] rm5 = new Document[files.length];//here, the parsed SR files will be stored.
			
			path = files[0].getAbsoluteFile().getParent();///path where data comes from
			if (path.equals(null)) {
				path = "";
			}
			for (int i = 0; i < files.length; i++) {//loop to process all selected files
				
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setIgnoringComments(true);
				builderFactory.setIgnoringElementContentWhitespace(true);
				
				try {
					DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
					rm5[i] = dBuilder.parse(files[i]);//parsing the current SR and writing it to the current array position
					rm5[i].normalize();
					System.out.println("parsed");
				} catch (ParserConfigurationException e) {
				} catch (SAXException e) {
				} catch (IOException e) {
				} 
			}
			
			
			return rm5;//returns array with parsed SR documents
}
	
	
	
	protected int numberOfIncludedStudies(Document rm5){
		// Gets root element of parsed XML document, moves to included studies section and returns length of included studies list.
		NodeList rootList = rm5.getElementsByTagName("COCHRANE_REVIEW");
		Element rootElement = (Element) rootList.item(0);
		
		NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
		Element characteristicsOfStudiesElement = (Element) characteristicsOfStudiesList.item(0);
		
		NodeList characteristicsOfIncludedStudiesList = 
				characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
		Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesList.item(0);
		
		NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
		return includedStudiesList.getLength();
	}
	
	

}
