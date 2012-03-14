package evoman.ec.gp.terminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



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
@ConfigRegister(name = "DoubleERC")
@ConfigProxy(proxy_for = GPNodeConfig.class)
public class GPDoubleERC extends GPMutableNode {

	private static final long	serialVersionUID	= 1L;
	Double						_value;



	public static void validate(GPNodeConfig conf) throws BadConfiguration {
		if (!conf.validate("max", Double.class) || !conf.validate("min", Double.class)) {
			throw new BadConfiguration("GPDoubleERC: min and/or max not set correctly.");
		}
	}



	public GPDoubleERC(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		Double min = getConfig().D("min");
		Double max = getConfig().D("max");
		_value = new Double(min + (max - min) * state.getRandom().nextDouble());
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
	public void mutate(EMState state) {
		Double min = getConfig().D("min");
		Double max = getConfig().D("max");
		_value = new Double(min + (max - min) * state.getRandom().nextDouble());
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPDoubleERC n = new GPDoubleERC(_conf, parent, (GPNodePos) _pos.clone());
		n._value = _value;
		doClone(n);
		return n;
	}
}
