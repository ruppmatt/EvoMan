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
	protected String			_path;



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
		MethodDictionary dict = (MethodDictionary) getConfig().get("dict");
		Integer max_path = conf.I("max_path");
		try {
			_path = dict.getRandomPath(t.getRandom(), max_path);
		} catch (BadConfiguration e) {
			_path = null;
		}
		init();
	}



	@Override
	public Object eval(Object context) throws BadNodeValue {
		MethodDictionary dict = (MethodDictionary) getConfig().get("dict");
		Number retrieved;
		try {
			retrieved = (Number) dict.evaluate(_path, context);
		} catch (UnresolvableException e) {
			throw new BadNodeValue("Unable to resolve path + " + _path + " for entity of class "
					+ context.getClass().getName() + "  Resolution returns: " + e.getMessage(), this);
		}
		return retrieved;
	}



	@Override
	public String toString() {
		return super.toString(_path);
	}



	@Override
	public String toString(Object context) throws BadNodeValue {
		return super.toString(context, _path + "<" + eval(context).toString() + ">");
	}



	@Override
	public void mutate() {
		try {
			MethodDictionary dict = (MethodDictionary) getConfig().get("dict");
			int max_path = getConfig().I("max_path");
			_path = dict.getRandomPath(_tree.getRandom(), max_path);
		} catch (BadConfiguration e) {
			_path = null;
		}
	}



	@Override
	public GPNode clone(GPTree t, GPNode parent) {
		GPNodeNumberMethodPath n = new GPNodeNumberMethodPath(t, _conf, parent, (GPNodePos) _pos.clone());
		n._path = _path;
		doClone(t, n);
		return n;
	}

}
