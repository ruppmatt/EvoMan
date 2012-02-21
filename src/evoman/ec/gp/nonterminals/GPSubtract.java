package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



/**
 * GPSubtract performs subtraction on two double children.
 * 
 * @author ruppmatt
 * 
 */

@GPNodeDescriptor(name = "Subtract", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPSubtract extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPSubtract(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
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
	public GPNode clone(GPTree t, GPNode parent) {
		GPSubtract n = new GPSubtract(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
