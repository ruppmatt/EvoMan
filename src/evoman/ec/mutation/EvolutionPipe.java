package evoman.ec.mutation;


import evoman.evo.pop.*;



public class EvolutionPipe {

	protected EvolutionOpConfig	_to;
	protected EvolutionOpConfig	_from;
	protected EvolutionPipeConfig		_conf;
	protected Population		_p	= null;



	public EvolutionPipe(EvolutionPipeConfig conf) {
		_conf = conf;
	}



	public EvolutionOpConfig getSender() {
		return _conf._from;
	}



	public EvolutionOpConfig getReceiver() {
		return _conf._to;
	}



	public void send(Population p) {
		_p = p;
	}



	public Population receive() {
		Population retval = _p;
		_p = null;
		return retval;
	}



	public boolean empty() {
		return _p == null;
	}



	public EvolutionPipeConfig getConfig() {
		return _conf;
	}
}
