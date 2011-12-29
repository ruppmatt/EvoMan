package evoman.ec.mutation;

import java.util.*;

import evoman.evo.*;
import evoman.evo.pop.*;
import evoman.tools.*;

public interface EvolutionOperator extends Constants {

	public boolean satisfied();
	public void receive(EvolutionOperator from, Population p);
	public void produce();
	public void reset();

}
