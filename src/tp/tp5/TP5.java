package tp.tp5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TP5 {
	
	public static double getSimilarity(File file1, File file2){
		String line1, line2;
		String[] words1, words2;
		double sum12 = 0, square_sum1 = 0, square_sum2 = 0;
		
		try {
			// Set up buffered readers
		    InputStream fis1 = new FileInputStream(file1);
		    InputStreamReader isr1 = new InputStreamReader(fis1);
		    BufferedReader br1 = new BufferedReader(isr1);
		    InputStream fis2 = new FileInputStream(file2);
		    InputStreamReader isr2 = new InputStreamReader(fis2);
		    BufferedReader br2 = new BufferedReader(isr2);

		    line1 = br1.readLine();
		    line2 = br2.readLine();

		    while ( (line1 != null) && (line2 != null) ){
		    	words1 = line1.split("\t", -1);
		    	words2 = line2.split("\t", -1);

		    	if (words1[0].equals(words2[0])){
		    		sum12 += Double.parseDouble(words1[1]) * Double.parseDouble(words2[1]);
		    		square_sum1 += Double.parseDouble(words1[1]) * Double.parseDouble(words1[1]);
		    		square_sum2 += Double.parseDouble(words2[1]) * Double.parseDouble(words2[1]);

		    		line1 = br1.readLine();
		    		line2 = br2.readLine();
		    	}
		    	else if (words1[0].compareTo(words2[0]) < 0){ // words1[0] < words2[0]
		    		square_sum1 += Double.parseDouble(words1[1]) * Double.parseDouble(words1[1]);
		    		line1 = br1.readLine();
		    	}
		    	else { //words1[0] > words2[0]
		    		square_sum2 += Double.parseDouble(words2[1]) * Double.parseDouble(words2[1]);
		    		line2 = br2.readLine();
		    	}
		    }
		    if (line1 != null){
		    	while (line1 != null){
		    		words1 = line1.split("\t", -1);
		    		square_sum1 += Double.parseDouble(words1[1]) * Double.parseDouble(words1[1]);
		    		line1 = br1.readLine();
		    	}
		    }
		    if (line2 != null){
		    	while (line2 != null){
		    		words2 = line2.split("\t", -1);
		    		square_sum2 += Double.parseDouble(words2[1]) * Double.parseDouble(words2[1]);
		    		line2 = br2.readLine();
		    	}
		    }

		    br1.close();
		    br2.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return sum12/(Math.sqrt(square_sum1)*Math.sqrt(square_sum2));
	}

	public static void getSimilarDocuments(File file, Set<File> fileList){
		ArrayList<String> files = new ArrayList<>();
		ArrayList<Double> sim_values = new ArrayList<>();
		String curr_file;
		double curr_sim;
		int index;

		System.out.println("Closest files to " + file.getName());
		for(File file2 : fileList){
			curr_file = file2.getName();
			curr_sim = getSimilarity(file, file2);

			index = 0;
			while (index < sim_values.size()){
				if (curr_sim >= sim_values.get(index)){
					sim_values.add(index, curr_sim);
					files.add(index, curr_file);
					break;
				}
				index++;
			}
			if (index == sim_values.size()){
				sim_values.add(curr_sim);
				files.add(curr_file);
			}
		}

		index = 0;
		while (index < 10 && index < sim_values.size()){
			System.out.println(files.get(index) + " " + sim_values.get(index));
			index++;
		}
	}
	
	/**
	 * Main
	 */
	public static void main(String[] args) {
		try {
			String outDirName = "/home/jonathan/tmp";
			String testFile = "/texte.95-1.poids";
			Normalizer stemmerNoStopWords = new FrenchStemmer(new File("/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt"));
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File("/home/jonathan/Documents/ENSIIE/S5/TC3/TP/frenchST.txt"));
			Normalizer[] normalizers = {stemmerNoStopWords, tokenizerNoStopWords};

			for (Normalizer normalizer : normalizers) {
				String name = normalizer.getClass().getName();
				if (!normalizer.getStopWords().isEmpty()) {
					name += "_noSW";
				}
				
				// Store all files in a Set
				File dir = new File(new File(outDirName), name);
				File first_file;
				Set<File> files_set = new TreeSet<File>();
				if (dir.isDirectory()) {
					File[] files = dir.listFiles();
					
					first_file = new File(dir.getAbsolutePath() + testFile);
					for (File file : files){
						files_set.add(file);
					}
					
					System.out.println("Normalisation avec " + name + "(dossier :" + dir + ", " + files_set.size() + " fichiers)");
					getSimilarDocuments(first_file, files_set);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
