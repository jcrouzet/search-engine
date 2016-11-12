package Search;





import index.Encoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;






import tools.Normalizer;

public class Search {




	public  Normalizer normalizer;
	public   File index_; 
	public String query ;
public File results;


	public Search (Normalizer normalizer, File index_ , String query , File results )
	{
		this.normalizer = normalizer;
	
		this.index_ = index_;
		this.query = query ;
	
		this.results = results ;
		
	}
	


	public void search () throws IOException
	
	{
		
	HashMap <String , Integer> tf =  new HashMap <String ,Integer> () ; 
	ArrayList<String> words = normalizer.normalize(query.toLowerCase());
	HashSet<String> wordsInFiles = new HashSet<String>(words) ; 
	 double sum_wr=0;
	 double wr;
			for (String term : wordsInFiles)
			{
				term =  term.toLowerCase();
				Integer value =  Collections.frequency(words, term);
                tf.put (term , value) ;
			}
 HashMap<String, Pair> sims =new HashMap<String, Pair>();
			try {
				InputStream fis1 = new FileInputStream(index_);
				InputStreamReader isr1 = new InputStreamReader(fis1);
				BufferedReader br1 = new BufferedReader(isr1);
				

				String line1 ;


				while ((line1 = br1.readLine()) != null) {
					String[] line_parts = line1.split("  ");
					String term= line_parts[0];
					if(!wordsInFiles.contains(term))
						continue;
						
					String[] Ids =  line_parts[1].split(",");
					String[] Ws = line_parts[2].split(",");

					wr = tf.get(term)*Math.log10(9842/Ids.length);
		
					for(int i = 0; i <Ids.length; i++){
						Pair p = sims.get(Ids[i]);
						if (p==null){p = new Pair(0,0);}
						Pair p1=new Pair(p.getLeft() + Math.pow(Double.parseDouble(Ws[i]), 2), 
										 p.getRight() + wr*Double.parseDouble(Ws[i]));
						sims.put(Ids[i],p1);
						sum_wr=sum_wr + Math.pow(wr, 2);
					}
				
				}
br1.close();

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
				
				// write the results
				
				if (!results.exists()) {
					results.createNewFile();
				}
			
					FileWriter fw = new FileWriter (results);
					BufferedWriter bw = new BufferedWriter (fw);
					PrintWriter out = new PrintWriter (bw);

				index1 = 0;
				
				while (index1 <20 && index1 < sim_values.size()){
					
					System.out.println(Encoding.base62(files.get(index1))+".txt" + "\t" + "\t" + sim_values.get(index1) + "\n");
					
out.print(Encoding.base62(files.get(index1))+".txt" + "\t" + "\t" + sim_values.get(index1) + "\n");
					index1++;
				}
				
				
				out.close();
				
				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}		
		}
	
}



