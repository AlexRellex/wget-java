package wget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileWriter extends Thread {
	private String url;
	private String original_filename = "";
	private String extension = "";
	private Boolean ascii_flag;
	private Boolean gzip_flag;
	private Boolean zip_flag;
	
	public void run() {
		this.fromUrl();
	}

	public FileWriter(String _url, Boolean _ascii_flag, Boolean _gzip_flag, Boolean _zip_flag) {
		this.ascii_flag = _ascii_flag;
		this.gzip_flag = _gzip_flag;
		this.zip_flag = _zip_flag;
		this.url = _url;
	}
	
	public void fromUrl() {
		try {
			URL url_obj = new URL(url);
			URLConnection connection = url_obj.openConnection();
			connection.connect();
			
			String url_path = url_obj.getPath();
			String filename = url_path.substring(url_path.lastIndexOf('/') + 1);
			if (filename == "") {
				filename = "index.html";
			}
			
			int i = filename.lastIndexOf('.');
			if (i > 0) {
			     this.extension = filename.substring(i+1, filename.length());
			}
			
			this.original_filename = filename;
			
			System.out.println("Downloading: " + filename);
			
			InputStream in = connection.getInputStream();
			
			FileOutputStream file_out = this.createTarget(filename);
	
			if (this.gzip_flag) {
				// -gz [-z] [-a]
				GZIPOutputStream gzip_out = new GZIPOutputStream(file_out);
				if (this.zip_flag) {
					ZipOutputStream zip_out = new ZipOutputStream(gzip_out);
					if (this.ascii_flag && (this.extension.equals(new String("html")))) {
						// -gz -z -a
						this.copyInputStreamToOutputStream(new Html2Ascii(in), zip_out);
					} else {
						// -gz -z
						this.copyInputStreamToOutputStream(in, zip_out);
					}
				} else {
					if (this.ascii_flag && (this.extension.equals(new String("html")))) {
						// -gz -a
						this.copyInputStreamToOutputStream(new Html2Ascii(in), gzip_out);
					} else {
						// -gz
						this.copyInputStreamToOutputStream(in, gzip_out);
					}
				}
				
			} else if (this.zip_flag) {
				// -z [-a]
				ZipOutputStream zip_out = new ZipOutputStream(file_out);
				if (this.ascii_flag && (this.extension.equals(new String("html"))))  {
					// -z -a
					this.copyInputStreamToOutputStream(new Html2Ascii(in), zip_out);
				} else {
					// -z
					this.copyInputStreamToOutputStream(in, zip_out);
				}
			} else if (this.ascii_flag && (this.extension.equals(new String("html")))) {
				// -a
				this.copyInputStreamToOutputStream(new Html2Ascii(in), file_out);
			} else {
				// Sense paràmetres
				this.copyInputStreamToOutputStream(in, file_out);
			}
						
		} catch (MalformedURLException e) {
			System.out.println("URL malformada: " + e);
		} catch (IOException e) {
			System.out.println("Error de E/S: " + e);
		}
	}
	
	private void copyInputStreamToOutputStream( InputStream in, OutputStream out ) {
	    try {
	    	if (this.zip_flag) {
	    		System.out.println(this.original_filename);
	    		((ZipOutputStream)out).putNextEntry(new ZipEntry(this.original_filename));
	    	}
	    	
	        int buf;
	        while((buf=in.read()) != -1){
	            out.write(buf);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
			System.out.println("Error 5");
	        e.printStackTrace();
	    }
	}
	
	private FileOutputStream createTarget(String filename) throws IOException {
		if (this.ascii_flag && (this.extension.equals(new String("html")))) {
			filename = filename + ".asc";
			this.original_filename = filename;
		}
		if (this.zip_flag) {
			filename = filename + ".zip";
		}
		if (this.gzip_flag) {
			filename = filename + ".gz";
		}
				
		File target_file = new File(filename);
		
		if (!target_file.exists()) {
			target_file.createNewFile();
	
		}
		return new FileOutputStream(target_file);
	}
}
