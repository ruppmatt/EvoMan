package evoman.evo.io;

import java.io.*;
import java.util.*;

import evoman.evo.*;

public class FileManager implements Serializable {
	private static final long		serialVersionUID	= 1L;
	HashMap<String, ManagedFile>	_files				= new HashMap<String, ManagedFile>();
	protected EvoMan				_s					= null;
	protected String				_outdir;

	public FileManager(EvoMan s) {
		_s = s;
		String dir = _s.getConfig().S("output_dir");
		_outdir = dir == null ? "output/" : dir + "/";
		createOutDir();
	}

	private void createOutDir() {
		java.io.File dir = new java.io.File(_outdir);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public CommentStrippedInFile openCommentStrippedInFile(String path) {
		_s.debug("About to open file " + path);
		CommentStrippedInFile f = new CommentStrippedInFile(this, path);
		_files.put(path, f);
		return f;
	}

	public TextOutFile newTextFile(String path) {
		path = _outdir + path + ".gz";
		_s.debug("About to open new TextOutFile " + path);
		TextOutFile f = new TextOutFile(this, path);
		_files.put(path, f);
		return f;
	}

	public DelimitedOutFile newDelimitedFile(String path, String format) {
		path = _outdir + path + ".gz";
		_s.debug("About to open new DelimitedOutFile " + path);
		DelimitedOutFile f = new DelimitedOutFile(this, path, format);
		_files.put(path, f);
		return f;
	}

	public void close(String path) {
		ManagedFile f = _files.get(path);
		f.close();
		_files.remove(path);
	}

	public void closeAll() {
		for (String k : _files.keySet()) {
			_files.get(k).close();
		}
		_files.clear();
	}

	public ManagedFile getFile(String path) {
		return _files.get(path);
	}

	protected void remove(String path) {
		if (_files.containsKey(path)) {
			_files.remove(path);
		}
	}
}
