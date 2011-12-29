package evoman.evo.io;

import java.io.*;

import evoman.evo.*;

public class Notifier implements Serializable {
	private static final long	serialVersionUID	= 1L;
	public EvoMan				_s;
	public Boolean				_terminating		= false;

	public Notifier(EvoMan s) {
		_s = s;
	}

	public String suffix() {
		return "";
	}

	public final String	endl	= System.getProperty("line.separator");

	public void debug(String s) {
		System.err.println("Debug" + suffix() + ": " + s);
	}

	public void fatal(String s) {
		fatal(s, ErrorCode.Unknown);
	}

	public void fatal(String s, ErrorCode c) {
		System.err.println("Fatal error" + suffix() + ":" + s);
		System.err.println("[Code " + c.code().toString() + "]");
		if (_s == null || _terminating) {
			System.exit(c.code());
		} else {
			_terminating = true;
			_s.terminate(c);
		}
	}

	public void warn(String s) {
		System.err.println("WARNING" + suffix() + ": " + s);
	}

	public void notify(String s) {
		System.out.println(suffix() + ":" + s);
	}

}
