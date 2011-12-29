package evoman.bindings.simternet;

import evoman.evo.*;

public interface EMObservee {
	
	public enum SimEvent{
		UNKNOWN, FINISHED
	};
	
	public void registerEM(EvoMan em);
	public void notifyEM(SimEvent e, Object me);
}
