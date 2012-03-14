package evoman.ec.gp;


import evoict.*;
import evoman.config.*;
import evoman.ec.gp.init.*;
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

@ConfigRegister(name = "GPVariationManager")
public class GPVariationManager extends VariationManager {

	private static final long	serialVersionUID	= 1L;



	/**
	 * Validate the GPVariationManager's configuration
	 */
	@Override
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (!getConfig().validate("pop_size", Integer.class) || getConfig().I("pop_size") < 1) {
			bc.append("pop_size is either inset or less than 1.");
		}
		if (_tree_config == null) {
			bc.append("Missing tree configuration.");
		}
		if (_tree_init == null) {
			bc.append("Missing tree initializer.");
		}
		bc.validate();
	}

	protected GPTreeConfig		_tree_config	= null;
	protected GPTreeInitializer	_tree_init		= null;



	/**
	 * Construct a new GPVariation manager attached to EvoPool parent
	 * 
	 * @param parent
	 */
	@ConfigConstructor(args = { ConfigArgs.PARENT })
	public GPVariationManager(EvoPool parent) {
		super(parent);
	}



	/**
	 * Set the tree configruation.
	 * 
	 * @param config
	 */
	@ConfigRequire()
	public void setTreeConfig(GPTreeConfig config) {
		_tree_config = config;
	}



	/**
	 * Set the tree initializer.
	 * 
	 * @param init
	 */
	@ConfigRequire()
	public void setTreeInitializer(GPTreeInitializer init) {
		_tree_init = init;
	}



	/**
	 * Accessor to get the population size;
	 */
	@Override
	public int getPopSize() {
		return getConfig().I("pop_size");
	}



	@Override
	/**
	 * Initialize the variation manager.  
	 * Begin by validating the configuration of the variation manager and the configuration of the GPTree(s).  
	 * If successfully validated, initialize (and validate) the evopipeline.
	 * 
	 * If everything checks out, construct a population of GPTrees.
	 */
	public void init() {
		super.init();
		if (_tree_config == null) {
			getNotifier().fatal("GPVariation manager for EvoPool " + _ep.getName() + ": no tree configration set.");
		}
		if (_tree_init == null) {
			getNotifier().fatal("GPVariation manager for EvoPool " + _ep.getName() + ": no tree initializer set.");
		}

		try {
			validate();
			GPTree.validate(_tree_config);
		} catch (BadConfiguration bc) {
			getNotifier().fatal("GPVariation manager for EvoPool " + _ep.getName() + ": " + bc.getMessage());
		}

		try {
			createPopulation();
		} catch (BadConfiguration bc) {
			getNotifier().fatal(
					"GPVariation manager for EvoPool " + _ep.getName() + ": unable to construct population.\n"
							+ bc.getMessage());
		}
	}



	public void createPopulation() throws BadConfiguration {
		Population p = new Population();
		int size = getPopSize();
		for (int k = 0; k < size; k++) {
			GPTree t = new GPTree(this, _tree_config, _tree_init);
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
