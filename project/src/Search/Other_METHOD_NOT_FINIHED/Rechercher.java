package Search.Other_METHOD_NOT_FINIHED;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tools.FrenchStemmer;
import tools.Normalizer;

public class Rechercher {
	
	

	public File corpus;
	public  Normalizer normalizer;
	public   File index_; 
	public String query ;
	 File weights_query;


	public Rechercher (Normalizer normalizer , File index_ , String query , File weights_query)
	{
	
		this.query = query ;
		this.normalizer = normalizer;
		this.index_ = index_;
		this.weights_query=  weights_query;
		
	}
         

	
	
	
	
ArrayList<HashMap <String,Double >> getDocsWeights (String term) throws IOException

{
	
	ArrayList<HashMap <String,Double >>  a = new   	ArrayList<HashMap <String,Double >> ();  
	HashMap<String,Double>  b = new HashMap<String,Double>  ();
	InputStream fis1 = new FileInputStream(index_);
	InputStreamReader isr1 = new InputStreamReader(fis1);
	BufferedReader br1 = new BufferedReader(isr1);
	String line1 ;


	while ((line1 = br1.readLine()) != null){
		String[] line_parts = line1.split("  ");
		String  terms = line_parts[0];
		if(term.equals(terms))
		{
		
		String[] Ids =  line_parts[1].split(",");
		String[] Ws = line_parts[2].split(",") ;

		
		for (int i1 =0 ; i1<Ids.length ; i1++)
		{
			    b.put(Ids[i1], Double.parseDouble(Ws[i1]));
			    
			    
			    
			 //   System.out.print(Ids.length);
			   
		}
		a.add(b) ; 
		}

		}

	
	
	br1.close();
	return a;
}	
public ArrayList<ArrayList<HashMap<String,Double>>> getDocsWeightsTerms (String req)  throws IOException

{
	ArrayList<ArrayList<HashMap<String,Double>>> b = new ArrayList<ArrayList<HashMap<String,Double>>>();
	ArrayList<String> words = normalizer.normalize(query);
	HashSet<String> wordsInFiles = new HashSet<String>(words);
for ( String term : wordsInFiles)
{
	ArrayList<HashMap <String,Double >> a = getDocsWeights(term);
	b.add(a);
	
}

	return b;

}
public ArrayList<Double> FileToList (  File weights_query) throws NumberFormatException, IOException
{
	InputStream fis1 = new FileInputStream(weights_query);
	InputStreamReader isr1 = new InputStreamReader(fis1);
	BufferedReader br1 = new BufferedReader(isr1);
	String line1 ;
ArrayList<Double> weighs = new ArrayList<Double>();

	while ((line1 = br1.readLine()) != null){
		String[] line_parts = line1.split("\t");
	
		String  Ws = line_parts[1];
          weighs.add(Double.parseDouble(Ws));
	}
	
	System.out.print(weighs);
	br1.close();

	return weighs;
	}

	public static void main(String[] args) throws IOException {
	
		String dir = "/home/anonyme/search-engine/project";
		String corpus1 = dir+"/data_corpus_reduit";
		File corpus= new File (corpus1);
		String StopWords = dir +"/data/frenchST.txt";
	    File index = new File(dir+"/result_indexation/index_stemmer");
		Normalizer stemmerNoStopWords = new FrenchStemmer(new File(StopWords));
		Normalizer normalizer = stemmerNoStopWords;

		File weights = new File (dir +"/weighs_query/weights.txt");
		String  query =  " Charlie ";
		WeightsQuery poids = new WeightsQuery (normalizer ,corpus , index ,query , weights);;
		
		poids.saveWeights();
Rechercher ch  = new Rechercher (normalizer,index , query,weights);
		
	    ch.FileToList(weights);
		
	 
	}
	}

