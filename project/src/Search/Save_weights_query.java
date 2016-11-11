package Search;

import java.io.*;
import index.Index;


import java.util.*;

import tools.FrenchTokenizer;
import tools.Normalizer


public class Save_weights_query {










	public File corpus;
	public Normalizer normalizer;
	public static File index_; 
	public String query ;

	public Save_weights_query (Normalizer normalizer, File corpus , File index_ , String query)
	{
		this.normalizer = normalizer;

		this.corpus = corpus;
		this.index_ = index_;
		this.query = query ;
	}
	
	ArrayList<String> terms  = normalizer.normalize(query); 
	public HashMap<String,Integer> getTerm () throws IOException
	{	
		
		
		// 
		ArrayList<String>words = normalizer.normalize(query);
				HashSet<String> wordInFiles = new HashSet<String>(words) ;
	
 HashMap <String , Integer> tf =  new HashMap <String ,Integer> () ; 

		if (corpus.isDirectory()) {
	 for (String term : wordInFiles) {
		 term =  term.toLowerCase();
		Integer value =  Collections.frequency(words, term);
			
		 tf.put (term , value) ;
		 
		}
	
	 }
	return tf;
	}
	
	
	public static HashMap<String, Integer> getDFIndex() throws IOException {
		HashMap<String, Integer> dfIndex = new LinkedHashMap<>();
		
		BufferedReader br1 = new BufferedReader(new FileReader(index_));
		String line;
		while ((line = br1.readLine()) != null) {
			String[] postings = line.split("\t");
		
		 dfIndex.put(String.valueOf(postings[0]), Integer.valueOf(postings[1]));

		}
		br1.close();
		return dfIndex;
	}
}





