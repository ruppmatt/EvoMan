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

	protected ArrayList<Genotype>	_genotypes	= new ArrayList<Genotype>();
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



	public void reset() {
		for (Genotype g : _genotypes) {
			g.reset();
		}
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
	public Object clone() {
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
		return _state.getRandom();
	}



	@Override
	public EMThreader getThreader() {
		return _state.getThreader();
	}



	@Override
	public Notifier getNotifier() {
		return _state.getNotifier();
	}

}
