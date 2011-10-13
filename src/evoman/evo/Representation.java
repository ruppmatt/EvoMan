package evoman.evo;

public interface Representation {
	public void inspect(EvoTypeInspector i);
	public String stringify(RepresentationPrinter p);
}
