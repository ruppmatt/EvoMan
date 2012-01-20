package evoman.evo;


import evoict.io.*;
import evoman.evo.structs.*;



public class EvoMan {

	protected EvoPool		_root;
	protected FileManager	_fm;
	protected Notifier		_notifier;



	public EvoMan(String[] args) {
		_notifier = new Notifier();
		_fm = new FileManager(_notifier, "output/");
	}



	public void debug(String msg) {
		_notifier.debug(msg);
	}



	public void fatal(String msg) {
		_notifier.fatal(msg);
	}



	public void fatal(String msg, ErrorCode c) {
		_notifier.fatal(msg, c);
	}



	public void notify(String msg) {
		_notifier.notify(msg);
	}



	public void warn(String msg) {
		_notifier.warn(msg);
	}



	public void terminate(ErrorCode c) {
		_fm.closeAll();
	}



	public FileManager getFileManager() {
		return _fm;
	}



	public Notifier getNotifier() {
		return _notifier;
	}

}
