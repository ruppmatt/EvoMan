package evoman.ec.gp.terminals;


import evoman.*;
import evoman.ec.gp.*;
import evoman.evo.*;
import evoman.evo.structs.*;



@GPNodeDescriptor(name = "MethodPathToNumber", return_type = Double.class, child_types = {})
public class GPNodeNumberMethodPath extends GPMutableNode {

	private static final long	serialVersionUID	= 1L;
	protected MethodDictionary	_dict;
	protected String			_path;
	protected Double			_value;



	public void validate(GPNodeConfig conf) throws BadConfiguration {
		if (conf.validate("dict", MethodDictionary.class)) {
			throw new BadConfiguration("GPNodeConfig: dict is not set correctly.");
		}
	}



	public GPNodeNumberMethodPath(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
		_path = _dict.getRandomPath();
		_dict = (MethodDictionary) conf.get("dict");
		init();
	}



	@Override
	public Object eval(Object entity) {
		Number retrieved = (Number) _dict.evaluate(_path, entity);
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
		_path = _dict.getRandomPath();
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
