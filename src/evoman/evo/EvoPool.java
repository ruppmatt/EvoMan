package evoman.evo;

import java.util.LinkedHashMap;

import evoman.tools.Hierarchical;



public class EvoPool extends EMConfigurable {

	protected VariationManager _vm = null;
	protected Population _pop = null;
	LinkedHashMap<String,EvoPool> _ep = new LinkedHashMap<String,EvoPool>();
	EvoTypePrinter _printer = null;
	
	public EvoPool(String name){
		this(name, null);
	}
	
	public EvoPool(String name, Hierarchical parent){
		super(name,parent);
	}
	
	public void addEvoPool(String name, EvoPool ep){
		_ep.put(ep.getName(), ep);
		addChild(ep);
	}
	
	
	public void addVM(VariationManager vm){
		_vm = vm;
		addChild(vm);
	}
	
	public VariationManager getVM(VariationManager vm){
		return _vm;
	}
	
	public void addPopulation(Population p){
		if (_pop != null)
			removeChild(_pop);
		_pop = p;
	}
	
	public void removePopulation(Population p){
		if (p == _pop)
			_pop = null;
		removeChild(_pop);
	}
	
	public EvoTypePrinter getETPrinter(){
		return _printer;
	}
}
