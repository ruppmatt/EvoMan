package evoman.ec.gp;


import evoman.*;
import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



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
	public void evolve() {
		if (_evopipeline == null) {
			return;
		} else {
			_ep.setPopulation(_evopipeline.process(getPoolPopulation()));
		}
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
