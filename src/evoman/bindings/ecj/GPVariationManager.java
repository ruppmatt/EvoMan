package evoman.bindings.ecj;

import java.util.*;

import ec.*;
import ec.util.*;
import evoman.evo.*;
import evoman.evo.Population;

public class GPVariationManager extends VariationManager{
	
	EMEvolutionState _ecj_state = null;
	MethodHNode _method_dictionary = null;
	Set<String> _operator_paths = new HashSet<String>();

	public GPVariationManager(String name, EvoPool parent) {
		super(name, parent);
	}
	
	public void setDictionary(MethodHNode dict){
		_method_dictionary = dict;
		_ecj_state.setMethodDictionary(dict);
	}
	
	public void addOperator(String path){
		_operator_paths.add(path);
	}
	
	@Override
	public void init(){
		super.init();
		
		//Initialize paramter database
		ParameterDatabase db = new ParameterDatabase();
		setupPopulation(db);
		setupTreeConstraints(db);
		setupFunctionSet(db);
		
		//Initalize EMEvolutionState
		_ecj_state = (EMEvolutionState) Evolve.initialize(db,0);
		_ecj_state.startFresh();
		_ecj_state.setMethodDictionary(_method_dictionary);
		
		_ep.setPopulation(new Population());
		//Retrieve population from ecj and copy to EM Population
		for (Individual i : _ecj_state.population.subpops[0].individuals){
				getPoolPopulation().addGenotype(makeGenotype((Representation) i, getPoolPopulation()));
		}
		
	}
	
		
	
	protected void setupPopulation(ParameterDatabase db){
		db.put("state", "evoman.bindings.ecj.EMEvolutionState");
		db.put("pop", "ec.Population");
		db.put("init", "ec.gp.GPInitializer");
		db.put("subpops", "1");
		db.put("pop.subpop.0", "ec.Subpopulation");
		db.put("pop.subpop.0.species", "ec.gp.GPSpecies");
		db.put("pop.subpop.0.species.ind", "evoman.bindings.ecj.EMIndividual");
		db.put("pop.subpop.0.species.agent", "evoman.bindings.ecj.EMAgent");
		db.put("pop.subpop.0.species.fitness", "ec.simple.SimpleFitness");
		db.put("pop.subpop.0.size", S("size"));
		db.put("pop.subpop.0.species.ind.numtrees", "1");
		db.put("pop.subpop.0.species.ind.tree.0.tc", "TreeConstraints");
		db.put("gp.type.a.size", 1);
		db.put("gp.type.a.0.name", "DType");
	}
	
	protected void setupTreeConstraints(ParameterDatabase db){
		db.put("gp.tc.size", "1");
		db.put("gp.tc.0.init", "ec.gp.koza.HalfBuilder");
		db.put("gp.tc.0.name", "EvoManTC");
		db.put("gp.tc.0.fset", "EvoManFS");
		db.put("gp.tc.0.returns", "DType");
	}
	
	protected void setupFunctionSet(ParameterDatabase db){
		db.put("gp.fs.0", "ec.gp.GPFunctionSet");
		db.put("gp.fs.0.size", "9");
		db.put("gp.fs.0.name", "EvoManFS");
	}

}
