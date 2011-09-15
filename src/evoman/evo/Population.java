package evoman.evo;

import java.util.Collection;
import java.util.LinkedHashMap;
import evoman.tools.Printable;


public class Population extends EMHierarchical implements Cloneable, Printable {

	protected LinkedHashMap<String,Genotype> _genotypes =
			new LinkedHashMap<String,Genotype>();
	protected EvoPool _ep;
	
	
	public Population(String name, EvoPool ep){
		super(name, ep);
		_ep = ep;
	}
	
	public void addGenotype(Genotype g){
		_genotypes.put(g.getName(), g);
		addChild(g);
	}
	
	public void removeGenotype(Genotype g){
		String name = g.getName();
		if (_genotypes.containsKey(name))
			_genotypes.remove(name);
		removeChild(g);
	}
	
	public Genotype getGenotype(String name){
		return (_genotypes.containsKey(name)) ? _genotypes.get(name) : null;
	}
	
	public int size(){
		return _genotypes.size();
	}
	
	public EvoTypePrinter getETPrinter(){
		return (_ep != null) ? _ep.getETPrinter() : null;
	}
	
	public void moveTo(EvoPool ep){
		if (_ep != null)
			_ep.removePopulation(this);
		_ep = ep;
		_ep.addPopulation(this);
	}
	
	public Population clone(){
		Population cl = new Population(_name, null);
		for (String g : _genotypes.keySet())
			cl._genotypes.put(g, _genotypes.get(g));
		return cl;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ " + getFullName() + " ]" + ENDL);
		Collection<Genotype> g = _genotypes.values();
		if (g.size() > 0)
			sb.append(g.toString());
		for (int k=1; k < _genotypes.size(); k++)
			sb.append(ENDL + g.toString());
		return sb.toString();
	}
	

}
