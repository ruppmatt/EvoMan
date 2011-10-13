package evoman.evo;

public abstract class EvolutionOperatorTerminus extends EvolutionOperator {

	Population _final_pop = null;
	
	EvolutionOperatorTerminus(String name, EvolutionPipeline p){
		super(name, p, UNCONSTRAINED, 0);
	}
	
	@Override
	public abstract void produce();

	public Population getPopulation(){
		return _final_pop;
	}

}
