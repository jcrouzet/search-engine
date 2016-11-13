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
		Normalizer stemmerNoStopWords = new FrenchStemmer(new File(StopWords));
		Normalizer normalizer = stemmerNoStopWords;
		File index = new File(dir+"/result_indexation/index_stemmer");
		//Time execussion
		long durée = System.nanoTime();
		// read the query
		System.out.println("Enter your request ");
		reader = new Scanner(System.in);  
	    String query = reader.nextLine();
		System.out.println("Start searching for the query " + query);
		String res= dir+"/results/stemmer/"+query+".txt";
		File sortie = new File (res);
		Search chercher  = new Search ( normalizer , index , query , sortie) ;
		chercher.search();
		long dur = (System.nanoTime()-durée)/1000000;
		System.out.println( "La durée de la requête" + query + " est "  + dur + "ms");
		System.out.println("End");
	}

}