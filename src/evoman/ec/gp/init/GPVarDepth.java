package evoman.ec.gp.init;


import evoman.ec.gp.*;
import evoman.evo.structs.*;



/**
 * GPVarDepth creates a tree with leaves in a range of depths. In the event that
 * max_depth exceeds the maximum depth allowed for a particular GPTree, the
 * latter is used.
 * 
 * Configuraiton required:
 * "max_depth"
 * The maximum depth of a leaf
 * 
 * "min_depth"
 * The minimum depth of the leaf.
 * 
 * @author ruppmatt
 * 
 */
public class GPVarDepth extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;



	public static boolean validate(GPInitConfig conf, String msg) {
		if (!conf.validate("max_depth", Integer.class) || !conf.validate("min_depth", Integer.class)) {
			msg = "max_depth and/or min_depth not specified.";
			return false;
		}
		if (conf.I("max_depth") < 1 || conf.I("min_depth") > conf.I("max_depth")) {
			msg = "max_depth and/or min_depth incorrectly specified.";
			return false;
		}
		return true;
	}

	protected final int	_max_depth;
	protected final int	_min_depth;



	public GPVarDepth(EMState state, GPInitConfig conf) {
		super(state, conf);
		_max_depth = conf.I("max_depth");
		_min_depth = conf.I("min_depth");
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl) {
		int cur_depth = (parent != null) ? parent.getDepth() + 1 : 1;
		int max = (_max_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : _max_depth;
		int min = (_min_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : _min_depth;
		if (cur_depth == max) {
			return true;
		} else if (cur_depth < min) {
			return false;
		} else {
			int delta = max - cur_depth;
			return (_state.getRandom().nextInt(delta) == 0);
		}
	}
}
