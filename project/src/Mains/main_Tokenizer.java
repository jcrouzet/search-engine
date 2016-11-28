package Mains;



import java.io.File;

import java.io.IOException;
import index.Index;
import tools.FrenchStemmer;
//import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class main_Tokenizer {

	
	public static void main(String[] args) throws IOException {
		 

			String	dir = "/home/anonyme/search-engine/project";
			String StopWords = dir + "/data/frenchST.txt";
			File corpus = new File ( dir+"/data/corpus_reduit");
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(StopWords));
	    		Normalizer normalizer =  tokenizerNoStopWords;
		     	String path_index = dir + "/result_indexation/index_tokenizer";
		     	File index_ = new File(path_index);
			    Index index = new Index(normalizer ,corpus , index_);
		
			index.getIndex();
	
		index.getIndex();
		
		
	
		
	}
}