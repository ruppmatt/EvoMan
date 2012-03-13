package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPMax returns the maximum of two double-valued children.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "Max", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMax extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPMax(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double _v1 = (Double) _children[0].eval(context);
		Double _v2 = (Double) _children[1].eval(context);
		return (_v1 > _v2) ? _v1 : _v2;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "MAX<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("MAX");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPMax n = new GPMax(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
