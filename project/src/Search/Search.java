package Search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import tools.Normalizer;

public class Search {

	public File index;
	
	public Normalizer normalizer;

	public Search ( File index)
		{
			

			this.index = index;
		}
	
	// Calculate the Similarity 
	
	// for this code i was inspirited from this web site
	// file:///home/amal/Documents/master%20aic/extraction/cours/hors-prof/Tf-Idf%20and%20Cosine%20similarity%20|%20Seeking%20Wisdom.html
	// it's in python but helped me to understand the principle of implementaion
	
	private double CosineSimilarity(String [] request, File index) throws FileNotFoundException
	{
		
		if (index.isDirectory())
		{
		File [] files =	index.listFiles();
		
		for (int i= 0 ; i< files.length ; i++)
		{
			
		}
		}
		
		return (Double) null ;
	}
}
