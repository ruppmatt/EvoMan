package evoman.ec.evolution;


import java.lang.reflect.*;
import java.util.*;

import evoict.*;
import evoict.graphs.*;
import evoman.evo.pop.*;
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

public class EvolutionPipeline extends Pipeline {

	private static final long													serialVersionUID	= 1L;

	// Configuration objects (user configurable)
	protected HashSet<EvolutionOpConfig>										_conf				= new HashSet<EvolutionOpConfig>();
	protected LinkedHashMap<EvolutionOpConfig, ArrayList<EvolutionPipeConfig>>	_pipes				= new LinkedHashMap<EvolutionOpConfig, ArrayList<EvolutionPipeConfig>>();
	protected LinkedHashMap<String, EvolutionOpConfig>							_names				= new LinkedHashMap<String, EvolutionOpConfig>();

	// Pipeline to store topography
	protected Pipeline															_pipeline			= new Pipeline();
	protected LinkedHashMap<EvolutionOpConfig, DANode>							_pipe_nodes			= new LinkedHashMap<EvolutionOpConfig, DANode>();
	protected LinkedHashMap<DANode, EvolutionOpConfig>							_pipe_ops			= new LinkedHashMap<DANode, EvolutionOpConfig>();

	// Final constructed operators (user-opaque)
	protected LinkedHashMap<EvolutionPipeConfig, EvolutionPipe>					_conf_pipes			= new LinkedHashMap<EvolutionPipeConfig, EvolutionPipe>();



	public EvolutionPipeline(VariationManager parent) {
		_pipeline = new Pipeline();
		_pipes.put(null, new ArrayList<EvolutionPipeConfig>());
	}



	/**
	 * Add an evolution operator configuration to the pipeline.
	 * Configurations should be complete before calling this method.
	 * The method will attempt to validate the configuration settings and
	 * return false if the configuration is not valid.
	 * 
	 * @param conf
	 *            Evolution operator configuration
	 * @return
	 */

