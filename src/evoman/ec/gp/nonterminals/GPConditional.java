package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



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



	public GPConditional(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
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
	public GPNode clone(GPNode parent) {
		GPConditional n = new GPConditional(_conf, parent, (GPNodePos) _pos.clone());
		doClone(n);
		return n;
	}
}
