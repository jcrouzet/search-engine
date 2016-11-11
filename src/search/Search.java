package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import search.Pair;
import indexation.Encoding;
import tools.Normalizer;

public class Search {

	public static Normalizer normalizer;
	public static File index_;

	public static void search (Normalizer normalizer, File index , HashMap<String, Integer> requete){
		double sum_wr=0;
		double wr;

		HashMap<String, Pair> sims =new HashMap<String, Pair>();
		try {
			InputStream fis1 = new FileInputStream(index);
			InputStreamReader isr1 = new InputStreamReader(fis1);
			BufferedReader br1 = new BufferedReader(isr1);
			String line1 = br1.readLine();

			while ( (line1 != null)){
				String[] words = line1.split("\t", -1);
				String[] Ids = words[1].replace("[","").replace("]","").split("  ", -1);
				String[] Ws = words[2].replace("[","").replace("]","").split("  ", -1);
				wr = requete.getOrDefault(words[0], 0)*Math.log10(10000/Ids.length);
				for(int i = 0; i <Ids.length; i++){
					Pair p = sims.get(Ids[i]);
					if (p==null){p = new Pair(0,0);}
					Pair p1=new Pair(p.getLeft() + Math.pow(Double.parseDouble(Ws[i]), 2), 
									 p.getRight() + wr*Double.parseDouble(Ws[i]));
					sims.put(Ids[i],p1);
					sum_wr=sum_wr + Math.pow(wr, 2);
				}
				line1 = br1.readLine();
			}

			ArrayList<String> files = new ArrayList<>();
			ArrayList<Double> sim_values = new ArrayList<>();
			String curr_file;
			double curr_sim;
			int index1;
			for (Map.Entry<String, Pair> sim : sims.entrySet()){
				curr_file = sim.getKey();
				curr_sim =sim.getValue().getRight()/(Math.sqrt(sim.getValue().getLeft())*Math.sqrt(sum_wr));
				index1 = 0;
				while (index1 < sim_values.size()){
					if (curr_sim >= sim_values.get(index1)){
						sim_values.add(index1, curr_sim);
						files.add(index1, curr_file);
						break;
					}
					index1++;
				}
				if (index1 == sim_values.size()){
					sim_values.add(curr_sim);
					files.add(curr_file);
				}
			}
			index1 = 0;
			while (index1 < 10 && index1 < sim_values.size()){
				System.out.println(Encoding.base62(files.get(index1)) + " " + sim_values.get(index1));
				index1++;
			}
			br1.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static void main(String[] args) {
		System.out.println("Start");
		int number;
		HashMap<String, Integer> requete = new HashMap<String, Integer>();
		for(int i = 0; i <args.length; i++)	{
			number = requete.getOrDefault(args[i].toLowerCase(), 0);
			requete.put(args[i].toLowerCase(), number + 1);
		}
		System.out.println(requete.getOrDefault("charlie", 0));
		File f = new File("C:\\Users\\Pasini\\Desktop\\search-engine-master\\project\\result_indexation\\index_tokenizer");
		search(normalizer, f,requete);
		System.out.println("End");
	}	
}


