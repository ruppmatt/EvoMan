package evoman.tools;

public class Identifiable {
	protected final String _name;
	
	public Identifiable(String name){
		_name = name;
	}
	
	public String getName(){
		return _name;
	}
}
