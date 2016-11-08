import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tools.Normalizer;


public class methods {

	
	
	public static HashMap<String, Integer> getTermFrequencies(String query , Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		
		
		ArrayList<String> wordss = normalizer.normalize (query); 
		
		HashSet<String> words = new HashSet<String>(wordss) ;
		Integer number;

		for (String word : words) {
			word = word.toLowerCase();
			number = hits.get(word);
			if (number == null) {
				hits.put(word, 1);
			} else {
				hits.put(word, ++number);
			}
		}
		return hits;
	}
	
	
		
}
