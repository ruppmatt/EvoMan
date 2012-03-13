package evoman.evo.pop;


import java.util.*;

import evoict.*;
import evoman.evo.structs.*;



/**
 * 
 * @author ruppmatt
 * 
 *         A population is a collection of Genotypes
 */
public class Population implements Cloneable, Printable {

	protected ArrayList<Genotype>	_genotypes	= new ArrayList<Genotype>();



	/**
	 * Construct a population and assign it to an EvoPool
	 * 
	 * @param name
	 * @param ep
	 */
	public Population() {
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



	public boolean placeGenotype(Genotype g, EMState state, Genotype... parents) {
		int ndx = state.getRandom().nextInt(size());
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



	public Genotype getRandomGenotype(EMState state) {
		int ndx = (_genotypes.size() > 0) ? state.getRandom().nextInt(_genotypes.size()) : -1;
		return (ndx == -1) ? null : _genotypes.get(ndx);
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
		ep.setPopulation(this);
	}



	/**
	 * Clone this population.
	 */
	@Override
	public Object clone() {
		Population cl = new Population();
		for (Genotype g : _genotypes)
			cl._genotypes.add((Genotype) g.clone());
		return cl;
	}

}
