package evoman.evo.vm;

import java.util.*;

import evoman.evo.pop.*;
import evoman.evo.structs.*;

public abstract class GenericEvolutionOperator extends EMConfigurableDANode implements EvolutionOperator {

	protected LinkedHashMap<EvolutionOperator,Population> _received = new LinkedHashMap<EvolutionOperator,Population>();
	protected int _inputs = UNCONSTRAINED;
	protected int _outputs = UNCONSTRAINED;
	
	public GenericEvolutionOperator(String name, EvolutionPipeline pipeline) {
		this(name, pipeline, UNCONSTRAINED, UNCONSTRAINED);
	}
	
	public GenericEvolutionOperator(String name, EvolutionPipeline pipeline, int inputs, int outputs){
		super(name, pipeline._pipeline);
		_inputs = inputs;
		_outputs = outputs;
	}

	public boolean satisfied(){
		return ( connectedFrom().size() == _inputs && connectedTo().size() == _outputs );
	}
	
	
	public void receive(EvolutionOperator from, Population p){
		_received.put(from,(Population) p.clone());
	}
	
	public abstract void produce();

	
	public void reset(){
		_received.clear();
	}
	
	public int numInputs(){
		return _inputs;
	}
	
	public int numOutputs(){
		return _outputs;
	}
}
