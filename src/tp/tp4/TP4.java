package tp.tp4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TP4 {
	
	public static void mergeInvertedFiles(File invertedFile1, File invertedFile2, File mergedInvertedFile) throws IOException{
		String line1, line2, word, files_list;
		String[] words1, words2;
		Integer files_count;
		
		try {
			// Set up buffered readers
		    InputStream fis1 = new FileInputStream(invertedFile1);
		    InputStreamReader isr1 = new InputStreamReader(fis1);
		    BufferedReader br1 = new BufferedReader(isr1);
		    InputStream fis2 = new FileInputStream(invertedFile2);
		    InputStreamReader isr2 = new InputStreamReader(fis2);
		    BufferedReader br2 = new BufferedReader(isr2);
		    
		    // Set up print writer
		    FileWriter fw = new FileWriter(mergedInvertedFile);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
		    
			line1 = br1.readLine();
	    	line2 = br2.readLine();
	    	
		    while ( (line1 != null) && (line2 != null) ){
		       words1 = line1.split("\t", -1);
		       words2 = line2.split("\t", -1);
		       
		       if (words1[0].equals(words2[0])){
		    	   word = words1[0];
		    	   files_count = Integer.parseInt(words1[1]) + Integer.parseInt(words2[1]);
		    	   files_list = words1[2] + "," + words2[2];
		    	   
		    	   out.println(word + "\t" + files_count + "\t" + files_list); 
		    	   
		    	   line1 = br1.readLine();
		    	   line2 = br2.readLine();
		    			   
		       }
		       else if (words1[0].compareTo(words2[0]) < 0){ // words1[0] < words2[0]
		    	   out.println(line1);
		    	   line1 = br1.readLine();
		       }
		       else { //words1[0] > words2[0]
		    	   out.println(line2);
		    	   line2 = br2.readLine();
		       }
		    }
		    if (line1 != null){
		    	while (line1 != null){
		    		out.println(line1);
		    		line1 = br1.readLine();
		    	}
		    }
		    if (line2 != null){
		    	while (line2 != null){
		    		out.println(line2);
		    		line2 = br2.readLine();
		    	}
		    }
		    
		    br1.close();
		    br2.close();
		    out.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	/**
	 * Main
	 */
	public static void main(String[] args) {
		try {
			String if1 = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/index1_utf8.ind";
			String if2 = "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/index2_utf8.ind";
			String ifOut =  "/home/jonathan/Documents/ENSIIE/S5/TC3/TP/index_utf8_merged.ind";
			
			System.out.println("Merging...");
			mergeInvertedFiles(new File(if1), new File(if2), new File(ifOut));
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