	public void addOperator(EvolutionOpConfig conf) throws BadConfiguration {
		if (conf.getName() == null) {
			conf._name = "Operator-" + Integer.toString(_conf.size());
		}
		if (_names.containsKey(conf.getName())) {
			throw new BadConfiguration("Evolution operator with the name " + conf.getName() + " already exists.");
		} else {
			try {
				conf.validate();
			} catch (BadConfiguration bc) {
				throw bc;
			}
			_conf.add(conf);
			_names.put(conf.getName(), conf);
			DANode n = new DANode(_pipeline);
			_pipe_ops.put(n, conf);
			_pipe_nodes.put(conf, n);
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
	public EvolutionPipeConfig createPipe(String from, String to) throws BadConfiguration {
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
	public EvolutionPipeConfig createPipe(EvolutionOpConfig from, EvolutionOpConfig to) throws BadConfiguration {
		EvolutionPipeConfig pipe_conf = null;
		if (_pipe_nodes.containsKey(from) && _pipe_nodes.containsKey(to)) {
			DANode _node_from = _pipe_nodes.get(from);
			DANode _node_to = _pipe_nodes.get(to);
			if (_node_to.inEdges() == to.getConstraints().inMax()) {
				throw new BadConfiguration("Cannot connect " + to.getName() + " to " + to.getName()
						+ ": exceeded input constraint.");
			} else if (!from.getConstraints().usesSelection() && to.getConstraints().usesSelection()) {
				throw new BadConfiguration("Cannot connect a non-selection operator " + (from.getName())
						+ " to a selection operator "
						+ (to.getName()) + ".");
			} else if (!_pipeline.canConnect(_node_from, _node_to)) {
				throw new BadConfiguration("Cannot connect " + from.getName() + " to " + to.getName()
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
			throw new BadConfiguration("Cannot connect " + from.getName() + " to " + to.getName()
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
	public Population process(VariationManager vm, Population p) throws BadConfiguration {

		// Begin by registering all start operators with a pipe that contains
		// the initial population
		Collection<DANode> start_nodes = _pipeline.getStart();
		LinkedHashMap<EvolutionOpConfig, EvolutionPipeConfig> start_pipes = new LinkedHashMap<EvolutionOpConfig, EvolutionPipeConfig>();
		for (DANode node : start_nodes) {
			EvolutionOpConfig start_conf = _pipe_ops.get(node);
			EvolutionPipeConfig new_pipe = new EvolutionPipeConfig(null, start_conf);
			start_conf.registerPipe(new_pipe);
			start_pipes.put(start_conf, new_pipe);
			EvolutionPipe pipe = new EvolutionPipe(new_pipe);
			pipe.send(p);
			_conf_pipes.put(new_pipe, pipe);
		}

		Population result = null;
		// Every pipeline should have exactly one terminal node
		EvolutionOpConfig terminal = _pipe_ops.get(_pipeline.getTerminal().toArray()[0]);

		// Then process the pipeline using a breadth-first search order
		for (DANode n : _pipeline.getEvalOrder()) {
			EvolutionOpConfig conf = _pipe_ops.get(n);
			// System.err.println("\n\nAbout to process " + conf.getName());
			try {
				Constructor<EvolutionOperator> constr_op = (Constructor<EvolutionOperator>) conf.getOpClass()
						.getConstructor(EvolutionPipeline.class, EvolutionOpConfig.class);
				EvolutionOperator op = constr_op.newInstance(this, conf);
				try {
					Population produced = op.produce(vm);
					// System.err.println("Success: " + conf.getName());
					if (conf == terminal) {
						result = produced;
					} else if (_pipes.containsKey(conf)) {
						for (EvolutionPipeConfig epc : _pipes.get(conf)) {
							EvolutionPipe pipe = new EvolutionPipe(epc);
							// System.println("Filled pipe " + epc.toString() +
							// " with " + pipe);
							_conf_pipes.put(epc, pipe);
							pipe.send(produced);
						}
					} else {
						vm.getNotifier().warn("Evolution operator results ignored: " + conf.getName());
					}
				} catch (BadConfiguration bc) {
					throw new BadConfiguration("Unable to produce population for operator named " + conf.getName()
							+ "\n" +
							bc.getMessage());
				}

			} catch (Exception e) {
				System.err.println(e.toString());
				e.printStackTrace();
				throw new BadConfiguration("Unable to create and/or execute evolution operator: " + conf.getName()
						+ "\n" + e.getMessage());
			}
		}

		// Cleanup
		_conf_pipes.clear();
		for (EvolutionOpConfig conf : start_pipes.keySet()) {
			conf.unregisterPipe(start_pipes.get(conf));
		}
		return result;
	}



	protected Population receivePopulation(EvolutionPipeConfig epc) {
		Population p = null;
		if (_conf_pipes.containsKey(epc)) {
			EvolutionPipe pipe = _conf_pipes.get(epc);
			p = pipe.receive();
			_conf_pipes.remove(epc); // remove the pipes as they are drained
		}
		return p;

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
		// System.err.println("Attempting to access pipe: " + epc);
		if (_conf_pipes.containsKey(epc)) {
			return _conf_pipes.get(epc);
		} else {
			return null;
		}
	}



	@Override
	public void validate() throws BadConfiguration {
		BadConfiguration bc = new BadConfiguration();
		try {
			super.validate();
		} catch (BadConfiguration par_bc) {
			bc.append(par_bc.getMessage());
		}
		for (EvolutionOpConfig op : _conf) {
			Class<? extends EvolutionOperator> cl = op.getOpClass();
			try {
				Method m = cl.getMethod("validate", EvolutionOpConfig.class);
				try {
					m.invoke(null, op);
				} catch (Exception e) {
					bc.append("Unable to access validation method for operator" + op.getName() + " on class "
							+ cl.toString());
				}
			} catch (SecurityException e) {
				bc.append("Validation method for " + cl.toString() + " is not accessible for operator " + op.getName());
			} catch (NoSuchMethodException e) {
				// Nothing to validate
			}
		}
	}

}
