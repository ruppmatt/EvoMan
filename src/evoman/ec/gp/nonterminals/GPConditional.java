package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



/**
 * GPConditional uses the boolean value provided by the first child to return
 * either the second (if true) or third (if false) child value.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "Conditional", return_type = Double.class, child_types = { Boolean.class, Double.class,
		Double.class })
public class GPConditional extends GPNode {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	protected Boolean			_cexpr;
	protected Double			_v1;
	protected Double			_v2;
	protected Double			_value;



	public GPConditional(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		_cexpr = (Boolean) _children[0].eval(context);
		_v1 = (Double) _children[1].eval(context);
		_v2 = (Double) _children[2].eval(context);
		_value = (_cexpr) ? _v1 : _v2;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("?:<" + _cexpr + "," + _v1 + "," + _v2 + "=" + _value + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("?:");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPConditional n = new GPConditional(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
