package evoman.ec.composite;


import evoict.*;
import evoict.graphs.*;
import evoman.config.*;
import evoman.ec.composite.init.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



/**
 * A Composite VariationManager controls the construction and variation of a
 * population of composite representations. It contains the EvolutionPipeline to
 * manipulate composite representations as well as the initializer to create an
 * initial population of composite genotypes.
 * 
 * It is an EMState object, meaning that it will be initialized after
 * construction.
 * 
 * @author ruppmatt
 * 
 */

@ConfigRegister(name = "CompositeVariationManager")
public class CompositeVariationManager extends VariationManager {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	protected CompositeConfig		_comp_config		= null;
	protected CompositeInitializer	_comp_init			= null;



	@ConfigConstructor(args = { ConfigArgs.PARENT })
	public CompositeVariationManager(EvoPool parent) {
		super(parent);
	}



	/**
	 * Set the composite configuration
	 * 
	 * @param config
	 *            Configuration settings to build a composite representation
	 */
	@ConfigRequire()
	public void setCompositeConfig(CompositeConfig config) {
		_comp_config = config;
	}



	/**
	 * Set the composite initializer.
	 * 
	 * @param init
	 *            initializer to construct a single composite representation
	 */
	@ConfigRequire()
	public void setCompositeInit(CompositeInitializer init) {
		_comp_init = init;
	}



	/**
	 * Validate and initialize the composite population.
	 */
	@Override
	public void init() {
		try {
			validate();
		} catch (BadConfiguration bc) {
			getNotifier().fatal("Composite manager for EvoPool " + _ep.getName() + ":" + bc.getMessage());
		}

		// Add children evopools as sources of the composite
		for (HNode n : _ep.getChildren()) {
			if (n instanceof EvoPool) {
				_comp_config.addSource((EvoPool) n);
			}
		}
		try {
			createPopulation();
		} catch (BadConfiguration bc) {
			getNotifier()
					.fatal("Composite manager for EvoPool " + _ep.getName() + ": cannot create population; "
							+ bc.getMessage());
		}
	}



	protected void createPopulation() throws BadConfiguration {

	}



	@Override
	public void finish() {
	}



	@Override
	public int getPopSize() {
		return getConfig().I("pop_size");
	}



	@Override
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		if (getConfig().validate("pop_size", Integer.class) || getConfig().I("pop_size") < 1) {
			bc.append("pop_size is either inset or less than 1.");
		}
		if (_comp_config == null) {
			bc.append("Missing composite configuration.");
		}
		if (_comp_init == null) {
			bc.append("Missing composite initializer.");
		}
		bc.validate();
	}

}
