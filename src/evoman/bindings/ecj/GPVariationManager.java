package evoman.bindings.ecj;

import java.util.*;

import ec.util.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;

public class GPVariationManager extends VariationManager{
	
	EMEvolutionState _ecj_state = null;  //ECJ's evolution state object
	MethodHNode _method_dictionary = null; //Method dictionary of this VM
	ArrayList<GPOperator> _operators = new ArrayList<GPOperator>();  //A list of GP internal node functions
	Collection<String> _types = new ArrayList<String>(); //A list of the tree's node types
	

	public GPVariationManager(String name, EvoPool parent) {
		super(name, parent);
		addDefault("tree_return_type", "DType", "Default return type of the GP tree.");
		addDefault("duplicate_retries", 100, "Number of retries to prevent duplicates.");
		addDefault("min_depth", 2, "Minimum depth of the tree.");
		addDefault("max_depth", 6, "Maximum depth of the tree.");
		addDefault("grow_pick", 0.5, "Pick-Grow probability for Koza HalfBuilder.");
		addDefault("grow_min_depth", 5, "");
		addDefault("grow_max_depth", 5, "");
	}
	
	/**
	 * Sets the method dictionary for this VM.
	 * This method should be called prior to init()
	 * @param dict
	 */
	public void setDictionary(MethodHNode dict){
		_method_dictionary = dict;
		_ecj_state.setMethodDictionary(dict);
	}
	
	/**
	 * Add GP node operator.  All operators must be added
	 * to the variation manager prior to init()
	 * @param op
	 */
	public void addOperator(GPOperator op){
		addChild(op);
		_operators.add(op);
	}
	
	@Override
	public void init(){
		super.init();
		
		//Initialize paramter database
		ParameterDatabase db = new ParameterDatabase();
		System.err.println("Setting up  ECJ paramter database.");
		setupPopulation(db);
		setupTreeConstraints(db);
		setupNodeConstraints(db);
		setupFunctionSet(db);
		setupDataTypes(db);
		
		//db.list(System.err);
		
		//Initalize EMEvolutionState
		System.err.println("Setting up ECJ Evolution State.");
		_ecj_state = new EMEvolutionState();
		_ecj_state.parameters = db;
		_ecj_state.random = new ec.util.MersenneTwisterFast[1];
		_ecj_state.random[0] = new MersenneTwisterFast(0);  //TODO: need to getRandom()
		_ecj_state.setMethodDictionary(_method_dictionary);
		_ecj_state.setup();
		System.err.println("Creating initial population.");
		//_ecj_state.startFresh();
		//System.err.println("Transfering ECJ population to EM.");
		initializePopulation();
		_ep.setPopulation(new Population());
	}
	
	
	protected void initializePopulation(){
		EMGPTree test = new EMGPTree();
		test.setup(_ecj_state, new Parameter("gp.tc.0"));
		System.err.println("Created test EMGPTree");
	}
	
	/**
	 * Returns ECJ's evolution state.
	 * @return
	 */
	public EMEvolutionState getEvolutionState(){
		return _ecj_state;
	}
		
	/**
	 * The population parameters are for the most part hardcoded.
	 * Only a few things like population size or dupicate retries are configurable
	 * by the user.
	 * @param db
	 */
	protected void setupPopulation(ParameterDatabase db){
		db.put("state", "evoman.bindings.ecj.EMEvolutionState");
		db.put("pop", "ec.Population");
		db.put("init", "ec.gp.GPInitializer");
		//db.put("finish", "ec.simple.SimpleFinisher");
		//db.put("breed", "ec.simple.SimpleBreeder");
		//db.put("eval", "ec.simple.SimpleEvaluator");
		//db.put("stat", "ec.simple.SimpleStatistics");
		//db.put("exch", "ec.simple.SimpleExchanger");
		db.put("pop.subpops", "1");
		db.put("pop.subpop.0", "ec.Subpopulation");
		db.put("pop.subpop.0.species", "ec.gp.GPSpecies");
		db.put("pop.subpop.0.species.ind", "evoman.bindings.ecj.EMIndividual");
		db.put("pop.subpop.0.species.agent", "evoman.bindings.ecj.EMAgent");
		db.put("pop.subpop.0.species.fitness", "ec.simple.SimpleFitness");
		db.put("pop.subpop.0.size", S("size"));
		db.put("pop.subpop.0.duplicate-retries", S("duplicate_retries"));
		db.put("pop.subpop.0.species.ind.numtrees", "1");
		db.put("pop.subpop.0.species.ind.tree.0.tc", "TreeConstraints");
		db.put("gp.species.pipe", "evoman.bindings.ecj.EMGPBreedingPipeline");
		db.put("pop.subpop.0.species.pipe.num-sources", "0");
		db.put("breedthreads", "1");
		db.put("evalthreads", "1");
		//db.put("verbosity", "1");
		db.put("seed.0", "1");
		//db.put("checkpoint", "false");
		db.put("prefix", "/dev/null");
		//db.put("checkpoint-modulo", "1");
		//db.put("generations", "1");
		//db.put("eval.problem", "evoman.bindings.ecj.EMProblem");
		//db.put("gp.problem.stack", "ec.gp.ADFStack");
		//db.put("gp.adf-stack.context", "ec.gp.ADFContext");
		//db.put("eval.problem.data", "ec.app.tutorial4.DoubleData");
	}
	
