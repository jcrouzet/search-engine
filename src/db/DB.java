package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DB {
	private static String DB_PATH = "/media/jonathan/Jonathan CROUZET/Documents/ProjetREI/";
	
	public static String getDBPath(){
		return DB_PATH;
	}
	
	private static List<String> getFilesFromDir(List<String> fileNames, Path dir) {
	    try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        for (Path path : stream) {
	            if(path.toFile().isDirectory()) {
	            	getFilesFromDir(fileNames, path);
	            } else {
	                fileNames.add(path.toAbsolutePath().toString());
	            }
	        }
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	    return fileNames;
	}
	
	public static TreeMap<String, String> getFiles(){
		TreeMap<String, String> files = new TreeMap<String, String>();;
		Integer indice = 1;
		
		String path = DB_PATH + "subindex";
		String path_text = DB_PATH + "text";
		File directory = new File(path);
		List<String> xml_files = new ArrayList<String>();
		
//get all the paths of files from a directory which the xml files are stocked
		
		if (directory.isDirectory()) {
			xml_files = getFilesFromDir(xml_files, directory.toPath());
		}

// source: https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm	
		for (String file_name : xml_files){
			try {
				File file = new File(file_name);
				String name = file.getName();
				String year = name.substring(0, 4);
				String month = name.substring(4, 6);
				String day = name.substring(6, 8);
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("doc");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode; 
						
						String value = path_text + "/" + year + "/" + month + "/" + day + "/" + eElement.getAttribute("id") + ".txt";
						files.put(indice.toString(), value);
						indice += 1;
					}
				} }
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		return files;
	}
	
	public static void saveFiles(TreeMap<String, String> files){
		try {
			FileWriter fw = new FileWriter (DB_PATH + "filesIndices.txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			for(Map.Entry<String, String> entry : files.entrySet()) {
				  String line = entry.getKey() + "\t" + entry.getValue();
				  out.println(line);
			}
			out.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static TreeMap<String, String> loadFiles(){
		TreeMap<String, String> files = new TreeMap<String, String>();
		try {
			FileReader fr = new FileReader (DB_PATH + "filesIndices.txt");
			BufferedReader br = new BufferedReader (fr);

			String line;
		    while ((line = br.readLine()) != null) {
		       String[] split = line.split("\t");
		       files.put(split[0], split[1]);
		    }
		    br.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return files;
	}
	
	public static void main(String[] args){
		saveFiles(getFiles());
		TreeMap<String, String> files = loadFiles();
	}

}
