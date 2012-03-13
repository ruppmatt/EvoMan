//Test
package evoman.evo.structs;


import java.util.*;

import evoman.config.*;
import evoman.evo.pop.*;
import evoman.evo.sm.*;
import evoman.evo.vm.*;



/**
 * 
 * @author ruppmatt
 * 
 *         An EvoPool contains the tools to operate on both simulations and
 *         genetic representations.
 */
public class EvoPool extends EMConfigurableHNode {

	private static final long					serialVersionUID	= 1L;
	protected VariationManager					_vm					= null;
	protected Population						_pop				= null;
	LinkedHashMap<String, EvoPool>				_ep					= new LinkedHashMap<String, EvoPool>();
	LinkedHashMap<String, SimulationManager>	_sm					= null;



	/**
	 * Construct an EvoPool without a parent.
	 * 
	 * @param name
	 */

	public EvoPool(String name) {
		this(name, null);
	}



	/**
	 * Construct an EvoPool with a particular aprent
	 * 
	 * @param name
	 * @param parent
	 */
	@ConfigConstructor(args = { ConfigArgs.NAME, ConfigArgs.PARENT })
	public EvoPool(String name, EvoPool parent) {
		super(name, parent);
	}



	/**
	 * The leaves must be initialized first so higher-order variation managers
	 * can use genotypes from lower-order pools.
	 */
	@Override
	public void init() {
		super.init();
		if (_vm != null)
			_vm.init();
	}



	/**
	 * Add a child EvoPool to this object
	 * 
	 * @param ep
	 */
	@ConfigOptional()
	public void addEvoPool(EvoPool ep) {
		_ep.put(ep.getName(), ep);
		addChild(ep);
	}



	/**
	 * Add a simulation manager to this object
	 * 
	 * @param sm
	 */
	public void setSM(SimulationManager sm) {
		String name = sm.getName();
		_sm.put(name, sm);
		addChild(sm);
	}



	/**
	 * Add a variation manager to this object
	 * 
	 * @param vm
	 */
	@ConfigOptional()
	public void setVM(VariationManager vm) {
		_vm = vm;
	}



	/**
	 * Get the variation manager associated with this EvoPool
	 * 
	 * @param vm
	 * @return
	 */
	public VariationManager getVM() {
		return _vm;
	}



	public Population getPopulation() {
		return _pop;
	}



	/**
	 * Add a population to this EvoPool
	 * 
	 * @param p
	 */
	public void setPopulation(Population p) {
		_pop = p;
	}



	/**
	 * Remove the population from this EvoPool (if present).
	 * 
	 * @param p
	 */
	public void removePopulation(Population p) {
		if (p == _pop)
			_pop = null;
	}

}
