package evoman.evo.structs;

import evoict.graphs.*;
import evoict.io.*;
import evoman.tools.*;

public class EMConfigurableDANode extends ConfigurableDANode implements EMState {

	protected EMState _emparent = null;
	protected MersenneTwisterFast _rand = null;
	protected Notifier _notifier = null;
	protected EMThreader _threader = null;
	protected boolean _initialized = false;
	protected boolean _finished = false;
	
	public EMConfigurableDANode(String name, DAGraph graph) {
		super(name, graph);
	}

	@Override
	public EMState getESParent() {
		return _emparent;
	}


	@Override
	public void init() {
		if (_initialized == true)
			return;
		for (DANode next : connectedTo()){
			if (next instanceof EMState){
				((EMState) next).init();
			}
		}
	}

	@Override
	public void finish() {
		if (_finished == true)
			return;
		for (DANode next : connectedTo()){
			if (next instanceof EMState){
				((EMState) next).finish();
			}
		}
	}

	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_emparent != null)
				_emparent.getRandom();
			else{
				if (_kv.isSet("seed"))
					_rand = new MersenneTwisterFast(_kv.I("seed"));
				else
					_rand = new MersenneTwisterFast();
			}
		}
		return _rand;
	}


	@Override
	public EMThreader getThreader() {
		if ( _threader == null ){
			if (_emparent != null)
				_emparent.getThreader();
			else{
				if (_kv.isSet("max_threads"))
					_threader = new EMThreader(this,I("max_threads"));
				else
					_threader = new EMThreader(this,1);
			}
		}
		return _threader;
	}



	@Override
	public Notifier getNotifier() {
		if ( _notifier == null ){
			if (_emparent != null)
				_emparent.getThreader();
			else{
				_notifier = new Notifier();
			}
		}
		return _notifier;
	}
	

}
