package evoman.evo.vm;


import java.io.*;

import evoict.*;
import evoict.io.*;
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
public abstract class VariationManager implements EMState, Serializable {

	private static final long				serialVersionUID	= 1L;
	protected EvoPool						_ep					= null;
	protected EvolutionPipeline				_evopipeline		= null;
	protected int							_total_genotypes	= 0;
	protected final VariationManagerConfig	_config;



	public VariationManager(EvoPool parent, VariationManagerConfig conf) {
		_ep = parent;
		_config = conf;
		_ep.setVM(this);
	}



	public VariationManagerConfig getConfig() {
		return _config;
	}



	public void evolve() {
		if (_evopipeline == null) {
			return;
		} else {
			Population result = _evopipeline.process(getPoolPopulation());
			result.reset();
			_ep.setPopulation(result);
		}
	}



	public Population evolve(Population p) {
		if (_evopipeline == null) {
			return (Population) p.clone();
		} else {
			return _evopipeline.process(p);
		}
	}



	public abstract int getPopSize();



	public void addEP(EvolutionPipeline ep) {
		_evopipeline = ep;
	}



	public void removeEP() {
		_evopipeline = null;
	}



	public Genotype makeGenotype(Representation r) {
		String id = String.format("%s-%d", _ep.getName(), _total_genotypes++);
		Genotype g = new Genotype(id, r);
		return g;
	}



	public Population getPoolPopulation() {
		return _ep.getPopulation();
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
}
