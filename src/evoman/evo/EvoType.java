package evoman.evo;

public interface EvoType {
	public void inspect(EvoTypeInspector i);
	public String stringify(EvoTypePrinter p);
}
