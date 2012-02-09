package evoman.ec.gp;


import evoict.*;



public class BadNodeValue extends BadConfiguration {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	final GPNode				_n;



	public BadNodeValue(String s, GPNode n) {
		super(s);
		_n = n;
	}



	public GPNode getNode() {
		return _n;
	}

}
