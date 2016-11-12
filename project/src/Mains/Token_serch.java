package Mains;

import java.io.File;
import java.io.IOException;

import java.util.Scanner;

import tools.FrenchTokenizer;
import tools.Normalizer;


import Search.Search;

public class Token_serch {

	
	


		
		private static Scanner reader;

		public static void main(String[] args) throws IOException {
			String dir = "/home/anonyme/search-engine/project";
			String StopWords = dir +"/data/frenchST.txt";
			File index = new File(dir+"/result_indexation/index_tokenizer");
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(StopWords));
    		Normalizer normalizer =  tokenizerNoStopWords;
            // Calculate the execussion time
    		long durée = System.nanoTime();
    		//Request
			System.out.println(" Your Query Please!!! ");
			reader = new Scanner(System.in);  
			String query = reader.nextLine();
			System.out.println("Start searching for the query " + query);
			String res= dir+"/results/token/"+query+".txt";
			File sortie = new File (res);
			Search chercher  = new Search ( normalizer , index , query  , sortie) ;
			chercher.search();
			long dur = (System.nanoTime()-durée)/1000000;
			System.out.println( "La durée de la requête" + query + " est "  + dur + "ms");
			System.out.println("End");
		
		
	}
}
