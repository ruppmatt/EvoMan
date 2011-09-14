package evoman.evo;

import evoman.tools.Printable;

public class Genotype extends EMHierarchical implements Cloneable, Printable{
	
	protected Population _pop;
	protected final double _fitness = 0.0;
	protected Boolean _evaluated = false;
	protected final EvoType _rep;
	
	
	public Genotype(String name, Population p, EvoType rep){
		super(name, p);
		_pop = p;
		_rep = rep;
	}
	
	public void moveTo(Population p){
		_pop.removeGenotype(this);
		_pop = p;
		_pop.addGenotype(this);
	}
	
	double getFitness(){
		return _fitness;
	}
	
	Boolean evaluated(){
		return _evaluated;
	}
	
	EvoType rep(){
		return _rep;
	}
	
	public Object clone(){
		return new Genotype(_name, null, _rep);
	}
	
	public String toString(){
		return getFullName() + "< " + _rep.stringify(_pop.getETPrinter()) + " >";
	}
}
