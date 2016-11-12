package index;



import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


import java.util.*;

import tools.Normalizer;
public class Index {


	public File corpus;
	public Normalizer normalizer;
	public File index_;

	public Index(Normalizer normalizer, File corpus , File index_)
	{
		this.normalizer = normalizer;

		this.corpus = corpus;
		this.index_ = index_;
	}

	public void  getIndex() throws IOException {

		// Create the index
		// if file doesnt exists, then create it

		if (!index_.exists()) {
			index_.createNewFile();
		}
		else {
			index_.delete();
			index_.createNewFile();


			FileWriter fw = new FileWriter(index_.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			//  get the occurence of each term in each document

			//The hash  is then used as the index at which the data associated with the key is stored. 
			//the transformation of the key into its hash code is performed automatically.

			HashMap<String, HashMap<String, Integer>> tf = new HashMap<String, HashMap<String, Integer>>();
			int NumberOfDocument = 0 ;

			if (corpus.isDirectory()) {

				// get all the files of the directory in order to Calculate of Tf (frequency terms)

				String [] files_corpus = corpus.list();

				for (String file : files_corpus)
				{

					ArrayList<String>words = normalizer.normalize(new File(corpus, file));
					//we use a hashset for no have  2 occurences of the term
					HashSet<String> wordsInFiles = new HashSet<String>(words) ;
					for(String word: wordsInFiles)
					{
						word = word.toLowerCase();
						if (word==null)
							continue;
						HashMap<String, Integer> value = tf.get(word); 
						if(value == null) 
							value = new HashMap<String, Integer>();
						//The frequency(Collection<?>, Object) method is used to get the number of elements
						//in the specified collection equal to the specified object.
						// example: https://www.tutorialspoint.com/java/util/collections_frequency.htm
						value.put(file, Collections.frequency(words, word));
						tf.put(word,value);
					}
					NumberOfDocument ++ ;

				}		
			}
			// Calculation of weights tf_idf 
			// list of terms on orders alphabetic (that means the terms of the tf calculated before)
			Set<String> terms= new TreeMap<String, HashMap<String, Integer>>(tf).keySet();

			// the result of tf will be like this term document1 document2 document3  1 2 3 
			// the idea is for each term in the hash map tf we will use the value which is the document
			// and the frequency like that:
			// suppose that we have 3 terms in 4 documents like this:
			// amal d1 d2 d3  1 2 3
			// targhi d1 d3   1 4 
			// engine d2      2
			// so we will extract the value of each term (document,frequency) 
			// first case for example for the term amal (d1,1) (d1,2)  (d1,3) 
			// and for each hashMap we will extract the keys (documents) d1 d2 d3
			// calculate extarct the term frequncy is the value of each hashmap
			//for calculate the Document frequency it is simply the size of the hashmap for example for amal is 3

			for ( String term : terms)
			{
				//  Create a List of the documents and their occurences of the terms the value of tf is 
				// a hashMap (Doc,occurence)

				HashMap<String, Float> tfIdfs = new HashMap<String, Float>();
				HashMap<String, Integer> documents_tf = tf.get(term);

				for(Map.Entry<String, Integer> document_tf : documents_tf.entrySet())
				{

					String document = document_tf.getKey();
					Integer  term_Frequency = documents_tf.get(document);
					Integer Df = documents_tf.size();
					Double weight= (double)term_Frequency * Math.log((double)(NumberOfDocument / Df));
					// truncate : for optimizing memory in the index 
					BigDecimal tfIdf = new BigDecimal(weight);
					tfIdf = tfIdf.setScale(2, RoundingMode.HALF_UP);
					// Convert a decimal to base 62 for reduce the size of the index
					tfIdfs.put(Encoding.base62(Integer.parseInt(document.substring(0,document.length()-4))), tfIdf.floatValue());
				}		
				bw.write(term+ "  " + tfIdfs.keySet().toString().replaceAll("[\\[\\] ]", "") + "  " + tfIdfs.values().toString().replaceAll("[\\[\\] ]", "")+ "  " +"\n");
			}
			bw.close();
		} 
	}
}