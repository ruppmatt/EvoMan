package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "Min", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMin extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPMin(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
	}



	@Override
	public Object eval(Object context) {
		Double _v1 = (Double) _children.get(0).eval(context);
		Double _v2 = (Double) _children.get(1).eval(context);
		_value = (_v1 < _v2) ? _v1 : _v2;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("MIN<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("MIN");
	}

}
