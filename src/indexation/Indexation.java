package indexation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

import db.DB;

public class Indexation {

	private static String INDEX_PATH = "/media/jonathan/Jonathan CROUZET/Documents/ProjetREI/results/";
	
	// private static String STOPWORDS_FILENAME = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt";
	

	public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(TreeMap<String, String> files, Normalizer normalizer) throws IOException{
		TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = new TreeMap<String, TreeMap<String, Integer>>();

		for(Map.Entry<String, String> entry : files.entrySet()){
			String id = entry.getKey();
			File file = new File(entry.getValue());

			HashSet<String> words = new HashSet<String>(normalizer.normalize(file));
			TreeMap<String, Integer> files_list;
			Integer file_word_count;
			for (String word : words) {
				word = word.toLowerCase();
				files_list = invertedFileWithWeights.getOrDefault(word, new TreeMap<String, Integer>());
				file_word_count = files_list.getOrDefault(id, 0);
				files_list.put(id, file_word_count + 1);
				invertedFileWithWeights.put(word, files_list);
			}
		}
		
		return invertedFileWithWeights;
	}
	
	public static void saveInvertedFileWithWeights(TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeight, String outFile) throws IOException {
		try {
			FileWriter fw = new FileWriter (outFile);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			for(Map.Entry<String, TreeMap<String, Integer>> entry : invertedFileWithWeight.entrySet()) {
				  String word = entry.getKey();
				  TreeMap<String, Integer> files_list = entry.getValue();
				  
				  String line = word + "\t" + files_list.size() + "\t";
				  for(Map.Entry<String, Integer> file : files_list.entrySet()){
					  String name = file.getKey();
					  Integer weight = file.getValue();
					  line += name + "," + weight + "\t";
				  }
				  
				  line = line.substring(0, line.length()-1);
				  out.println(line);
			}
			out.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static TreeMap<String, TreeMap<String, Integer>> readInvertedFileWithWeights(String file) throws IOException {
		TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeight = new TreeMap<String, TreeMap<String, Integer>>();

		try {
			FileReader fr = new FileReader (file);
			BufferedReader br = new BufferedReader (fr);

			String line;
			while ((line = br.readLine()) != null) {
				TreeMap<String, Integer> wordWeights = new TreeMap<String, Integer>();
				String[] split = line.split("\t");
				for (int i = 2; i<split.length; i++){
					String[] split2 = split[i].split(",");
					wordWeights.put(split2[0], Integer.parseInt(split2[1]));
				}
				invertedFileWithWeight.put(split[0], wordWeights);
			}
			br.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}

		return invertedFileWithWeight;
	}


	public static void main(String[] args) {
		try {
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer tokenizerAllWords = new FrenchTokenizer();
				
			System.out.println("Creating DB dictionary");
			TreeMap<String, String> files = DB.getFiles();
			System.out.println("Number of files : " + files.size());
			DB.saveFiles(files);
			System.out.println("Done");
			
			System.out.println("Creating index");
			TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = getInvertedFileWithWeights(files, stemmerAllWords);
			String out_file = INDEX_PATH + stemmerAllWords.getClass().getName() + ".ind";
			saveInvertedFileWithWeights(invertedFileWithWeights, out_file);
			System.out.println("Done");
			
			System.out.println("Reading index and saving it again (test)");
			TreeMap<String, TreeMap<String, Integer>> test = readInvertedFileWithWeights(out_file);
			saveInvertedFileWithWeights(test, out_file + ".test");
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
