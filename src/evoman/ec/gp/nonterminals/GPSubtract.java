package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPSubtract performs subtraction on two double children.
 * 
 * @author ruppmatt
 * 
 */

@ConfigRegister(name = "Subtract")
@ConfigProxy(proxy_for = GPNodeConfig.class)
@GPNodeDescriptor(name = "Subtract", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPSubtract extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPSubtract(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double v = null;
		try {
			v = (Double) _children[0].eval(context) -
					(Double) _children[1].eval(context);
		} catch (Exception e) {
			throw new BadNodeValue(e.getMessage(), this);
		}
		return v;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "-<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("-");
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPSubtract n = new GPSubtract(_conf, parent, (GPNodePos) _pos.clone());
		doClone(n);
		return n;
	}

}
