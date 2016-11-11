package search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import db.DB;
import indexation.Indexation;
import tools.Normalizer;

public class Search {

	private static String RESULTS_PATH = "/media/jonathan/Jonathan CROUZET/Documents/ProjetREI/results/";

	public static List<String> getQueries(){
		List<String> queries = new ArrayList<>();
		queries.add("Charlie Hedbo");
		queries.add("volcan");
		queries.add("playoffs NBA");
		queries.add("accidents d'avion");
		queries.add("Laïcité");
		queries.add("élections législatives");
		queries.add("Sepp Blatter");
		queries.add("budget de la défense");
		queries.add("Galaxy S6");
		queries.add("Kurdes");

		return queries;
	}

	public static void search (String query, Normalizer normalizer, TreeMap<String, TreeMap<String, Double>> index, TreeMap<String, String> files){
		// Query processing
		List<String> words = normalizer.normalize(query);

		TreeMap<String, Integer> words_counts = new TreeMap<String, Integer>();
		Integer count;
		for(String word : words){
			count = words_counts.getOrDefault(word, 0);
			words_counts.put(word, count + 1);
		}

		// Compute similarity
		double sum_wr=0;
		double wr;
		HashMap<String, Pair> sims =new HashMap<String, Pair>();
		for (Map.Entry<String, TreeMap<String, Double>> ligne : index.entrySet()){

			wr = words_counts.getOrDefault(ligne.getKey(), 0)*Math.log10((files.size()+1)/ligne.getValue().size());
			for (Map.Entry<String, Double> id : ligne.getValue().entrySet()){
				Pair p = sims.get(id.getKey());
				if (p==null){p = new Pair(0,0);}
				Pair p1=new Pair(p.getLeft() + Math.pow(id.getValue(),2),p.getRight() + wr*(id.getValue()));
				sims.put(id.getKey(),p1);
			}
			sum_wr=sum_wr + Math.pow(wr, 2);
		}

		ArrayList<String> files_names = new ArrayList<>();
		ArrayList<Double> sim_values = new ArrayList<>();
		String curr_file;
		double curr_sim;
		int i;
		for (Map.Entry<String, Pair> sim : sims.entrySet()){
			curr_file = sim.getKey();
			curr_sim =sim.getValue().getRight()/(Math.sqrt(sim.getValue().getLeft())*Math.sqrt(sum_wr));
			i = 0;
			while (i < sim_values.size()){
				if (curr_sim >= sim_values.get(i)){
					sim_values.add(i, curr_sim);
					files_names.add(i, curr_file);
					break;
				}
				i++;
			}
			if (i == sim_values.size()){
				sim_values.add(curr_sim);
				files_names.add(curr_file);
			}
		}

		// Write results
		String norm_name;
		if (normalizer.getStopWords().isEmpty()){
			norm_name = normalizer.getClass().getName();
		}
		else {
			norm_name = normalizer.getClass().getName() + "StopWord_";
		}
		File file = new File(RESULTS_PATH + norm_name + query + ".res");
		try {
			FileWriter fw = new FileWriter (file);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			i = 0;
			while (i < 20 && i < sim_values.size()){
				out.println(sim_values.get(i) + "\t" + files.get(files_names.get(i)) );
				i++;
			}
			out.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}


	public static void main(String[] args) {
		TreeMap<String, String> files = DB.loadFiles();
		TreeMap<String, Normalizer> normalizers = Indexation.getNormalizers();
		List<String> queries = getQueries();

		for (Map.Entry<String, Normalizer> norm : normalizers.entrySet()){
			try {
				System.out.println("Loading index " + norm.getKey());
				TreeMap<String, TreeMap<String, Double>> index = Indexation.readInvertedFileWithWeights(norm.getKey());
				System.out.println("Querying");
				for(String query : queries){
					search(query, norm.getValue(), index, files);
				}
				System.out.println("Done");
			}
			catch (Exception e){
				System.out.println(e.toString());
			}
		}
		System.out.println("End");
	}	
}


