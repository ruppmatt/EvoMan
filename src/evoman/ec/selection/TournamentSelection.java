package evoman.ec.selection;

import java.util.*;

import evoict.graphs.*;
import evoman.ec.mutation.*;
import evoman.evo.pop.*;

public class TournamentSelection extends GenericEvolutionOperator {
	
	public TournamentSelection(EvolutionPipeline ep){
		this("TournamentSelection", ep);
	}
	
	public TournamentSelection(String name, EvolutionPipeline ep){
		super(name, ep);
		addDefault("tour_size", 3, "The size of the tournament.");
		addDefault("pop_size", UNCONSTRAINED, "The size of the resulting population.");
	}
	
	public boolean satisfied(){
		return (I("tour_size") > 0 && I("pop_size") > 0 && super.satisfied());
	}

	@Override
	public void produce() {
		int num_genotypes = 0;
		int tour_size = I("tour_size");
		int new_size = (I("pop_size") == UNCONSTRAINED) ? num_genotypes : I("pop_size");
		
		for (Population p : _received.values()){
			num_genotypes += p.size();
		}
		ArrayList<Genotype> all = new ArrayList<Genotype>(num_genotypes);
		for (Population p : _received.values()){
			all.addAll(p.getGenotypes());
		}
		Population selected = new Population();
		for (int i = 0; i < new_size; i++){
			Genotype winner = all.get(getRandom().nextInt(num_genotypes));
			for (int k = 1; k < tour_size; k++){
				Genotype competitor = all.get(getRandom().nextInt(num_genotypes));
				winner = (winner.getFitness() < competitor.getFitness()) ? competitor : winner;
			}
			selected.addGenotype(winner);
		}
		
		for (DANode node : connectedTo()){
			((EvolutionOperator) node).receive(this, selected);
		}
	}

}
