package evoman.ec.gp.terminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;



/**
 * GPNodeDoubleConst creates a constant with a particular immutable value.
 * 
 * Configuration requires:
 * "value"
 * The value of the constant.
 * 
 * @author ruppmatt
 * 
 */

@GPNodeDescriptor(name = "GPNodeDoubleConst", return_type = Double.class, child_types = {})
@ConfigRegister(name = "DoubleConstant")
@ConfigProxy(proxy_for = GPNodeConfig.class)
public class GPNodeDoubleConst extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public static void validate(GPNodeConfig conf) throws BadConfiguration {
		if (!conf.validate("value", Double.class)) {
			throw new BadConfiguration("GPNodeDoubleConst: value is not set correctly");
		}
	}



	public GPNodeDoubleConst(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		return getConfig().D("value");
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, eval(context).toString());
	}



	@Override
	public String toString() {
		return super.toString(getConfig().D("value").toString());
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPNodeDoubleConst n = new GPNodeDoubleConst(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
