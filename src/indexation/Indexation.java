package indexation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class Indexation {

	private static String DB_PATH = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/lemonde_utf8/";
	
	private static String RESULTS_PATH = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/lemonde_utf8/";
	
	private static String STOPWORDS_FILENAME = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt";


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
	
	public static void saveInvertedFileWithWeights(TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeight, File outFile) throws IOException {
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
					  line += name + "," + weight + ",";
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


	public static void main(String[] args) {
		try {
			String dir = DB_PATH;
			String outDir = RESULTS_PATH;
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			
			
			System.out.println("Inverted with weights");
			TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeights = getInvertedFileWithWeights(new File(dir), stemmerAllWords);
			saveInvertedFileWithWeights(invertedFileWithWeights, new File(outDir + "/" + stemmerAllWords.getClass().getName() + ".ind"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
