package evoman.bindings.ecj;

import ec.*;
import ec.gp.*;
import ec.util.*;

public class EMGPBreedingPipeline extends GPBreedingPipeline {

	private static final long serialVersionUID = 1L;

	@Override
	public Parameter defaultBase() {
		return new Parameter("evoman.bindings.ecj");
	}

	@Override
	public int numSources() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int produce(int min, int max, int start, int subpopulation, Individual[] inds, EvolutionState state,
			int thread) {
		return 0;
	}

}
