package evoman.ec.gp.nonterminals;


import evoman.config.*;
import evoman.ec.gp.*;



/**
 * GPMultiply performs multiplication on two double children.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "Multiply")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Multiply", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMultiply extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPMultiply(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double v = (Double) _children[0].eval(context) *
				(Double) _children[1].eval(context);
		return v;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "*<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("*");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPMultiply n = new GPMultiply(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
