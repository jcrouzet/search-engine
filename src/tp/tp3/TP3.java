package tp.tp3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TP3 {
	
	public static TreeMap<String, TreeSet<String>> getInvertedFile(File dir, Normalizer normalizer) throws IOException {
		TreeMap<String, TreeSet<String>> invertedFile = new TreeMap<String, TreeSet<String>>();
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			
			for (File file : files){
				HashSet<String> words = new HashSet<String>(normalizer.normalize(file));
				TreeSet<String> files_list;
				for (String word : words) {
					word = word.toLowerCase();
					files_list = invertedFile.getOrDefault(word, new TreeSet<String>());
					files_list.add(file.getName());
					invertedFile.put(word, files_list);
				}
			}
		
		}
		
		return invertedFile;
	}
	
	public static void saveInvertedFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile) throws IOException {
		try {
			FileWriter fw = new FileWriter (outFile);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			for(Map.Entry<String,TreeSet<String>> entry : invertedFile.entrySet()) {
				  String word = entry.getKey();
				  TreeSet<String> files_list = entry.getValue();
				  
				  String line = word + "\t" + files_list.size() + "\t";
				  for(String file : files_list){
					  line += file + ",";
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

	public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(File dir, Normalizer normalizer) throws IOException{
		TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = new TreeMap<String, TreeMap<String, Integer>>();
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			
			for (File file : files){
				HashSet<String> words = new HashSet<String>(normalizer.normalize(file));
				TreeMap<String, Integer> files_list;
				Integer file_word_count;
				for (String word : words) {
					word = word.toLowerCase();
					files_list = invertedFileWithWeights.getOrDefault(word, new TreeMap<String, Integer>());
					file_word_count = files_list.getOrDefault(file.getName(), 0);
					files_list.put(file.getName(), file_word_count + 1);
					invertedFileWithWeights.put(word, files_list);
				}
			}
		
		}
		
		return invertedFileWithWeights;
	}

	/**
	 * Main
	 */
	public static void main(String[] args) {
		try {
			String dir = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/lemonde_utf8/";
			String outFile = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/inverted_file";
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			
			System.out.println("Inverted file");
			TreeMap<String, TreeSet<String>> invertedFile1 = getInvertedFile(new File(dir), stemmerAllWords);
			saveInvertedFile(invertedFile1, new File(outFile + "_" + stemmerAllWords.getClass().getName() + ".ind"));
			
			TreeMap<String, TreeSet<String>> invertedFile2 = getInvertedFile(new File(dir), tokenizerAllWords);
			saveInvertedFile(invertedFile2, new File(outFile + "_" + tokenizerAllWords.getClass().getName() + ".ind"));
			
			System.out.println("Inverted with weights");
			TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = getInvertedFileWithWeights(new File(dir), stemmerAllWords);
			
			System.out.println("Done : IF.size =" + invertedFile1.size() + " IFW.size =" + invertedFileWithWeights.size());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
