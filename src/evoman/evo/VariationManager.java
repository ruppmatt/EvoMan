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
	
	public VariationManager(String name, EvoPool parent){
		super(name,parent);
		_ep = parent;
		addDefault("size", 0, "Default size of the population");
	}
	
	public void populate(){
	}
	
	public void evolve(){
		Population new_pop = _evopipe.process(_ep.getPopulation());
		_ep.addPopulation(new_pop);
	}
	
	public void addEP(EvolutionPipeline evopipe){
		_evopipe = evopipe;
	}
	
	public void removeEP(){
		_evopipe = null;
	}
}
