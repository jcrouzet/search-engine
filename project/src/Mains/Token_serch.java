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
		// Stop words
		String StopWords = dir +"/data/frenchST.txt"; 
		Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(StopWords));
		Normalizer normalizer =  tokenizerNoStopWords;
		// Index
		File index = new File(dir+"/result_indexation/index_tokenizer");
		// Calculate the execussion time
		long durée = System.nanoTime();
		//Request
		System.out.println(" Your Query Please!!! ");
		reader = new Scanner(System.in);  
		String query = reader.nextLine();
		System.out.println("Start searching for the query " + query);
		String res= dir+"/results/token/"+query+".txt";
		// Save the results 
		File sortie = new File (res);
		Search chercher  = new Search ( normalizer , index , query  , sortie) ;
		chercher.search();
		long dur = (System.nanoTime()-durée)/1000000;
		System.out.println( "La durée de la requête" + query + " est "  + dur + "ms");
		System.out.println("Thank you!!! Enjoy the results ");

	}
}
