package evoman.evo.io;

public class TextOutFile extends GZOutFile {

	public TextOutFile(FileManager fm, String path) {
		super(fm, path);
	}

	@Override
	public void write(String s) {
		try {
			_buf.write(s);
		} catch (Exception e) {
			if (_fm != null) {
				_fm._s.getNotifier().fatal("Unable to write to file: " + getPath(), ErrorCode.FileIO);
			}
		}
	}
}
