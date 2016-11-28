package Mains;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import Search.Search;

import tools.FrenchStemmer;
import tools.Normalizer;

public class Serarch_stemmer {

	
	private static Scanner reader;

	public static void main(String[] args) throws IOException {
		String dir = "/home/anonyme/search-engine/project";
		String StopWords = dir +"/data/frenchST.txt";
		File index = new File(dir+"/result_indexation/index_stemmer");
		Normalizer stemmerNoStopWords = new FrenchStemmer(new File(StopWords));
		Normalizer normalizer = stemmerNoStopWords;
		
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