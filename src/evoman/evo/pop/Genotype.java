package evoman.evo.pop;


import evoict.*;



/**
 * 
 * @author ruppmatt
 * 
 *         A genotype contains information about its EvoType
 */
public class Genotype implements Cloneable, Printable {

	protected double				_fitness	= 0.0;
	protected Boolean				_evaluated	= false;
	protected final Representation	_rep;
	protected final String			_id;



	/**
	 * Construct a genotype belonging to a particular population and having a
	 * particular EvoType representation
	 * 
	 * @param name
	 * @param p
	 * @param rep
	 */
	public Genotype(String id, Representation rep) {
		_id = id;
		_rep = rep;
	}



	/**
	 * Return the fitness of this genotype
	 * 
	 * @return
	 */
	public double getFitness() {
		return _fitness;
	}



	public void setFitness(double w) {
		_evaluated = true;
		_fitness = w;
	}



	public void reset() {
		_evaluated = false;
	}



	/**
	 * Returns true if the genotype has been recently evaluated
	 * 
	 * @return
	 */
	public Boolean evaluated() {
		return _evaluated;
	}



	/**
	 * Return the EvoType representation
	 * 
	 * @return
	 */
	public Representation rep() {
		return _rep;
	}



	/**
	 * Clone the genotype
	 */
	@Override
	public Object clone() {
		return new Genotype(_id, (Representation) _rep.clone());
	}



	/**
	 * Print out this genotype.
	 */
	@Override
	public String toString() {
		return _id + ":" + "< " + _rep.toString() + " >";
	}
}
