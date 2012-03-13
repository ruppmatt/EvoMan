package evoman.ec.gp;


import evoict.*;
import evoman.config.*;
import evoman.ec.*;
import evoman.evo.structs.*;



/**
 * A GPTreeConfig contains global configuration information about a GPTree such
 * as maximum allowed depth and return type.
 * 
 * Because there may be different types of GPTrees (e.g. FixedRoot, regular),
 * the class of the GPTree needs to be passed in as a constructor argument.
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
public class GPTreeConfig extends RepresentationConfig {

	@Override
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (validate("max_depth", Integer.class) || I("max_depth") < 1) {
			bc.append("GPTreeConfig: max_depth is not set or less than 1,");
		}
		if (validate("return_type", Class.class)) {
			bc.append("GPTreeConfig: return_type is not set.");
		}
		bc.validate();
	}

	private static final long				serialVersionUID	= 1L;
	protected GPNodeDirectory				_node_dir;
	protected EMState						_state;
	protected final Class<? extends GPTree>	_tree_type;



	@ConfigConstructor()
	public GPTreeConfig(Class<? extends GPTree> cl) {
		_tree_type = cl;
		;
	}



	@ConfigRequire()
	public void setNodeDirectory(GPNodeDirectory dir) {
		_node_dir = dir;
	}



	public Class<? extends GPTree> treeType() {
		return _tree_type;
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
