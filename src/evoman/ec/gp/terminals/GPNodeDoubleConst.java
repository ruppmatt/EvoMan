package evoman.ec.gp.terminals;


import evoman.ec.gp.*;
import evoman.evo.*;



@GPNodeDescriptor(name = "GPNodeDoubleConst", return_type = Double.class, child_types = {})
public class GPNodeDoubleConst extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public static void validate(GPNodeConfig conf) throws BadConfiguration {
		if (!conf.validate("value", Double.class)) {
			throw new BadConfiguration("GPNodeDoubleConst: value is not set correctly");
		}
	}



	public GPNodeDoubleConst(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
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



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPNodeDoubleConst n = new GPNodeDoubleConst(t, _conf, parent, (GPNodePos) _pos.clone());
		n._value = _value;
		doClone(t, n);
		return n;
	}

}
