//Test
package evoman.evo.structs;

import java.util.*;

import evoict.graphs.*;
import evoman.evo.pop.*;
import evoman.evo.sm.*;
import evoman.evo.vm.*;



/**
 * 
 * @author ruppmatt
 *
 *		An EvoPool contains the tools to operate on both simulations and genetic representations.
 */
public class EvoPool extends EMConfigurableHNode {

	protected VariationManager _vm = null;
	protected Population _pop = null;
	LinkedHashMap<String,EvoPool> _ep = new LinkedHashMap<String,EvoPool>();
	LinkedHashMap<String,SimulationManager> _sm = new LinkedHashMap<String,SimulationManager>();
	RepresentationPrinter _printer = null;
	
	/**
	 * Construct an EvoPool without a parent.
	 * @param name
	 */
	public EvoPool(String name){
		this(name, null);
	}
	
	/**
	 * Construct an EvoPool with a particular parent
	 * @param name
	 * @param parent
	 */
	public EvoPool(String name, HNode parent){
		super(name,parent);
	}
	
	/**
	 * Construct an EvoPool with a particular aprent
	 * @param name
	 * @param parent
	 */
	public EvoPool(String name, EvoPool parent){
		super(name,parent);
	}
	
	/**
	 * Move this EvoPool to another parent
	 * @param ep
	 */
	public void moveTo(EvoPool ep){
		super.moveTo(ep);
		ep.addEvoPool(this);
	}
	
	
	/**
	 * Add a child EvoPool to this object
	 * @param ep
	 */
	public void addEvoPool(EvoPool ep){
		_ep.put(ep.getName(), ep);
		addChild(ep);
	}
	
	/**
	 * Add a simulation manager to this object
	 * @param sm
	 */
	public void setSM(SimulationManager sm){
		String name = sm.getName();
		_sm.put(name,sm);
		addChild(sm);
	}
	
	
	/**
	 * Add a variation manager to this object
	 * @param vm
	 */
	public void setVM(VariationManager vm){
		if (_vm != null)
			removeChild(_vm);
		_vm = vm;
		addChild(_vm);
	}
	
	/**
	 * Get the variation manager associated with this EvoPool
	 * @param vm
	 * @return
	 */
	public VariationManager getVM(VariationManager vm){
		return _vm;
	}
	
	
	public Population getPopulation(){
		return _pop;
	}
	
	/**
	 * Add a population to this EvoPool
	 * @param p
	 */
	public void setPopulation(Population p){
		if (_pop != null)
			removeChild(_pop);
		_pop = p;
	}
	
	/**
	 * Remove the population from this EvoPool (if present).
	 * @param p
	 */
	public void removePopulation(Population p){
		if (p == _pop)
			_pop = null;
		removeChild(_pop);
	}
	
	/**
	 * Get the EvoType printer for this EvoPool
	 * @return
	 */
	public RepresentationPrinter getETPrinter(){
		return _printer;
	}
}
