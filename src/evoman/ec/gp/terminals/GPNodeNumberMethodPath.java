package evoman.ec.gp.terminals;


import evoict.*;
import evoict.reflection.*;
import evoman.ec.gp.*;



/**
 * GPNumberMethodPath uses a string path into a method dictionary. The method
 * dictionary must contain methods that are able to be evaluated on the
 * evaluation's context object. If the method path cannot be mapped to
 * a valid method via eval's context object, then a BadNodeValue exception is
 * thrown.
 * 
 * GPNumberMethod configuration requires:
 * "dict"
 * A method dictionary that maps the context object's methods; all methods must
 * return some type of number.
 * 
 * "max_path"
 * A maximum path length to cap the depth of lookups and avoid infinite loops.
 * 
 * @author ruppmatt
 * 
 */
@GPNodeDescriptor(name = "MethodPathToNumber", return_type = Double.class, child_types = {})
public class GPNodeNumberMethodPath extends GPMutableNode {

	private static final long	serialVersionUID	= 1L;
	protected MethodDictionary	_dict;
	protected String			_path;
	protected Double			_value;
	protected Integer			_max_path;



	public void validate(GPNodeConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("dict", MethodDictionary.class)) {
			bc.append("GPNodeConfig: dict is not set correctly.");
		}
		if (!conf.validate("max_path", Integer.class) || conf.I("max_path") < 1) {
			bc.append("GPNodeConfig: max_path is either not set or less than zero.");
		}
		bc.validate();
	}



	public GPNodeNumberMethodPath(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
		_dict = (MethodDictionary) conf.get("dict");
		_max_path = conf.I("max_path");
		try {
			_path = _dict.getRandomPath(t.getRandom(), _max_path);
		} catch (BadConfiguration e) {
			_path = null;
		}
		init();
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {

		Number retrieved;
		try {
			retrieved = (Number) _dict.evaluate(_path, context);
		} catch (UnresolvableException e) {
			throw new BadNodeValue("Unable to resolve path + " + _path + " for entity of class "
					+ context.getClass().getName() + "  Resolution returns: " + e.getMessage(), this);
		}
		_value = retrieved.doubleValue();
		return _value;
	}



	@Override
	public Object last() {
		return _value;
	}



	@Override
	public String toString() {
		return super.toString(_path);
	}



	@Override
	public String lastEval() {
		return super.lastEval(_path + "<" + _value.toString() + ">");
	}



	@Override
	public void mutate() {
		try {
			_path = _dict.getRandomPath(_tree.getRandom(), _max_path);
		} catch (BadConfiguration e) {
			_path = null;
		}
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPNodeNumberMethodPath n = new GPNodeNumberMethodPath(t, _conf, parent, (GPNodePos) _pos.clone());
		n._dict = _dict;
		n._path = _path;
		n._value = _value;
		doClone(t, n);
		return n;
	}

}
