package evoman.evo.io;

import java.io.*;
import java.util.zip.*;

public abstract class GZOutFile extends ManagedFile {

	protected BufferedWriter	_buf;

	public GZOutFile(FileManager fm, String path) {
		super(fm, path);

		try {
			FileOutputStream ofstream = new FileOutputStream(_file);
			GZIPOutputStream gzstream = new GZIPOutputStream(ofstream);
			OutputStreamWriter writer = new OutputStreamWriter(gzstream);
			_buf = new BufferedWriter(writer);
		} catch (Exception e) {
			if (_fm._s != null) {
				_fm._s.getNotifier().fatal("Unable to open file for output: " + path, ErrorCode.FileIO);
			}
		}
	}

	abstract public void write(String s);

	@Override
	public void close() {
		if (_fm != null) {
			_fm._s.debug("About to close file: " + _file.getAbsolutePath());
		}
		try {
			_buf.close();
			super.close();
		} catch (Exception e) {
			if (_fm._s != null) {
				_fm._s.getNotifier().fatal("Unable to close file: " + getPath(), ErrorCode.FileIO);
			}
		}
		if (_fm != null) {
			_fm._s.debug("Successfully closed file: " + _file.getAbsolutePath());
		}
	}

	@Override
	protected void finalize() throws Throwable{
		_buf.close();
	}
}