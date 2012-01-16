package evoman.ec.mutation;


import java.lang.reflect.*;
import java.util.*;

import evoict.graphs.*;
import evoict.io.*;
import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;



/**
 * An EvolutionPipleline is a directed, acyclic graph whose nodes are
 * EvolutionOperators and edges are EvolutionPipes. Both of these classes are
 * only instantiated during the invocation of the process() method. All
 * configuration of these objects are done using their respective configuration
 * objects, EvolutionOpConfig and EvolutionPipeConfig.
 * 
 * Although all EvolutionOpConfig objects may be constructed anywhere, all
 * EvolutionPipes used by the pipeline must be created using the createPipe
 * method to prevent topologically-misconfigured pipelines. Topology is
 * controlled by keeping a shadow-copy of the EvolutionPipleine in a Pipeline
 * graph.
 * 
 * Although non-selection operators may receive populations from any type of
 * evolution operator, selection operators can only receive populations from the
 * initial population or other selection operators.
 * 
 * @author ruppmatt
 * 
 */

public class EvolutionPipeline extends Pipeline implements EMState {

	private static final long													serialVersionUID	= 1L;
	protected VariationManager													_vm;

	// Configuration objects (user configurable)
	protected HashSet<EvolutionOpConfig>										_conf				= new HashSet<EvolutionOpConfig>();
	protected LinkedHashMap<EvolutionOpConfig, ArrayList<EvolutionPipeConfig>>	_pipes				= new LinkedHashMap<EvolutionOpConfig, ArrayList<EvolutionPipeConfig>>();
	protected LinkedHashMap<String, EvolutionOpConfig>							_names;

	// Pipeline to store topography
	protected Pipeline															_pipeline			= new Pipeline();
	protected LinkedHashMap<EvolutionOpConfig, DANode>							_pipe_nodes			= new LinkedHashMap<EvolutionOpConfig, DANode>();
	protected LinkedHashMap<DANode, EvolutionOpConfig>							_pipe_ops			= new LinkedHashMap<DANode, EvolutionOpConfig>();

	// Final constructed operators (user-opaque)
	protected LinkedHashMap<EvolutionPipeConfig, EvolutionPipe>					_conf_pipes			= new LinkedHashMap<EvolutionPipeConfig, EvolutionPipe>();



	public EvolutionPipeline(VariationManager parent) {
		_pipeline = new Pipeline();
		_vm = parent;
		_pipes.put(null, new ArrayList<EvolutionPipeConfig>());
	}



	public boolean addOperator(EvolutionOpConfig conf) {
		if (_names.containsKey(conf.getName())) {
			return false; // Can't have duplicate operators
		} else {

		}
	}



	/**
	 * Create pipes between named evolution operator configurations
	 * 
	 * @param from
	 *            name of sending operator configuration
	 * @param to
	 *            name of receiving operator configuration
	 * @return
	 */
	public EvolutionPipeConfig createPipe(String from, String to) {
		return createPipe(_names.get(from), _names.get(to));
	}



	/**
	 * 
	 * Only pipe configurations created using this method are allowed in a
	 * pipeline.
	 * The Pipeline instance maintains a shadow-copy of the topology of the
	 * EvolutionPipline. Any nodes that create cycles are not allowed to be
	 * created.
	 * 
	 * @param from
	 *            Sending evolution operator configuration
	 * @param to
	 *            Receiving evolution operator configuration
	 * @return Configuration to evolution pipeline
	 */
	public EvolutionPipeConfig createPipe(EvolutionOpConfig from, EvolutionOpConfig to) {
		EvolutionPipeConfig pipe_conf = null;
		if (_pipe_nodes.containsKey(from) && _pipe_nodes.containsKey(to)) {
			DANode _node_from = _pipe_nodes.get(from);
			DANode _node_to = _pipe_nodes.get(to);
			if (_node_from.outEdges() == from.getConstraints().inMax()) {
				getNotifier().fatal(
						"Cannot connect " + from.getName() + " to " + to.getName()
								+ ": exceeded input constraint.");
			} else if (!_pipeline.canConnect(_node_from, _node_to)) {
				getNotifier().fatal("Cannot connect " + from.getName() + " to " + to.getName()
						+ ": connection creates cycle in pipeline.");
			} else if (_pipeline.connect(_node_from, _node_to)) {
				pipe_conf = new EvolutionPipeConfig(from, to);
				to.registerPipe(pipe_conf);
				if (!_pipes.containsKey(from)) {
					_pipes.put(from, new ArrayList<EvolutionPipeConfig>());
				}
				_pipes.get(from).add(pipe_conf);
			}
		} else {
			getNotifier().fatal("Cannot connect " + from.getName() + " to " + to.getName()
					+ ": operator was not created by this pipeline.");
		}
		return pipe_conf;
	}



