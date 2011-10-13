package evoman.evo;

import java.util.*;

import evoman.tools.*;

public class EvolutionPipeline extends EMConfigurableHNode {

	public static final int UNCONSTRAINED = Pipeline.UNCONSTRAINED;
	protected Pipeline _pipeline;
	
	public EvolutionPipeline(String name){
		this(name, null, UNCONSTRAINED, UNCONSTRAINED);
	}
	
	
	public EvolutionPipeline(String name, int starts, int terminals){
		this(name, null, starts, terminals);
	}
	
	
	public EvolutionPipeline(String name, HNode parent, int starts, int terminals){
		super(name,parent);
		_pipeline = new Pipeline(name);
	}
	
	public boolean addOperator(EvolutionOperator o){
		return _pipeline.add(o);
	}
	
	public boolean createFlow(EvolutionOperator from, EvolutionOperator to){
		return _pipeline.connect(from, to);
	}
	
	public boolean removeOperator(EvolutionOperator o){
		return _pipeline.remove(o);
	}
	
	public boolean destroyFlow(EvolutionOperator from, EvolutionOperator to){
		return _pipeline.disconnect(from, to);
	}
	
	public boolean isValid(){
		for (DANode n : _pipeline.getNodes()){
			if ( ((EvolutionOperator) n).satisfied() == false )
				return false;
		}
		if (_pipeline.getTerm().size() != 1)
			return false;
		if (_pipeline.getStart().size() < 1)
			return false;
		return true;
	}
	
	public void process(){
		Collection<DANode> ordering = _pipeline.getBFSOrder();
		for (DANode n : ordering){
			EvolutionOperator operator = (EvolutionOperator) n;
			operator.produce();
		}
	}
	
}
