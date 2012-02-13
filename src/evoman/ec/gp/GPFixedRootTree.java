package evoman.ec.gp;


import evoict.*;
import evoman.evo.structs.*;



/**
 * A GPFixedRootTree has an immutable root node. This node cannot be altered by
 * direct mutation or substitution.
 * 
 * @author ruppmatt
 * 
 */
public class GPFixedRootTree extends GPTree {

	private static final long	serialVersionUID	= 1L;



	public static void validate(GPTreeConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();

		if (!conf.validate("root_config", GPNodeConfig.class)) {
			bc.append("GPFixedFootTree: a valid root_config not found.");
		} else {
			GPNodeConfig root_conf = (GPNodeConfig) conf.get("root_config");
			try {
				GPNodeDirectory.validateNodeConfig(root_conf);
			} catch (BadConfiguration node_bc) {
				bc.append(node_bc.getMessage());
				bc.append("GPFixedRootTree: root node configuration is invalid.");
			}
			conf.set("return_type", root_conf.getConstraints().getReturnType());
		}

		try {
			GPTree.validate(conf);
		} catch (BadConfiguration parbc) {
			bc.append(parbc.getMessage());
		}
	}

	protected GPNodeConfig	_root_conf;



	public GPFixedRootTree(EMState state, GPTreeConfig conf, GPNodeConfig root_conf) {
		super(state, conf);
		_root_conf = root_conf;
	}



	@Override
	public void init() {
		_root = buildNode(null, _root_conf, new GPNodePos());
		_root.init();
	}



	@Override
	public boolean reRoot(GPNode newroot) {
		return false;
	}



	@Override
	public boolean canAlter(GPNode n) {
		return (n == _root) ? false : true;
	}
}
