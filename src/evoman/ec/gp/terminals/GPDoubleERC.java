package evoman.ec.gp.terminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "ERC", return_type = Double.class, child_types = {})
public class GPDoubleERC extends GPNode {

	private static final long	serialVersionUID	= 1L;
	Double						_min;
	Double						_max;
	Double						_value;



	public static boolean validate(GPNodeConfig conf) {
		if (conf.validate("max", Double.class) && conf.validate("min", Double.class)) {
			return true;
		} else {
			return false;
		}
	}



	public GPDoubleERC(GPTree t, GPNodeConfig conf, int depth) {
		super(t, conf, depth);
		_min = conf.D("min");
		_max = conf.D("max");
		_value = new Double(_min + (_max - _min) * _tree.getRandom().nextDouble());
	}



	@Override
	public Object eval(Object context) {
		_value = new Double(_min + (_max - _min) * _tree.getRandom().nextDouble());
		_evaluated = true;
		return _value;
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("ERC<min=" + _min.toString() + ",max=" + _max.toString() + ">");
	}



	@Override
	public String lastEval() {
		return super.lastEval("ERC<" + _value.toString() + ">");
	}



	@Override
	public void mutate() {
		_value = new Double(_min + (_max - _min) * _tree.getRandom().nextDouble());
	}

}
