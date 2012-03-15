package evoman.ec.gp.init;


import evoict.*;
import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPFullTree constructs a new (sub)tree with all leaves at the a depth
 * specified by max_depth. In the event max_depth exceeds the maximum depth
 * allowed by a GP Tree's configuration, the latter is used.
 * 
 * 
 * Required configuration:
 * "max_depth"
 * The depth of the (sub)tree.
 * 
 * @author ruppmatt
 * 
 */

@GPInitDescriptor(name = "FullTree")
public class GPFullTree extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;
	protected KeyValueStore		_config;



	@Override
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (_kv.validate("max_depth", Integer.class)) {
			if (_kv.I("max_depth") < 1) {
				bc.append("Tree depth cannot be less than 1.");
			}
		} else {
			bc.append("GPFullTree: max_depth not set or incorrectly set.");
		}
		bc.validate();
	}



	public GPFullTree() {
		super();
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl, EMState state) {
		int local_max_depth = I("max_depth");
		int node_depth = (parent == null) ? 1 : parent.getDepth() + 1;
		int max = (local_max_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : local_max_depth;
		if (node_depth == max) {
			return true;
		} else if (node_depth < max) {
			return false;
		} else {
			// System.err.println("Exceeding maximum, value = " + node_depth);
			return true;
		}
	}

}
