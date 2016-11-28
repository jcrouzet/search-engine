package Mains;

import index.Index;

import java.io.File;
import java.io.IOException;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;


public class main_stemmer {

	
	public static void main(String[] args) throws IOException {
		 
		String	dir = "/home/anonyme/search-engine/project";
		String StopWords = dir + "/data/frenchST.txt";
		File corpus = new File ( dir+"/data/corpus_reduit");
		Normalizer stemmerNoStopWords = new FrenchStemmer(new File(StopWords));
    		Normalizer normalizer = stemmerNoStopWords;
	     	String path_index = dir + "/result_indexation/index_stemmer";
	     	File index_ = new File(path_index);
		    Index index = new Index(normalizer ,corpus , index_);
	
		index.getIndex();
	}
	
	

	
}

