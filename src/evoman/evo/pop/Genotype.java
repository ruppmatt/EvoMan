package evoman.evo.pop;

import evoman.evo.structs.*;
import evoman.tools.*;

/**
 * 
 * @author ruppmatt
 *
 *	A genotype contains information about its EvoType
 */
public class Genotype extends EMHNode implements Cloneable, Printable{
	
	protected Population _pop;
	protected final double _fitness = 0.0;
	protected Boolean _evaluated = false;
	protected final Representation _rep;
	
	/**
	 * Construct a genotype belonging to a particular population and having a particular EvoType representation
	 * @param name
	 * @param p
	 * @param rep
	 */
	public Genotype(String name, Population p, Representation rep){
		super(name, p);
		_pop = p;
		_rep = rep;
	}
	
	/**
	 * Move this genotype to another population
	 * @param p
	 */
	public void moveTo(Population p){
		_pop.removeGenotype(this);
		_pop = p;
		_pop.addGenotype(this);
	}
	
	/**
	 * Return the fitness of this genotype
	 * @return
	 */
	public double getFitness(){
		return _fitness;
	}
	
	/**
	 * Returns true if the genotype has been recently evaluated
	 * @return
	 */
	public Boolean evaluated(){
		return _evaluated;
	}
	
	/**
	 * Return the EvoType representation
	 * @return
	 */
	public Representation rep(){
		return _rep;
	}
	
	/**
	 * Clone the genotype
	 */
	public Object clone(){
		return new Genotype(_name, null, _rep);
	}
	
	/**
	 * Print out this genotype.
	 */
	public String toString(){
		return getFullName() + "< " + _rep.toString() + " >";
	}
}
