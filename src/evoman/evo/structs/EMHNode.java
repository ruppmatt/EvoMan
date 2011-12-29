package evoman.evo.structs;

import evoict.graphs.*;
import evoict.io.*;
import evoman.tools.*;

/**
 * 
 * @author ruppmatt
 *
 *		An EMHierarchical object contains shared-state information.
 */
public class EMHNode extends HNode implements EMState {

	EMState _emparent = null;
	MersenneTwisterFast _rand = null;
	Notifier _notifier = null;
	EMThreader _threader = null;
	int _running_threads = 0;
	
	
	/**
	 * Construct a parentless EMHierarchical
	 * @param name
	 */
	public EMHNode(String name){
		this(name, null);
	}
	
	/**
	 * Construct an EMHierarchical with a particular parent
	 * @param name
	 * @param parent
	 */
	public EMHNode(String name, HNode parent){
		super(name, parent);
		if (parent instanceof EMState)
			_emparent = (EMState) parent;
	}


	@Override
	public EMState getESParent() {
		return _emparent;
	}


	@Override
	public void init() {
		for (HNode child : getChildren()){
			if (child instanceof EMState){
				((EMState) child).init();
			}
		}
	}

	@Override
	public void finish() {
		for (HNode child : getChildren()){
			if (child instanceof EMState){
				((EMState) child).finish();
			}
		}
	}

	@Override
	public MersenneTwisterFast getRandom() {
		if ( _rand == null ){
			if (_emparent != null)
				_emparent.getRandom();
			else{
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
