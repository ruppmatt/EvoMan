package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPMin returns the minimum of two double-valued children.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "Min")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Min", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMin extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPMin(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double _v1 = (Double) _children[0].eval(context);
		Double _v2 = (Double) _children[1].eval(context);
		return (_v1 < _v2) ? _v1 : _v2;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "MIN<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("MIN");
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPMin n = new GPMin(_conf, parent, (GPNodePos) _pos.clone());
		doClone(n);
		return n;
	}
}
