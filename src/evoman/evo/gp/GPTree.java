package evoman.evo.gp;

import evoman.evo.pop.*;
import evoman.evo.structs.*;
import evoman.tools.*;


public class GPTree implements Representation, EMState{
	
	protected GPTreeConstraints _tc;
	protected GPTreeFunctions   _fs;
	protected MethodHNode       _md;
	protected EMState           _parent;
	
	public GPTree(EMState parent, GPTreeInitializer ti, GPTreeConstraints tc, GPTreeFunctions fs, MethodDictionary md){
		_tc = tc;
		_fs = fs;
	}
	
	
	@Override
	public Object evaluate(Object o) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MethodHNode getDictionary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDictionary() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public EMState getESParent() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public MersenneTwisterFast getRandom() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public EMThreader getThreader() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Notifier getNotifier() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
