package evoman.ec.gp;


public class BadConstraint extends Exception {

	private static final long	serialVersionUID	= 1L;
	protected String			_name;



	public BadConstraint(String name) {
		_name = name;
	}



	public String getError() {
		return "Invalid constraint " + _name;
	}
}
