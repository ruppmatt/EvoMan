package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPless returns true if the first double-valued child is less than the second
 * double-valued child.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "LessThan", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPLessThan extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPLessThan(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double lhs = (Double) _children[0].eval(context);
		Double rhs = (Double) _children[1].eval(context);
		return (lhs < rhs) ? lhs : rhs;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "LESS<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("LESS");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPLessThan n = new GPLessThan(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}