package evoman.tools;

public enum ErrCode {
	UNKNOWN(0), 
	FILEIO(1);
	
	private final int _code;
	
	ErrCode(int code){
		_code = code;
	}
	
	public int code(){
		return _code;
	}
}
