package evoman.ec.gp.init;


import evoman.ec.gp.*;
import evoman.evo.structs.*;



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

	protected final int	max_depth;
	protected final int	min_depth;



	public GPVarDepth(EMState state, GPInitConfig conf) {
		super(state, conf);
		max_depth = conf.I("max_depth");
		min_depth = conf.I("min_depth");
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl) {
		int cur_depth = (parent != null) ? parent.getDepth() + 1 : 1;
		if (cur_depth == max_depth) {
			return true;
		} else if (cur_depth < min_depth) {
			return false;
		} else {
			int delta = max_depth - cur_depth;
			return (_state.getRandom().nextInt(delta) == 0);
		}
	}
}
