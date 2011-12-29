package evoman.evo.io;


public abstract class ManagedFile {
	protected static final String	endl	= System.getProperty("line.separator");
	protected java.io.File			_file;
	protected FileManager			_fm;

	public ManagedFile(String path) {
		this(null, path);
	}

	public ManagedFile(FileManager fm, String path) {
		_fm = fm;
		_file = new java.io.File(path);
	}

	public FileManager getFileManager() {
		return _fm;
	}

	public String getPath() {
		return _file.getPath();
	}

	public void close() {
		if (_fm != null) {
			_fm.remove(_file.getPath());
		}
	}

}
