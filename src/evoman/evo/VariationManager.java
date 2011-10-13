package evoman.evo;


/**
 * 
 * @author ruppmatt
 *
 *	A variation manager provides mutations and fitness filters to create new populations.
 */
public class VariationManager extends EMConfigurableHNode {

	protected final EvoPool _ep;
	
	public VariationManager(String name, EvoPool parent){
		super(name,parent);
		_ep = parent;
		addDefault("size", 0, "Default size of the population");
	}
	
	public void populate(){
	}
	
}
