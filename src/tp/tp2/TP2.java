package tp.tp2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

import tp.tp1.TP1;

/**
 * TP 2
 * @author xtannier
 *
 */
public class TP2 {
	
	/**
	 * Le répertoire du corpus
	 */
	protected static String DIRNAME = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/lemonde_utf8/";
	/**
	 * Le fichier contenant les mots vides
	 */
	private static String STOPWORDS_FILENAME = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt";


	
	/**
	 * exo 2.1 : Calcule le df, c'est-à-dire le nombre de documents
	 * pour chaque mot apparaissant dans la collection. Le mot
	 * "à" doit ainsi apparaître dans 88 documents, le mot
	 * "ministère" dans 4 documents.
	 */
	public static HashMap<String, Integer> getDocumentFrequency(File dir, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			
			for (File file : files){
				HashSet<String> words = new HashSet<String>(normalizer.normalize(file));
				Integer number;
				for (String word : words) {
					word = word.toLowerCase();
					number = hits.getOrDefault(word, 0);
					hits.put(word, number + 1);
				}
			}
		
		}
		return hits;
	}
	
	/**
	 * exo 2.4 : Calcule le tf.idf des mots d'un fichier en fonction
	 * des df déjà calculés, du nombre de documents et de
	 * la méthode de normalisation.
	 */
	public static HashMap<String, Double> getTfIdf(File file, HashMap<String, Integer> dfs, int documentNumber, Normalizer normalizer) throws IOException {
		HashMap<String, Double> tfIdfs = new HashMap<String, Double>();
		
		HashMap<String, Integer> tf = TP1.getTermFrequencies(file, normalizer);
		
		tf.forEach((key,value) -> tfIdfs.put(key, (double)value * Math.log((double)documentNumber / (double)dfs.get(key))));
		
		return tfIdfs;
	}
	
	/**
	 * exo 2.5 : Crée, pour chaque fichier d'un répertoire, un nouveau
	 * fichier contenant les poids de chaque mot. Ce fichier prendra
	 * la forme de deux colonnes (mot et poids) séparées par une tabulation.
	 * Les mots devront être placés par ordre alphabétique.
	 * Les nouveaux fichiers auront pour extension .poids
	 * et seront écrits dans le répertoire outDirName.
	 */
	private static void getWeightFiles(File inDir, File outDir, Normalizer normalizer) throws IOException {
		if (inDir.isDirectory()) {
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			
			File[] files = inDir.listFiles();
			HashMap<String, Integer> dfs = getDocumentFrequency(inDir, normalizer);
			
			for (File file : files){
				HashMap<String, Double> tfIdfs = getTfIdf(file, dfs, files.length, normalizer);
				TreeSet<String> words = new TreeSet<String>(tfIdfs.keySet());
				
			    try {
					FileWriter fw = new FileWriter (new File(outDir, file.getName().replaceAll(".txt$", ".poids")));
					BufferedWriter bw = new BufferedWriter (fw);
					PrintWriter out = new PrintWriter (bw);
	
					for (String word : words){
						out.println(word + "\t" + tfIdfs.get(word));
					}
					out.close();
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
			}
		}
	}


	/**
	 * Main, appels de toutes les méthodes des exercices du TP2. 
	 */
	public static void main(String[] args) {
		try {
			String outDirName = "/home/jonathan/tmp";
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
			Normalizer[] normalizers = {stemmerAllWords, stemmerNoStopWords, 
					tokenizerAllWords, tokenizerNoStopWords};
			for (Normalizer normalizer : normalizers) {
				String name = normalizer.getClass().getName();
				if (!normalizer.getStopWords().isEmpty()) {
					name += "_noSW";
				}
				System.out.println("Normalisation avec " + name);
				System.out.println(getDocumentFrequency(new File(DIRNAME), normalizer).size());
				System.out.println("GetWeightFiles avec " + name);
				getWeightFiles(new File(DIRNAME), new File(new File(outDirName), name), normalizer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