	/**
	 * Produce a population using the operators in this pipeline.
	 * Only in this method are EvolutionOperators instantiated. They
	 * (along with their pipes) are destroyed after this method ends to
	 * allow for dynamic reconfiguration of the pipeline.
	 * 
	 * @param p
	 *            Receiving population from the VariationManager
	 */
	@SuppressWarnings("unchecked")
	public void process(Population p) {

		// Begin by registering all start operators with a pipe that contains
		// the initial population
		LinkedHashMap<EvolutionOpConfig, EvolutionPipeConfig> start_pipes = new LinkedHashMap<EvolutionOpConfig, EvolutionPipeConfig>();
		for (DANode start : _pipeline.getStart()) {
			EvolutionOpConfig eoc = _pipe_ops.get(start);
			EvolutionPipeConfig start_pipe_conf = new EvolutionPipeConfig(null, eoc);
			eoc.registerPipe(start_pipe_conf);
			EvolutionPipe start_pipe = new EvolutionPipe(start_pipe_conf);
			_conf_pipes.put(start_pipe_conf, start_pipe);
			start_pipe.send(p);
		}

		// Then process the pipeline using a breadth-first search order
		for (DANode n : _pipeline.getEvalOrder()) {
			EvolutionOpConfig conf = _pipe_ops.get(n);
			try {
				Constructor<EvolutionOperator> constr_op = (Constructor<EvolutionOperator>) conf.getOpClass()
						.getConstructor(EvolutionPipeline.class, EvolutionOpConfig.class);
				EvolutionOperator op = constr_op.newInstance(this, conf);
				if (_pipes.containsKey(conf)) {
					for (EvolutionPipeConfig epc : _pipes.get(conf)) {
						EvolutionPipe pipe = new EvolutionPipe(epc);
						_conf_pipes.put(epc, pipe);
						pipe.send(op.produce());
					}
				}
			} catch (Exception e) {
				getNotifier().fatal("Unable to create evolution operator: " + conf.getName());
			}
		}

		// Finish by clearing out pipes and resetting start nodes
		_conf_pipes.clear();
		for (EvolutionOpConfig start : start_pipes.keySet()) {
			start.unregisterPipe(start_pipes.get(start));
		}
	}



	/**
	 * Retrieve a pipeline for a particular pipeline configuration.
	 * Pipelines are only in existence during the invocation of process()
	 * 
	 * @param epc
	 *            configuration of pipeline
	 * @return
	 */
	protected EvolutionPipe getPipe(EvolutionPipeConfig epc) {
		if (_conf_pipes.containsKey(epc)) {
			return _conf_pipes.get(epc);
		} else {
			return null;
		}
	}



	@Override
	public EMState getESParent() {
		return _vm;
	}



	@Override
	public void init() {
		if (!_pipeline.validate()) {
			getNotifier().fatal("Pipeline is not configured correctly.");
		}
	}



	@Override
	public void finish() {
	}



	@Override
	public MersenneTwisterFast getRandom() {
		return _vm.getRandom();
	}



	@Override
	public EMThreader getThreader() {
		return _vm.getThreader();
	}



	@Override
	public Notifier getNotifier() {
		return _vm.getNotifier();
	}
}
