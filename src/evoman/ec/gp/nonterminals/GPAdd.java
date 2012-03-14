package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPAdd adds the first double-valued child to the second double-valued child.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "Add")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Add", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPAdd extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPAdd(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double retval = 0.0;
		for (GPNode c : _children) {
			retval += (Double) c.eval(context);
		}
		return retval;
	}



	@Override
	public String toString() {
		return super.toString("+");
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "+<" + eval(context).toString() + ">");
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPAdd n = new GPAdd(_conf, parent, (GPNodePos) _pos.clone());
		doClone(n);
		return n;
	}

}
