package evoman.ec.gp.terminals;


import evoict.*;
import evoman.ec.gp.*;



/**
 * GPDoubleERC is a node that stores an ephemeral random constant within a
 * specified range. This value may be changed via mutation.
 * 
 * Configuration requires:
 * "min"
 * The minimum value of the constant.
 * 
 * "max"
 * The maximum value of the constant.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "ERC", return_type = Double.class, child_types = {})
public class GPDoubleERC extends GPMutableNode {

	private static final long	serialVersionUID	= 1L;
	Double						_value;



	public static void validate(GPNodeConfig conf) throws BadConfiguration {
		if (!conf.validate("max", Double.class) || !conf.validate("min", Double.class)) {
			throw new BadConfiguration("GPDoubleERC: min and/or max not set correctly.");
		}
	}



	public GPDoubleERC(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
		Double min = conf.D("min");
		Double max = conf.D("max");
		_value = new Double(min + (max - min) * _tree.getRandom().nextDouble());
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		return _value;
	}



	@Override
	public String toString() {
		Double min = getConfig().D("min");
		Double max = getConfig().D("max");
		return super.toString("ERC<min=" + min.toString() + ",max=" + max.toString() + ">");
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "ERC<" + eval(context) + ">");
	}



	@Override
	public void mutate() {
		Double min = getConfig().D("min");
		Double max = getConfig().D("max");
		_value = new Double(min + (max - min) * _tree.getRandom().nextDouble());
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPDoubleERC n = new GPDoubleERC(t, _conf, parent, (GPNodePos) _pos.clone());
		n._value = _value;
		doClone(t, n);
		return n;
	}
}
