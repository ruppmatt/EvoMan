package evoman.evo.io;

import java.util.*;

public class DelimitedOutFile extends GZOutFile {

	protected final char				_delim;
	protected final String				_format;
	protected final int					_numFields;
	protected final ArrayList<String>	_fieldNames	= new ArrayList<String>();
	protected final String              _format_string;

	public DelimitedOutFile(FileManager fm, String path, String format) {
		this(fm, path, format, ',');
	}

	public DelimitedOutFile(FileManager fm, String path, String format, char delim) {
		super(fm, path);
		_format = format;
		_delim = delim;
		_format_string = parseFormat(format);
		_numFields = _fieldNames.size();
		printHeader();
	}

	protected String parseFormat(String format) {
		Scanner scan = new Scanner(format).useDelimiter(",");
		StringBuffer fmtstr = new StringBuffer();
		while(scan.hasNext()) {
			String f = scan.next();
			String[] toks = f.split("%");
			fmtstr.append("%" + toks[1]);
			if (scan.hasNext()){
				fmtstr.append(_delim);
			}
			_fieldNames.add(toks[0]);
		}
		return fmtstr.toString();
	}

	protected void printHeader() {
		write("# " + _format + ManagedFile.endl);
		for (int k = 0; k < _fieldNames.size(); k++) {
			if (k > 0) {
				write(_delim + _fieldNames.get(k));
			} else {
				write(_fieldNames.get(k));
			}
		}
		write(ManagedFile.endl);
	}

	@Override
	public void write(String s) {
		try {
			_buf.write(s);
		} catch (Exception e) {
			if (_fm != null) {
				_fm._s.getNotifier().fatal("Unable to write to the file: " + getPath(), ErrorCode.FileIO);
			}
		}
	}

	public void write(Object... fields) {
		write(String.format(_format_string, fields)+ManagedFile.endl);
	}

}
