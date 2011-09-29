package evoman.tools;

/**
 * 
 * @author ruppmatt
 *
 *	An enumeration of different termination codes.
 */

public enum ErrCode {
	UNKNOWN(0), 
	FILEIO(1);
	
	private final int _code;
	
	/**
	 * Construct error code enumeration
	 * @param code
	 */
	ErrCode(int code){
		_code = code;
	}
	
	/**
	 * Return the error code
	 * @return
	 */
	public int code(){
		return _code;
	}
}
