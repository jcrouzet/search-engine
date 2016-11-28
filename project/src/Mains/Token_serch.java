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
			File index = new File(dir+"/result_indexation/index_tokenizer" +
					"");
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(StopWords));
    		Normalizer normalizer =  tokenizerNoStopWords;
			
			// Lire la requete 
			System.out.println("Enter your request ");
			reader = new Scanner(System.in);  
			String query = reader.nextLine();
			System.out.println("Start searching for the query " + query);
			Search chercher  = new Search ( normalizer , index , query) ;
			chercher.search();
			System.out.println("End");
		
		
	}
}
