package evoman.ec.mutation;

import evoman.evo.*;
import evoman.evo.pop.*;

public interface EvolutionOperator extends Constants {

	public boolean satisfied();
	public void receive(EvolutionOperator from, Population p);
	public void produce();
	public void reset();

}
