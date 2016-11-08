package index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;



public class IdFile {
	public static void main(String[] args) throws IOException {
		 
		
		File f = new File("/home/amal/search-engine/project/data/corpus_reduit");
		ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
		   Collections.sort(names);
		//   System.out.print(names.size());
		
		File file_id = new File("/home/amal/search-engine/project/data/file_id");
		if (!file_id.exists()) {
			file_id.createNewFile();
		}
		else {
			file_id.delete();
			file_id.createNewFile();
			FileWriter fw = new FileWriter(file_id.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
		
		HashMap<String, String> id = new HashMap<String, String> ();
		
		int i = 0; 
		for (i = 0 ; i<names.size(); i++)
		{
			
			
			id.put(names.get(i), names.get(i).substring(0,names.get(i).length()-4));
			// System.out.print(names.get(i) + "\t" + id.get(names.get(i))  + "\n");
			bw.write(names.get(i) + "\t" + id.get(names.get(i))  + "\n");

		}
		bw.close();
		}
		
		
	}
}