	/**
	 * Most tree constraints are controlled by the user.  The koza builder is used
	 * in this implementation.
	 * @param db
	 */
	protected void setupTreeConstraints(ParameterDatabase db){
		db.put("gp.tc.size", "1");
		db.put("gp.tc.0", "ec.gp.GPTreeConstraints");
		db.put("gp.tc.0.init", "ec.gp.koza.HalfBuilder");
		db.put("gp.tc.0.name", "EvoManTC");
		db.put("gp.tc.0.fset", "EvoManFS");
		db.put("gp.tc.0.returns", S("tree_return_type"));
		db.put("gp.tc.0.init.max-depth", S("max_depth"));
		db.put("gp.tc.0.init.min-depth", S("min_depth"));
		db.put("gp.koza.half.min-depth", S("min_depth"));
		db.put("gp.koza.half.max-depth", S("max_depth"));
		db.put("gp.koza.half.growp", S("grow_pick"));
		db.put("gp.koza.grow.min-depth", S("grow_min_depth"));
		db.put("gp.koza.grow.max-depth", S("grow_max_depth"));
	}
	
	/**
	 * Node constraints are derived from the operators added to the
	 * varation manager prior to initialization.  Keep in mind that the
	 * only terminal-type node in our tree is included in the constraints.
	 * It will always be assigned to be Constraint-0.
	 * @param db
	 */
	protected void setupNodeConstraints(ParameterDatabase db){
		LinkedHashMap<GPNodeConstraint,Integer> observed = new LinkedHashMap<GPNodeConstraint, Integer>();
		int unique_constraints = 0;
		
		//Add EM-ERC Constraint
		GPNodeConstraint emerc_nc = new GPNodeConstraint("DType");
		observed.put(emerc_nc, unique_constraints++);
				
		//Add unique constraints from operators
		for (GPOperator op : _operators){
			GPNodeConstraint thiscon = new GPNodeConstraint(op);
			if (observed.containsKey(thiscon)){
				op.setConstraint(String.format("Constraint-%d", observed.get(thiscon)));
			} else {
				observed.put(thiscon, unique_constraints);
				op.setConstraint(String.format("Constraint-%d", observed.get(thiscon)));
					unique_constraints++;
			}
		}
		
		//Add the constraints to the parameter database
		db.put("gp.nc.size", Integer.toString(unique_constraints));
		for (GPNodeConstraint con : observed.keySet()){
			int ndx = observed.get(con);
			String prefix = String.format("gp.nc.%d", ndx);
			db.put(prefix, "ec.gp.GPNodeConstraints");
			db.put(prefix+".name", String.format("Constraint-%d",ndx));
			db.put(prefix+".returns", con.getReturns());
			db.put(prefix+".size", Integer.toString(con.getNumChildren()));
			ArrayList<String> child_types = con.getChildTypes();
			for (int k = 0; k < con.getNumChildren(); k++){
				db.put(prefix+String.format(".child.%d",k), child_types.get(k));
			}
		}
	}
	
	
	/**
	 * The data types are determined by observing the operators return types.
	 * For the most part, these will always be DType.
	 * @param db
	 */
	protected void setupDataTypes(ParameterDatabase db){
		HashSet<String> types = new HashSet<String>();
		
		//Tree return type
		types.add(S("tree_return_type"));
		
		//Operator types
		for (GPOperator op : _operators){
			types.add(op.getReturnType());
			for (String child : op.getChildTypes()){
				types.add(child);
			}
		}
		String prefix = "gp.type.a";
		db.put(prefix+".size", Integer.toString(types.size()));
		Object[] list =  types.toArray();
			for (int k=0; k < list.length; k++){
			db.put(prefix+String.format(".%d.name",k), (String) list[k]);
		}
	}
	
	
	/**
	 * The function sets are derived from the operators added to the
	 * variation manager prior to initialization.  The ephemeral random constant
	 * EMERC, which is the only terminal in this GP tree, is added as the first
	 * function (0) in the function set.
	 * @param db
	 */
	protected void setupFunctionSet(ParameterDatabase db){
		db.put("gp.fs.size", "1");
		String prefix = "gp.fs.0";
		db.put(prefix, "ec.gp.GPFunctionSet");
		db.put(prefix+".size", Integer.toString(_operators.size()+1)); //Add 1 for EMERC
		
		db.put(prefix+".name", "EvoManFS");
		
		String em_erc_prefix = prefix + String.format(".func.%d", 0);		
		db.put(em_erc_prefix, "evoman.bindings.ecj.EMERC");
		db.put(em_erc_prefix + ".nc", "Constraint-0");

		for (int k=0; k<_operators.size(); k++){
			String op_prefix = prefix + String.format(".func.%d", k+1);
			GPOperator op = _operators.get(k);
			db.put(op_prefix, op.getPath());
			db.put(op_prefix+".nc", op.getConstraint());
		}
		
	}
	
	/**
	 * Returns a dictionary of the keys and stringified-values of the ECJ
	 * parameter database maintained by this variation manager.
	 * @return
	 */
	public LinkedHashMap<String,String> mapParameterDatabase(){
		ParameterDatabase db = _ecj_state.parameters;
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		Set<Object> keys = db.keySet();
		for (Object key : keys){
			map.put(key.toString(), db.getProperty((String) key).toString());
		}
		return map;
	}

}
