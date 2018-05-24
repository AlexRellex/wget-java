package wget;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Html2Ascii extends FilterInputStream {
	protected Html2Ascii(InputStream in) {
		super(in);
	}

	public int read() throws IOException {
		int current_char = this.in.read();
		if (current_char == -1) {
			return -1;
		}
		
		while ((char)current_char == '<') {
			while ((char)current_char != '>') {
				current_char = this.in.read();
				if (current_char == -1) {
					return -1;
				}
			}
			current_char = this.in.read();
		}
		
		return current_char;
	}
}
