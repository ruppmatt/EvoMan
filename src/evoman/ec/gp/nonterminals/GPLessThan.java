package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "LessThan", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPLessThan extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPLessThan(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) {
		Double lhs = (Double) _children.get(0).eval(context);
		Double rhs = (Double) _children.get(1).eval(context);
		_value = (lhs < rhs) ? lhs : rhs;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("LESS<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("LESS");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPLessThan n = new GPLessThan(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}