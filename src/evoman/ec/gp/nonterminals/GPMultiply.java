package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



/**
 * GPMultiply performs multiplication on two double children.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "Multiply", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMultiply extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPMultiply(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double v = (Double) _children[0].eval(context) *
				(Double) _children[1].eval(context);
		_value = v;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("*<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("*");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPMultiply n = new GPMultiply(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
