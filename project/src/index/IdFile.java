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
		 
		
		File f = new File("/home/anonyme/search-engine/project/data/corpus_reduit");
	
		
		String [ ] names = f.list();
		File file_id = new File("/home/anonyme/search-engine/project/data/file_id");
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
		for (i = 0 ; i< names.length ; i++)
		{
			
			id.put((Encoding.base62(Integer.parseInt(names[i].substring(0,names[i].length()-4)))), names[i]);
			
			id.put(names[i],Encoding.base62(Integer.parseInt(names[i].substring(0,names[i].length()-4))));
			// System.out.print(names.get(i) + "\t" + id.get(names.get(i))  + "\n");
			bw.write((Encoding.base62(Integer.parseInt(names[i].substring(0,names[i].length()-4)))) + "\t" + id.get((Encoding.base62(Integer.parseInt(names[i].substring(0,names[i].length()-4)))))  + "\n"  );

		}
		bw.close();
		}
		
		
	}
}
