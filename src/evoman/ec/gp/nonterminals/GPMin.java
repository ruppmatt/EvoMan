package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "Min", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPMin extends GPNode {

	private static final long	serialVersionUID	= 1L;
	protected Double			_value;



	public GPMin(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double _v1 = (Double) _children.get(0).eval(context);
		Double _v2 = (Double) _children.get(1).eval(context);
		_value = (_v1 < _v2) ? _v1 : _v2;
		return _value;
	}



	@Override
	public String lastEval() {
		return super.lastEval("MIN<" + _value.toString() + ">");
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString("MIN");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPMin n = new GPMin(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}
}
