package evoman.ec.mutation;

import java.util.*;

import evoict.graphs.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;

public class EvolutionPipeline extends EMConfigurableHNode {

	public static final int UNCONSTRAINED = Pipeline.UNCONSTRAINED;
	protected Pipeline _pipeline;
	protected VariationManager _vm;
	
	public EvolutionPipeline(String name, VariationManager parent){
		this(name, null, UNCONSTRAINED, UNCONSTRAINED);
	}
	
	
	public EvolutionPipeline(String name, int starts, int terminals){
		this(name, null, starts, terminals);
	}
	
	
	public EvolutionPipeline(String name, VariationManager parent, int starts, int terminals){
		super(name,parent);
		_pipeline = new Pipeline(name);
	}
	
	public void moveTo(EvoPool parent){
		if (_vm != null){
			_vm.removeEP();
		}
		_vm.addEP(this);
	}
	
	public boolean addOperator(DANode o){
		return _pipeline.add(o);
	}
	
	public boolean createFlow(DANode from, DANode to){
		return _pipeline.connect(from, to);
	}
	
	public boolean removeOperator(DANode o){
		return _pipeline.remove(o);
	}
	
	public boolean destroyFlow(DANode from, DANode to){
		return _pipeline.disconnect(from, to);
	}
	
	public boolean isValid(){
		int num_terminals = 0;
		for (DANode n : _pipeline.getNodes()){
			if ( ((EvolutionOperator) n).satisfied() == false )
				return false;
			if ( n instanceof EvolutionOperatorTerminus )
				num_terminals++;
		}
		if (num_terminals != 1)
			return false;
		if (_pipeline.getTerm().size() != 1)
			return false;
		if ( !(_pipeline.getTerm().toArray()[0] instanceof EvolutionOperatorTerminus) )
			return false;
		if (_pipeline.getStart().size() < 1)
			return false;
		return true;
	}
	
	public Population process(Population p){
		EvolutionOperatorTerminus term = (EvolutionOperatorTerminus) _pipeline.getTerm().toArray()[0];
		Collection<DANode> ordering = _pipeline.getBFSOrder();
		for (DANode n : ordering){
			EvolutionOperator operator = (EvolutionOperator) n;
			operator.receive(null, p);
			operator.produce();
		}
		return term.getPopulation();
	}

	
	
}
