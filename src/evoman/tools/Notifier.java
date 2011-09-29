package evoman.tools;

import java.io.*;

/**
 * 
 * @author ruppmatt
 *
 *		A Notifier is meant to allow objects to share a particular path for writing output and error.
 */
public class Notifier {

	protected PrintStream out = System.out;
	protected PrintStream err = System.err;
	
	public Notifier(){
	}
	
	/**
	 * Print a message
	 * @param msg
	 */
	public void notify(String msg){
		out.println(msg);
	}
	
	/**
	 * Print a warning
	 * @param msg
	 */
	public void warn(String msg){
		err.println("Warning: " + msg);
	}
	
	/**
	 * Print a fatal error message.
	 * @param msg
	 * @param e
	 */
	public void fatal(String msg, ErrCode e){
		err.println("Fatal error [" + e.code() + "]: " + msg);
	}
}
