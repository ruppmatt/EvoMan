package evoman.evo.vm;


import evoman.ec.mutation.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



/**
 * 
 * @author ruppmatt
 * 
 *         A variation manager provides mutations and fitness filters to create
 *         new populations.
 */
public class VariationManager extends EMConfigurableHNode {

	private static final long	serialVersionUID	= 1L;
	protected EvoPool			_ep					= null;
	protected EvolutionPipeline	_evopipe			= null;
	protected int				_total_genotypes	= 0;
	protected MethodHNode		_dictionary			= null;



	public VariationManager(String name, EvoPool parent) {
		super(name, parent);
		_ep = parent;
		addDefault("size", 100, "Default size of the population");
	}



	public void evolve() {
		Population new_pop = _evopipe.process(_ep.getPopulation());
		_ep.setPopulation(new_pop);
	}



	public void addDictionary(MethodHNode dict) {
		_dictionary = dict;
	}



	public void removeMethodDictionary() {
		_dictionary = null;
	}



	public MethodHNode getMethodDictionary() {
		return _dictionary;
	}



	public void addEP(EvolutionPipeline evopipe) {
		_evopipe = evopipe;
	}



	public void removeEP() {
		_evopipe = null;
	}



	public Genotype makeGenotype(Representation r) {
		String id = String.format("%s-%d", getName(), _total_genotypes++);
		Genotype g = new Genotype(id, r);
		return g;
	}



	public Population getPoolPopulation() {
		return _ep.getPopulation();
	}
}
