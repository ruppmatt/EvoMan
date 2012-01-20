package evoman.ec.gp.nonterminals;


import evoman.ec.gp.*;



@GPNodeDescriptor(name = "Add", return_type = Double.class, child_types = { Double.class, Double.class })
public class GPAdd extends GPNode {

	private static final long	serialVersionUID	= 1L;
	Double						_last;



	public GPAdd(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
		_last = Double.NaN;
	}



	@Override
	public Object eval(Object context) {
		Double retval = 0.0;
		for (GPNode c : _children) {
			retval += (Double) c.eval(context);
		}
		_last = retval;
		return retval;
	}



	@Override
	public Object last() {
		return _last;
	}



	@Override
	public String toString() {
		return super.toString("+");
	}



	@Override
	public String lastEval() {
		return super.lastEval("+<" + _last.toString() + ">");
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPAdd n = new GPAdd(t, _conf, parent, (GPNodePos) _pos.clone());
		doClone(t, n);
		return n;
	}

}
