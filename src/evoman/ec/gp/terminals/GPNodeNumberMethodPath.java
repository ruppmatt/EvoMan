package evoman.ec.gp.terminals;


import evoman.ec.gp.*;
import evoman.evo.structs.*;



@GPNodeDescriptor(name = "MethodPathToNumber", return_type = Double.class, child_types = {})
public class GPNodeNumberMethodPath extends GPMutableNode {

	private static final long	serialVersionUID	= 1L;
	protected MethodDictionary	_dict;
	protected String			_path;
	protected Double			_value;



	public boolean validate(GPNodeConfig conf) {
		if (conf.isSet("dict") && conf.get("dict") instanceof MethodDictionary) {
			return true;
		} else {
			return false;
		}
	}



	public GPNodeNumberMethodPath(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
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

}
