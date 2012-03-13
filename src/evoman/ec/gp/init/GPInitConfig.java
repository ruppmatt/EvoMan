package evoman.ec.gp.init;


import evoict.*;



/**
 * GPInitConfig contains information about tree initializers.
 * 
 * @author ruppmatt
 * 
 */
public class GPInitConfig extends KeyValueStore {

	private static final long						serialVersionUID	= 1L;

	protected Class<? extends GPTreeInitializer>	_cl;



	public GPInitConfig(Class<? extends GPTreeInitializer> cl) {
		_cl = cl;
	}



	public Class<? extends GPTreeInitializer> getInitClass() {
		return _cl;
	}

}
