package indexation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import db.DB;
import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class Indexation {

	private static String INDEX_PATH = "/media/jonathan/Jonathan CROUZET/Documents/ProjetREI/results/";

	private static String STOPWORDS_FILENAME = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt";


	public static TreeMap<String, Normalizer> getNormalizers(){
		TreeMap<String, Normalizer> normalizers = new TreeMap<String, Normalizer>();

		try {
			Normalizer norm = new FrenchStemmer();
			normalizers.put(INDEX_PATH + norm.getClass().getName() + ".ind", norm);

			norm = new FrenchTokenizer();
			normalizers.put(INDEX_PATH + norm.getClass().getName() + ".ind", norm);

			norm = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			normalizers.put(INDEX_PATH + norm.getClass().getName() + "StopWord.ind", norm);

			norm = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
			normalizers.put(INDEX_PATH + norm.getClass().getName() + "StopWord.ind", norm);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return normalizers;
	}

	public static TreeMap<String, TreeMap<String, Double>> getInvertedFileWithWeights(TreeMap<String, String> files, Normalizer normalizer) throws IOException{
		TreeMap<String, TreeMap<String, Integer>> counts = new TreeMap<String, TreeMap<String, Integer>>();

		for(Map.Entry<String, String> entry : files.entrySet()){
			String id = entry.getKey();
			File file = new File(entry.getValue());

			List<String> words = normalizer.normalize(file);
			TreeMap<String, Integer> files_list;
			Integer file_word_count;
			for (String word : words) {
				word = word.toLowerCase();
				files_list = counts.getOrDefault(word, new TreeMap<String, Integer>());
				file_word_count = files_list.getOrDefault(id, 0);
				files_list.put(id, file_word_count + 1);
				counts.put(word, files_list);
			}
		}

		TreeMap<String, TreeMap<String, Double>> invertedFileWithWeights = new TreeMap<String, TreeMap<String, Double>>();
		for(Map.Entry<String, TreeMap<String, Integer>> entry : counts.entrySet()){
			String word = entry.getKey();
			TreeMap<String, Integer> word_counts = entry.getValue();
			TreeMap<String, Double> word_tfidf = new TreeMap<String, Double>();

			for(Map.Entry<String, Integer> sub_entry : word_counts.entrySet()){
				Double tfidf = sub_entry.getValue() / Math.log(files.size()/ word_counts.size());
				word_tfidf.put(sub_entry.getKey(), tfidf);
			}
			invertedFileWithWeights.put(word, word_tfidf);
		}

		return invertedFileWithWeights;
	}

	public static void saveInvertedFileWithWeights(TreeMap<String, TreeMap<String, Double>> invertedFileWithWeight, String outFile) throws IOException {
		try {
			FileWriter fw = new FileWriter (outFile);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			for(Map.Entry<String, TreeMap<String, Double>> entry : invertedFileWithWeight.entrySet()) {
				String word = entry.getKey();
				TreeMap<String, Double> files_list = entry.getValue();

				String line = word + "\t" + files_list.size() + "\t";
				for(Map.Entry<String, Double> file : files_list.entrySet()){
					String name = file.getKey();
					Double weight = file.getValue();
					line += name + "," + String.format(Locale.US, "%.2f", weight) + "\t";
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

	public static TreeMap<String, TreeMap<String, Double>> readInvertedFileWithWeights(String file) throws IOException {
		TreeMap<String, TreeMap<String, Double>> invertedFileWithWeight = new TreeMap<String, TreeMap<String, Double>>();

		try {
			FileReader fr = new FileReader (file);
			BufferedReader br = new BufferedReader (fr);

			String line;
			while ((line = br.readLine()) != null) {
				TreeMap<String, Double> wordWeights = new TreeMap<String, Double>();
				String[] split = line.split("\t");
				for (int i = 2; i<split.length; i++){
					String[] split2 = split[i].split(",");
					wordWeights.put(split2[0], Double.parseDouble(split2[1]));
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
			System.out.println("Creating DB dictionary");
			TreeMap<String, String> files = DB.getFiles();
			System.out.println("Number of files : " + files.size());
			DB.saveFiles(files);
			System.out.println("Done");

			System.out.println("Creating indexes");
			for(Map.Entry<String, Normalizer> entry : getNormalizers().entrySet()){
				Normalizer norm = entry.getValue();
				String file = entry.getKey();

				System.out.println("Index for " + norm.getClass().getName());
				TreeMap<String, TreeMap<String, Double>> invertedFileWithWeights = getInvertedFileWithWeights(files, norm);
				saveInvertedFileWithWeights(invertedFileWithWeights, file);
				System.out.println("Wrote :" + file);

				System.out.println("Reading index and saving it again (test)");
				TreeMap<String, TreeMap<String, Double>> test = readInvertedFileWithWeights(file);
				String file_test = file + ".test";
				saveInvertedFileWithWeights(test, file_test);
				System.out.println("Wrote :" + file_test);
			}
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
