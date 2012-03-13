package evoman.ec.gp.nonterminals;


import evoman.config.*;
import evoman.ec.gp.*;



/**
 * GPConditional uses the boolean value provided by the first child to return
 * either the second (if true) or third (if false) child value.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "Conditional")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Conditional", return_type = Double.class, child_types = { Boolean.class, Double.class,
		Double.class })
public class GPConditional extends GPNode {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;



	public GPConditional(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		boolean cexpr = (Boolean) _children[0].eval(context);
		if (cexpr == true)
			return _children[1].eval(context);
		else
			return _children[2].eval(context);
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "?:<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("?:");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPConditional n = new GPConditional(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
