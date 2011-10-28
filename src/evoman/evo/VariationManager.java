package evoman.evo;


/**
 * 
 * @author ruppmatt
 *
 *	A variation manager provides mutations and fitness filters to create new populations.
 */
public class VariationManager extends EMConfigurableHNode {

	protected EvoPool _ep = null;
	protected EvolutionPipeline _evopipe = null;
	protected int _total_genotypes = 0;
	
	public VariationManager(String name, EvoPool parent){
		super(name,parent);
		_ep = parent;
		addDefault("size", 0, "Default size of the population");
	}
	

	public void evolve(){
		Population new_pop = _evopipe.process(_ep.getPopulation());
		_ep.setPopulation(new_pop);
	}
	
	public void addEP(EvolutionPipeline evopipe){
		_evopipe = evopipe;
	}
	
	public void removeEP(){
		_evopipe = null;
	}
	
	public Genotype makeGenotype(Representation r, Population p){
		String id = String.format("%s-%d", getName(), _total_genotypes++);
		Genotype g = new Genotype(id,p,r);
		return g;
	}
	
	public Population getPoolPopulation(){
		return _ep.getPopulation();
	}
}
