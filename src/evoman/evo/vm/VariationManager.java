package evoman.evo.vm;


import java.io.*;

import evoict.*;
import evoict.io.*;
import evoman.config.*;
import evoman.ec.*;
import evoman.ec.evolution.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



/**
 * 
 * @author ruppmatt
 * 
 *         A variation manager provides mutations and fitness filters to create
 *         new populations.
 */
public abstract class VariationManager implements EMState, Serializable, Settable, Validatable {

	private static final long	serialVersionUID	= 1L;
	protected EvoPool			_ep					= null;
	protected EvolutionPipeline	_evopipeline		= null;
	protected int				_total_genotypes	= 0;
	protected KeyValueStore		_conf				= new KeyValueStore();



	public VariationManager(EvoPool parent) {
		_ep = parent;
		_ep.setVM(this);
	}



	@ConfigOptional()
	public void setEvoPipeline(EvolutionPipeline pipeline) {
		_evopipeline = pipeline;
	}



	@Override
	@ConfigOptional()
	public void set(String key, Object value) {
		_conf.set(key, value);
	}



	public KeyValueStore getConfig() {
		return _conf;
	}



	public void evolve() throws BadConfiguration {
		if (_evopipeline == null) {
			return;
		} else {
			Population result;
			try {
				result = _evopipeline.process(this, getPoolPopulation());
				result.reset();
				_ep.setPopulation(result);
			} catch (BadConfiguration e) {
				throw new BadConfiguration(
						errorPrefix() + "evolution pipleine is invalid.\n"
								+ e.getMessage());
			}

		}
	}



	public Population evolve(Population p) throws BadConfiguration {
		if (_evopipeline == null) {
			return (Population) p.clone();
		} else {
			try {
				return _evopipeline.process(this, p);
			} catch (BadConfiguration e) {
				throw new BadConfiguration(
						errorPrefix() + "evolution pipleine is invalid.\n"
								+ e.getMessage());
			}
		}
	}



	public abstract int getPopSize();



	public Genotype makeGenotype(Representation r) {
		String id = String.format("%s-%d", _ep.getName(), _total_genotypes++);
		Genotype g = new Genotype(id, r);
		return g;
	}



	public Population getPoolPopulation() {
		return _ep.getPopulation();
	}



	@Override
	public void init() {
		if (_evopipeline != null) {
			try {
				_evopipeline.validate();
			} catch (BadConfiguration e) {
				getNotifier().fatal(
						errorPrefix() + "evolution pipleine is invalid.\n"
								+ e.getMessage());
			}
		}
	}



	@Override
	public EMState getESParent() {
		return _ep;
	}



	@Override
	public RandomGenerator getRandom() {
		return _ep.getRandom();
	}



	@Override
	public EMThreader getThreader() {
		return _ep.getThreader();
	}



	@Override
	public Notifier getNotifier() {
		return _ep.getNotifier();
	}



	protected String errorPrefix() {
		return "Variation manager for EvoPool " + _ep.getFullName() + ": ";
	}
}
