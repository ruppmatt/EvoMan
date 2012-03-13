package evoman.ec.gp.terminals;


import evoman.config.*;
import evoman.ec.gp.*;



/**
 * GPBooleanERC creates a boolean-valued random constant. This constant is
 * mutable.
 * 
 * No configuration is required.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "GPBooleanERC", return_type = Boolean.class, child_types = {})
@ConfigRegister(name = "BooleanERC")
@ConfigProxy(proxy_for = GPNodeConfig.class)
public class GPBooleanERC extends GPMutableNode {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	Boolean						_value;



	public GPBooleanERC(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
		_value = (_tree.getRandom().nextBoolean());
	}



	@Override
	public void mutate() {
		_value = (_tree.getRandom().nextBoolean());
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPBooleanERC n = new GPBooleanERC(t, _conf, parent, (GPNodePos) _pos.clone());
		n._value = _value;
		doClone(t, n);
		return n;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		return _value;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "BooleanERC<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("BooleanERC");
	}

}
