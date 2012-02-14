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
 * "depth"
 * The depth of the (sub)tree.
 * 
 * @author ruppmatt
 * 
 */
@GPInitDescriptor(name = "FullTree")
public class GPFullTree extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;

	protected int				_max_depth;



	public static void validate(GPInitConfig conf) throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (conf.validate("depth", Integer.class)) {
			if (conf.I("depth") < 1) {
				bc.append("Tree depth cannot be less than 1.");
			}
		}
		bc.validate();
	}



	public GPFullTree(EMState state, GPInitConfig conf) {
		super(state, conf);
		_max_depth = _conf.I("depth");
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl) {
		int node_depth = (parent == null) ? 1 : parent.getDepth() + 1;
		int max = (_max_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : _max_depth;
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
