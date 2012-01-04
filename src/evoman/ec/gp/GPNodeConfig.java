package evoman.ec.gp;


import evoict.*;



public class GPNodeConfig extends KeyValueStore {

	private static final long			serialVersionUID	= 1L;
	protected GPNodeConstraints			_constraints;
	protected Class<? extends GPNode>	_class;



	public GPNodeConfig(Class<? extends GPNode> cl) {
		super();
		_class = cl;
		_constraints = GPNodeConstraints.scan(cl);
	}



	public Class<? extends GPNode> getNodeClass() {
		return _class;
	}



	public GPNodeConstraints getConstraints() {
		return _constraints;
	}
}
