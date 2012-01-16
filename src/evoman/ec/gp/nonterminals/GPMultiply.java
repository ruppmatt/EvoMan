package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "Multiply", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMultiply extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPMultiply(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
	}



	@Override
	public Object eval(Object context) {
		Double v = (Double) _children.get(0).eval(context) *
				(Double) _children.get(1).eval(context);
		_value = v;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("*<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("*");
	}

}
