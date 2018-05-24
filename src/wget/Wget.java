package wget;

import java.util.ArrayList;

public class Wget {

	public static void main(String[] args) {
		String url_file = null;
		Boolean ascii_flag = false;
		Boolean zip_flag = false;
		Boolean gzip_flag = false;

		
		// Parsear parametros
		for (int i=0; i<args.length; i++) {
			if (args[i].startsWith("-")) {
				switch (args[i]) {
				case "-f":
					url_file = args[i+1];
					i++;
					break;
				case "-a":
					ascii_flag = true;
					break;
				case "-z":
					zip_flag = true;
					break;
				case "-gz":
					gzip_flag = true;
					break;
				}
			}
		}
		
		if (url_file == null) {
			System.out.println("Fitxer no passat com a parametre");
			System.exit(1);
		}

		ArrayList<String> file_lines = UrlFileReader.ReadFile(url_file);
		for (String line : file_lines) {
			FileWriter fw = new FileWriter(line, ascii_flag, gzip_flag, zip_flag);
			fw.start();
		}
	}
}
