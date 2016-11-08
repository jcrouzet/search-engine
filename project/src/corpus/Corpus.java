package corpus;




import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;




// this class it s for  creating the corpus containing the xml's id

public class Corpus {



	public static void main(String[] args) throws IOException {

// my variables
		
		String path = "/home/amal/subindex/2015";
		String path_text = "/home/amal/2015";
		File directory = new File(path);
		File directory_text = new File(path_text);
		List<String> fichiers_xml = new ArrayList<String>(); 
		List<String> fichiers_text = new ArrayList<String>(); 
		
//get all the paths of files from a directory which the xml files are stocked
		
		if (directory.isDirectory()) {

			File[] dList = directory.listFiles();


			for (File file : dList){
				File [] subdList = file.listFiles();
				for (File j : subdList) {
					fichiers_xml.add(j.getAbsolutePath());

				}

			}

		}

//get all the path files from a directory which the text files are stocked
				
		if (directory_text.isDirectory()) {

			File[] dList_text = directory_text.listFiles();
			//	File[] dList_text_1= directory_text.listFiles();

			for (File file1 : dList_text){
				File [] subdList_text = file1.listFiles();
				for (File t : subdList_text) {
					File [] subdList_text1 = t.listFiles();
					for (File k: subdList_text1) {		
						fichiers_text.add(k.getAbsolutePath());

					}

				}

			}

		}
// parse xml files
		ArrayList<File> files_xml = new ArrayList<File>();
		for (int i=0 ;  i<fichiers_xml.size() ; i++) {
			files_xml.add(new File(fichiers_xml.get(i)));
		}
		ArrayList<String> id = new ArrayList<String>();
// source: https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm	
		for (File file_xml : files_xml){
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file_xml);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("doc");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode; 

						id.add(eElement.getAttribute("id"));
					}
				} }
			catch (Exception e) {
				e.printStackTrace();
			}

		}

// create a corpus
// files which their names is the id of xml files

		ArrayList<File> corpus = new ArrayList<File>();

		for (String ide: id) {
			for (String text : fichiers_text)
			{
				if (text.contains(ide))
				{
					corpus.add(new File (text));

				}
			}
		}
		
//my new directory corpus containing my files
	
		String outDirName = "/home/amal/search-engine/project/data/corpus";


		File dir= new File(outDirName);
		if(!dir.exists()){
			dir.mkdir();}

		for (File file : corpus)
			try {
			    Files.copy(
			        Paths.get(file.getAbsolutePath()),
			        Paths.get(new File(outDirName , file.getName()).getAbsolutePath()));
			} catch (IOException e) {
			    e.printStackTrace();
			} 

		

		}
	}

