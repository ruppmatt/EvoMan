package evoman.evo.io;

import java.io.*;
import java.util.*;

public class CommentStrippedInFile extends ManagedFile {

	protected Scanner		_scanner;
	protected final String	eol			= "(\n|\r\n|$)";
	protected final String	delim_word	= "(\\s+|\\s*(#[^" + eol + "]*)+)+";
	protected final String	delim_line	= "(#[^" + eol + "]*)*" + eol + "+";

	public CommentStrippedInFile(FileManager fm, String path) {
		super(fm, path);
		try {
			_scanner = new Scanner(_file);
		} catch (FileNotFoundException e) {
			if (_fm != null) {
				_fm._s.getNotifier().fatal("Unable to open file: " + _file.getAbsolutePath() + " for input.",
						ErrorCode.FileIO);
			} else {
				System.err.println("Unable to open file: " + _file.getAbsolutePath());
			}
		}
		_scanner.useDelimiter(delim_word);
	}

	public boolean hasNextChar() {
		return _scanner.hasNext(".+");
	}

	public boolean hasNextWord() {
		return _scanner.hasNext();
	}

	public boolean hasNextLine() {
		_scanner.useDelimiter(delim_line);
		boolean retval = _scanner.hasNext();
		_scanner.useDelimiter(delim_word);
		return retval;
	}

	public boolean hasNextInt() {
		return _scanner.hasNextInt();
	}

	public boolean hasNextDouble() {
		return _scanner.hasNextDouble();
	}

	public String nextWord() {
		String tok = _scanner.next();
		return tok;
	}

	public String nextLine() {

		_scanner.useDelimiter(delim_line);
		String tok = _scanner.next();
		while (_scanner.hasNext(delim_line)) {
			_scanner.skip(delim_line);
		}
		if (tok.equals("")) {
			if (hasNextLine()) {
				tok = nextLine();
			}
		}
		_scanner.useDelimiter(delim_word);
		return tok;
	}

	public int nextInt() {
		int tok = _scanner.nextInt();
		return tok;
	}

	public double nextDouble() {
		double tok = _scanner.nextDouble();
		return tok;
	}

	public String nextChar() {
		_scanner.useDelimiter("");
		String tok = _scanner.next(".");
		_scanner.useDelimiter("\\s+");
		return tok;
	}

	@Override
	public void close() {
		_scanner.close();
		super.close();
	}

}
