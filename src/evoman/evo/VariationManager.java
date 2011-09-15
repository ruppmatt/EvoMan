package evoman.evo;


public class VariationManager extends EMConfigurable {

	protected final EvoPool _ep;
	
	public VariationManager(String name, EvoPool parent){
		super(name,parent);
		_ep = parent;
		addDefault("size", 0, "Default size of the population");
	}
	
	public void populate(){
	}
	
}
