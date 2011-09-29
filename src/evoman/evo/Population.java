package evoman.evo;

import java.util.Collection;
import java.util.LinkedHashMap;
import evoman.tools.Printable;

/**
 * 
 * @author ruppmatt
 *
 *		A population is a collection of Genotypes
 */
public class Population extends EMHierarchical implements Cloneable, Printable {

	protected LinkedHashMap<String,Genotype> _genotypes =
			new LinkedHashMap<String,Genotype>();
	protected EvoPool _ep;
	
	
	/**
	 * Construct a Population without a parent
	 * @param name
	 */
	public Population(String name){
		this(name, null);
	}
	
	
	/**
	 * Construct a population and assign it to an EvoPool
	 * @param name
	 * @param ep
	 */
	public Population(String name, EvoPool ep){
		super(name, ep);
		_ep = ep;
	}
	
	/**
	 * Add a Genotype to this population
	 * @param g
	 */
	public void addGenotype(Genotype g){
		_genotypes.put(g.getName(), g);
		addChild(g);
	}
	
	/**
	 * Remove a genotype from this pool.
	 * @param g
	 */
	public void removeGenotype(Genotype g){
		String name = g.getName();
		if (_genotypes.containsKey(name))
			_genotypes.remove(name);
		removeChild(g);
	}
	
	/**
	 * Try to get a genotype from this population with a particular name.
	 * @param name
	 * @return
	 */
	public Genotype getGenotype(String name){
		return (_genotypes.containsKey(name)) ? _genotypes.get(name) : null;
	}
	
	/**
	 * Return the number of genotypes in the population.
	 * @return
	 */
	public int size(){
		return _genotypes.size();
	}
	
	/**
	 * Return the EvoType printer.
	 * @return
	 */
	public EvoTypePrinter getETPrinter(){
		return (_ep != null) ? _ep.getETPrinter() : null;
	}
	
	/**
	 * Move this population to another EvoPool
	 * @param ep
	 */
	public void moveTo(EvoPool ep){
		if (_ep != null)
			_ep.removePopulation(this);
		_ep = ep;
		_ep.addPopulation(this);
	}
	
	/**
	 * Clone this population.
	 */
	public Population clone(){
		Population cl = new Population(_name, null);
		for (String g : _genotypes.keySet())
			cl._genotypes.put(g, _genotypes.get(g));
		return cl;
	}
	
	/**
	 * Print this population as a string.
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ " + getFullName() + " ]" + ENDL);
		Collection<Genotype> g = _genotypes.values();
		if (g.size() > 0)
			sb.append(g.toString());
		for (int k=1; k < _genotypes.size(); k++)
			sb.append(ENDL + g.toString());
		return sb.toString();
	}
	

}
