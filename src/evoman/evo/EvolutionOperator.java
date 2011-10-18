package evoman.evo;

import java.util.*;

import evoman.tools.*;

public abstract class EvolutionOperator extends ConfigurableDANode implements EMState {

	EvolutionPipeline _pipeline = null;
	public static final int UNCONSTRAINED = -1;
	protected int _inputs = UNCONSTRAINED;
	protected int _outputs = UNCONSTRAINED;
	
	protected LinkedHashMap<EvolutionOperator,Population> _received = new LinkedHashMap<EvolutionOperator,Population>();

	
	
	public EvolutionOperator(String name, EvolutionPipeline pipeline) {
		this(name, pipeline, UNCONSTRAINED, UNCONSTRAINED);
	}
	
	public EvolutionOperator(String name, EvolutionPipeline pipeline, int inputs, int outputs){
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
	
	@Override
	public EMState getESParent() {
		return _pipeline;
	}

	@Override
	public void init() {	
	}

	@Override
	public void finish() {
	}

	@Override
	public MersenneTwisterFast getRandom() {
		return _pipeline.getRandom();
	}

	@Override
	public void notify(String msg) {
		_pipeline.notify(msg);
	}

	@Override
	public void warn(String msg) {
		_pipeline.warn(msg);
	}

	@Override
	public void fatal(String msg) {
		_pipeline.fatal(msg);
	}

	@Override
	public int getMaxThreads() {
		return _pipeline.getMaxThreads();
	}

	@Override
	public int getRunningThreads() {
		return _pipeline.getRunningThreads();
	}

	@Override
	public int getAvailableThreads() {
		return _pipeline.getAvailableThreads();
	}

	@Override
	public void incThreadCount() {
		_pipeline.incThreadCount();
	}

	@Override
	public void decThreadCount() {
		_pipeline.decThreadCount();
	}

}
