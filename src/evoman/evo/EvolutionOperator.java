package evoman.evo;

import java.util.*;

import evoman.tools.*;

public interface EvolutionOperator extends Constants {

	public boolean satisfied();
	public void receive(EvolutionOperator from, Population p);
	public abstract void produce();
	public void reset();

}
