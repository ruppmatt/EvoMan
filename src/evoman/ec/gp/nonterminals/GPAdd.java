package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



/**
 * GPAdd adds the first double-valued child to the second double-valued child.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "Add", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPAdd extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPAdd(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
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
	public GPNode clone(GPTree t, GPNode parent) {
		GPAdd n = new GPAdd(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
