package evoman.ec.gp.nonterminals;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



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



	public GPGreaterThan(GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(conf, parent, pos);
	}



	@Override
	public void init(EMState state) throws BadConfiguration {
		return;
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		Double lhs = (Double) _children[0].eval(context);
		Double rhs = (Double) _children[1].eval(context);
		_value = (lhs > rhs) ? true : false;
		return _value;
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, "GREATER<" + eval(context).toString() + ">");
	}



	@Override
	public String toString() {
		return super.toString("GREATER");
	}



	@Override
	public GPNode clone(GPNode parent) {
		GPGreaterThan n = new GPGreaterThan(_conf, parent, (GPNodePos) _pos.clone());
		doClone(n);
		return n;
	}

}
