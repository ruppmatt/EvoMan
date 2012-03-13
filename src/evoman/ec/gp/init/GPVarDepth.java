package evoman.ec.gp.init;


import evoict.*;
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

@GPInitDescriptor(name = "VariableDepthTree")
public class GPVarDepth extends GPTreeInitializer {

	private static final long	serialVersionUID	= 1L;



	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!_kv.validate("max_depth", Integer.class) || !_kv.validate("min_depth", Integer.class)) {
			bc.append("GPVarDepth: max_depth and/or min_depth not specified.");
		}
		if (_kv.I("max_depth") < 1 || _kv.I("min_depth") > _kv.I("max_depth")) {
			bc.append("max_depth and/or min_depth incorrectly specified.");
		}
		bc.validate();
	}



	public GPVarDepth() {
		super();
	}



	@Override
	public boolean createTerminal(GPTree t, GPNode parent, Class<?> cl, EMState state) {
		int cur_depth = (parent != null) ? parent.getDepth() + 1 : 1;
		int max_depth = I("max_depth");
		int min_depth = I("min_depth");
		int max = (max_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : max_depth;
		int min = (min_depth > t.getConfig().getMaxDepth()) ? t.getConfig().getMaxDepth() : min_depth;
		if (cur_depth == max) {
			return true;
		} else if (cur_depth < min) {
			return false;
		} else {
			int delta = max - cur_depth;
			return (state.getRandom().nextInt(delta) == 0);
		}
	}
}
