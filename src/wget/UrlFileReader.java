package wget;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UrlFileReader {
	public static ArrayList<String> ReadFile(String file) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Fitxer urls no trobat");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error 2");
			//e.printStackTrace();
		}
		
		return lines;
	}
}
