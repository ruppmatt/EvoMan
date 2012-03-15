//Test
package evoman.evo.structs;


import java.io.*;
import java.util.*;

import evoict.*;
import evoict.io.*;
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
public class EvoPool implements EMState, Configurable, Identifiable, Serializable {

	private static final long					serialVersionUID	= 1L;
	protected String							_name				= null;
	protected EvoPool							_parent				= null;
	protected VariationManager					_vm					= null;
	protected Population						_pop				= null;
	LinkedHashMap<String, EvoPool>				_ep					= new LinkedHashMap<String, EvoPool>();
	LinkedHashMap<String, SimulationManager>	_sm					= null;

	RandomGenerator								_rand				= null;
	EMThreader									_threader			= null;
	Notifier									_notifier			= null;

	KeyValueStore								_kv					= new KeyValueStore();



	/**
	 * Construct an EvoPool without a parent.
	 * 
	 * @param name
	 */

	public EvoPool(String name) {
		this(name, null);
	}



	/**
	 * Construct an EvoPool with a particular parent
	 * 
	 * @param name
	 * @param parent
	 */
	@ConfigConstructor(args = { ConfigArgs.NAME, ConfigArgs.PARENT })
	public EvoPool(String name, EvoPool parent) {
		_name = name;
		_parent = parent;
		if (_parent != null)
			_parent.addEvoPool(this);
	}



	@Override
	public String getName() {
		return _name;
	}



	public String getFullName() {
		return (_parent == null) ?
				_name : _parent.getFullName() + "." + _name;
	}



	/**
	 * The leaves must be initialized first so higher-order variation managers
	 * can use genotypes from lower-order pools.
	 */
	@Override
	public void init() {
		for (EvoPool child : _ep.values()) {
			child.init();
		}
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
	}



	/**
	 * Add a simulation manager to this object
	 * 
	 * @param sm
	 */
	public void setSM(SimulationManager sm) {
		String name = sm.getName();
		_sm.put(name, sm);
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



	/**
	 * Return the child EvoPools
	 * 
	 * @return
	 */
	public Collection<EvoPool> getChildPools() {
		return _ep.values();
	}



	@Override
	public RandomGenerator getRandom() {
		if (_rand == null) {
			if (_parent == null) {
				if (_kv.isSet("seed")) {
					_rand = new RandomGenerator(_kv.I("seed"));
				} else {
					_rand = new RandomGenerator();
				}
			}
			else {
				_rand = _parent.getRandom();
			}
		}

		return _rand;
	}



	@Override
	public EMThreader getThreader() {
		if (_threader == null) {
			if (_parent != null)
				_parent.getThreader();
			else {
				if (_kv.isSet("max_threads"))
					_threader = new EMThreader(this, I("max_threads"));
				else
					_threader = new EMThreader(this, 1);
			}
		}
		return _threader;
	}



	@Override
	public Notifier getNotifier() {
		if (_notifier == null) {
			if (_parent == null)
				_notifier = new Notifier();
			else {
				return _parent.getNotifier();
			}
		}
		return _notifier;
	}



	@Override
	public Object get(String name) {
		return _kv.get(name);
	}



	@Override
	public void set(String name, Object value) {
		_kv.set(name, value);
	}



	@Override
	public void unset(String name) {
		_kv.unset(name);
	}



	@Override
	public Boolean isSet(String name) {
		return _kv.isSet(name);
	}



	@Override
	public Integer I(String name) {
		return _kv.I(name);
	}



	@Override
	public Double D(String name) {
		return _kv.D(name);
	}



	@Override
	public String S(String name) {
		return _kv.S(name);
	}



	@Override
	public Boolean validate(String name, Class<?> cl) {
		return _kv.validate(name, cl);
	}



	@Override
	public EMState getESParent() {
		return _parent;
	}



	@Override
	public void finish() {
	}

}
