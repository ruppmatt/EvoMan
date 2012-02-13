package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



/**
 * GPGreaterThan returns true if the first double-valued child is greater than
 * the secon double-valued child.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "GreaterThan", return_type = Boolean.class, child_types = { Double.class, Double.class })
public class GPGreaterThan extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Boolean			_value;



	public GPGreaterThan(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double lhs = (Double) _children[0].eval(context);
		Double rhs = (Double) _children[1].eval(context);
		_value = (lhs > rhs) ? true : false;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("GREATER<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("GREATER");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPGreaterThan n = new GPGreaterThan(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
