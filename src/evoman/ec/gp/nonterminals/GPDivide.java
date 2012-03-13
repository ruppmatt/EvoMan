package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPDivide divides the first double-valued child by the second. In the event of
 * a divide-by-zero, a NaN is returned.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "Divide", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPDivide extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPDivide(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double v = (Double) _children[0].eval(context);
		Double div = (Double) _children[1].eval(context);
		if (div == 0.0) {
			return Double.NaN;
		} else {
			return v / div;
		}
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "/<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("/");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPDivide n = new GPDivide(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
