package evoman.ec.mutation;


import java.util.*;

import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;



public abstract class GenericEvolutionOperator extends EMConfigurableDANode implements Constants, EvolutionOperator {

	private static final long								serialVersionUID	= 1L;
	protected LinkedHashMap<EvolutionOperator, Population>	_received			= new LinkedHashMap<EvolutionOperator, Population>();
	protected int											_inputs				= UNCONSTRAINED;
	protected int											_outputs			= UNCONSTRAINED;



	public GenericEvolutionOperator(String name, EvolutionPipeline pipeline) {
		this(name, pipeline, UNCONSTRAINED, UNCONSTRAINED);
	}



	public GenericEvolutionOperator(String name, EvolutionPipeline pipeline, int inputs, int outputs) {
		super(name, pipeline._pipeline, pipeline.getESParent());
		_inputs = inputs;
		_outputs = outputs;
	}



	@Override
	public boolean satisfied() {
		return (connectedFrom().size() == _inputs && connectedTo().size() == _outputs);
	}



	@Override
	public void receive(EvolutionOperator from, Population p) {
		_received.put(from, p.clone());
	}



	@Override
	public abstract void produce();



	@Override
	public void reset() {
		_received.clear();
	}



	public int numInputs() {
		return _inputs;
	}



	public int numOutputs() {
		return _outputs;
	}
}
