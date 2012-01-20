package evoman.ec.gp.init;


import evoman.ec.gp.*;
import evoman.evo.structs.*;



@GPInitDescriptor(name = "FullTree")
public class GPFullTree extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;

	protected int				_max_depth;



	public static boolean validate(GPInitConfig conf, GPTree t, String msg) {
		if (conf.validate("depth", Integer.class)) {
			if (conf.I("depth") < 0) {
				msg = "Tree depth cannot be negative.";
				return false;
			} else if (conf.I("depth") > t.getConfig().I("max_depth")) {
				msg = "Tree initializer maximum depth greater than allowed.";
				return false;
			}
			return true;
		} else {
			return false;
		}
	}



	public GPFullTree(EMState state, GPInitConfig conf) {
		super(state, conf);
		_max_depth = _conf.I("depth");
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl) {
		int depth = (parent == null) ? -1 : parent.getDepth();
		if (depth == _max_depth - 1) {
			return true;
		} else {
			return false;
		}
	}
}
