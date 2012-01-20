package evoman.ec.gp;


import evoict.*;
import evoman.ec.gp.init.*;
import evoman.evo.structs.*;



public class GPTreeConfig extends KeyValueStore {

	private static final long	serialVersionUID	= 1L;
	protected GPNodeDirectory	_node_dir;
	protected GPTreeInitializer	_init				= null;
	protected KeyValueStore		_kv					= new KeyValueStore();
	protected EMState			_state;



	public GPTreeConfig(GPNodeDirectory dir, GPTreeInitializer init) {
		_node_dir = dir;
		_init = init;
	}



	public GPNodeDirectory getNodeDirectory() {
		return _node_dir;
	}



	public GPTreeInitializer getInitializer() {
		return _init;
	}



	public Class<?> getReturnType() {
		return (Class<?>) _kv.get("return_type");
	}



	public int getMaxDepth() {
		return I("max_depth");
	}

}
