package evoman.ec.gp;


import evoict.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



/**
 * A GPVariationManager controls the composition of a population of GPTrees. It
 * contains the EvolutionPipeline to manipulate GPTrees as well as the
 * initializer information for the population at the start of the experiment.
 * 
 * @author ruppmatt
 * 
 */

public class GPVariationManager extends VariationManager {

	private static final long	serialVersionUID	= 1L;



	public static void validate(VariationManagerConfig conf) throws BadConfiguration {
		if (!conf.validate("pop_size", Integer.class) || conf.I("pop_size") < 1) {
			throw new BadConfiguration("pop_size is either inset or less than 1.");
		}
	}

	protected GPTreeConfig	_tree_config	= null;



	public GPVariationManager(EvoPool parent, VariationManagerConfig conf) {
		super(parent, conf);
	}



	public void setTreeConfig(GPTreeConfig config) {
		_tree_config = config;
	}



	@Override
	public int getPopSize() {
		return _config.I("pop_size");
	}



	@Override
	public void init() {
		try {
			GPVariationManager.validate(_config);
			if (_tree_config == null)
				throw new BadConfiguration("Missing tree configuration.");
			GPTree.validate(_tree_config);
		} catch (BadConfiguration bc) {
			getNotifier().fatal("GPVariation manager for EvoPool " + _ep.getName() + ": " + bc.getMessage());
		}
		if (_evopipeline != null) {
			_evopipeline.init();
		}
		Population p = new Population(_ep);
		int size = getPopSize();
		for (int k = 0; k < size; k++) {
			GPTree t = new GPTree(_ep, _tree_config);
			t.init();
			Genotype g = makeGenotype(t);
			p.addGenotype(g);
		}
		_ep.setPopulation(p);
	}



	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
