package evoman.evo;


public class VariationManager extends EMConfigurable {

	protected final EvoPool _ep;
	
	public VariationManager(String name, EvoPool parent){
		super(name,parent);
		_ep = parent;
	}
	
	public void populate(){
	}
	
}
