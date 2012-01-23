package evoman.evo.pop;


import java.util.*;

import evoict.*;
import evoict.io.*;
import evoman.evo.structs.*;



/**
 * 
 * @author ruppmatt
 * 
 *         A population is a collection of Genotypes
 */
public class Population implements EMState, Cloneable, Printable {

	protected ArrayList<Genotype>	_genotypes		= new ArrayList<Genotype>();
	protected Notifier				_local_notifier	= null;
	protected RandomGenerator		_local_random	= null;
	protected EMState				_state;



	/**
	 * Construct a population and assign it to an EvoPool
	 * 
	 * @param name
	 * @param ep
	 */
	public Population(EMState state) {
		_state = state;
	}



	/**
	 * Add a Genotype to this population
	 * 
	 * @param g
	 */
	public void addGenotype(Genotype g) {
		_genotypes.add(g);
	}



	/**
	 * Remove a genotype from this pool.
	 * 
	 * @param g
	 */
	public void removeGenotype(Genotype g) {
		_genotypes.remove(g);
	}



	public boolean replaceGenotype(Genotype original, Genotype replacement) {
		int ndx = _genotypes.indexOf(original);
		if (ndx == -1)
			return false;
		_genotypes.set(ndx, replacement);
		return true;
	}



	public boolean placeGenotype(Genotype g, Genotype... parents) {
		int ndx = getRandom().nextInt(size());
		_genotypes.set(ndx, g);
		return true;
	}



	/**
	 * Try to get a genotype from this population with a particular name.
	 * 
	 * @param name
	 * @return
	 */
	public Genotype getGenotype(int ndx) {
		if (ndx > _genotypes.size() - 1 || ndx < 0) {
			return null;
		} else {
			return _genotypes.get(ndx);
		}
	}



	public ArrayList<Genotype> getGenotypes() {
		return _genotypes;
	}



	/**
	 * Return the number of genotypes in the population.
	 * 
	 * @return
	 */
	public int size() {
		return _genotypes.size();
	}



	/**
	 * Move this population to another EvoPool
	 * 
	 * @param ep
	 */
	public void moveTo(EvoPool ep) {
		_state = ep;
		ep.setPopulation(this);
	}



	/**
	 * Clone this population.
	 */
	@Override
	public Population clone() {
		Population cl = new Population(_state);
		for (Genotype g : _genotypes)
			cl._genotypes.add((Genotype) g.clone());
		return cl;
	}



	@Override
	public EMState getESParent() {
		return _state;
	}



	@Override
	public void init() {
	}



	@Override
	public void finish() {
	}



	@Override
	public RandomGenerator getRandom() {
		if (_state != null) {
			return _state.getRandom();
		} else {
			if (_local_random == null) {
				_local_random = new RandomGenerator();
			}
			return _local_random;
		}
	}



	@Override
	public EMThreader getThreader() {
		if (_state != null)
			return _state.getThreader();
		else
			return null;
	}



	@Override
	public Notifier getNotifier() {
		if (_state != null) {
			return _state.getNotifier();
		} else {
			if (_local_notifier == null)
				_local_notifier = new Notifier();
			return _local_notifier;
		}
	}

}
