package evoman.evo;

public interface Representation {
	public Object evaluate(Object o);
	public String stringify(RepresentationPrinter p);
}
