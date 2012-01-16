package evoman.ec.gp.init;


import evoman.ec.gp.*;
import evoman.evo.structs.*;



@GPInitDescriptor(name = "FullTree")
public class GPFullTree extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;

	protected int				_max_depth;



	public static boolean validate(GPInitConfig conf) {
		if (conf.validate("depth", Integer.class) && conf.I("depth") > 0) {
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
	public boolean createTerminal(GPTree t, GPNode n, Class<?> cl) {
		int depth = (n == null) ? -1 : n.getPosition().getDepth();
		if (depth == _max_depth - 1) {
			return true;
		} else {
			return false;
		}
	}
}
