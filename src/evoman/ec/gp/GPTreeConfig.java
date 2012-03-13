package evoman.ec.gp;


import evoict.*;
import evoman.config.*;
import evoman.evo.structs.*;



/**
 * A GPTreeConfig contains global configuration information about a GPTree such
 * as maximum allowed depth and return type.
 * 
 * Settings
 * 
 * max_depth
 * maximum depth allowed by the tree. All operators must obey this limit.
 * 
 * return_type
 * the return type (class) of the tree.
 * 
 * @author ruppmatt
 * 
 */
@ConfigRegister(name = "GPTree")
public class GPTreeConfig extends KeyValueStore {

	public static void validate(GPTreeConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!conf.validate("max_depth", Integer.class) || conf.I("max_depth") < 1) {
			bc.append("GPTreeConfig: max_depth is not set or less than 1,");
		}
		if (!conf.validate("return_type", Class.class)) {
			bc.append("GPTreeConfig: return_type is not set.");
		}
		bc.validate();
	}

	private static final long	serialVersionUID	= 1L;
	protected GPNodeDirectory	_node_dir;
	protected EMState			_state;



	@ConfigConstructor()
	public GPTreeConfig(GPNodeDirectory dir) {
		_node_dir = dir;
	}



	public GPNodeDirectory getNodeDirectory() {
		return _node_dir;
	}



	public Class<?> getReturnType() {
		return (Class<?>) get("return_type");
	}



	public int getMaxDepth() {
		return I("max_depth");
	}

}
