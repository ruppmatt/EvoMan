package evoman.ec.gp.terminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "GPNodeDoubleConst", return_type = Double.class, child_types = {})
public class GPNodeDoubleConst extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public static boolean validate(GPNodeConfig conf) {
		if (conf.validate("value", Double.class)) {
			return true;
		} else {
			return false;
		}
	}



	public GPNodeDoubleConst(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
		_value = conf.D("value");
	}



	@Override
	public Object eval(Object context) {
		return _value;
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval(_value.toString());
	}



	@Override
	public String toString() {
		return super.toString(_value.toString());
	}

}
