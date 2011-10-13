package evoman.tools;

public class ConfigurableDANode extends DANode implements Configurable{

	protected KeyValueStore _kv = new KeyValueStore();
	
	public ConfigurableDANode(String name, DAGraph graph) {
		super(name, graph);
	}


	@Override
	public void addDefault(String name, Object value, String descr) {
		_kv.addDefault(name, value, descr);
	}


	@Override
	public Object get(String name) {
		return _kv.get(name);
	}


	@Override
	public void set(String name, Object value) {
		_kv.set(name,  value);
	}


	@Override
	public void unset(String name) {
		_kv.unset(name);
	}


	@Override
	public PType getType(String name) {
		return _kv.getType(name);
	}


	@Override
	public Boolean isSet(String name) {
		return _kv.isSet(name);
	}


	@Override
	public Integer I(String name) {
		return _kv.I(name);
	}


	@Override
	public Double D(String name) {
		return _kv.D(name);
	}


	@Override
	public String S(String name) {
		return _kv.S(name);
	}


	@Override
	public Boolean isDefault(String name) {
		return _kv.isDefault(name);
	}

}
