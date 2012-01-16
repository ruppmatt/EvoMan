package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "Divide", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPDivide extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPDivide(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
	}



	@Override
	public Object eval(Object context) {
		Double v = (Double) _children.get(0).eval(context);
		Double div = (Double) _children.get(1).eval(context);
		if (div == 0.0) {
			_value = Double.NaN;
		} else {
			_value = v / div;
		}
		return _value;
	}



	@Override
	public String lastEval() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object last() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
