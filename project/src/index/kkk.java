package index;





import java.io.*;



import java.util.*;

import tools.FrenchTokenizer;
import tools.Normalizer;
public class kkk {


	public File corpus;
	public Normalizer normalizer;
	public static File index_; 
	public String query ;
	
	
	
	
	
	HashMap <String , Integer> tf =  new HashMap <String ,Integer> () ; 

	ArrayList<String> words = normalizer.normalize(query);
	HashSet<String> wordsInFiles = new HashSet<String>(words) ;


	if (corpus.isDirectory()) {

		for (String term : wordsInFiles) {
			term =  term.toLowerCase();
			Integer value =  Collections.frequency(words, term);

			tf.put (term , value) ;

		}


}





