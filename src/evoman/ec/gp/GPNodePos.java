package evoman.ec.gp;


import java.util.*;

import evoman.evo.*;



public class GPNodePos {

	protected ArrayList<Integer>	_pos;



	public GPNodePos() {
		_pos = new ArrayList<Integer>();
	}



	public int getDepth() {
		return _pos.size();
	}



	public int getLastPos() {
		if (_pos.size() == 0) {
			return Constants.UNDEFINED;
		} else {
			return _pos.get(_pos.size() - 1);
		}
	}



	@SuppressWarnings("unchecked")
	public GPNodePos newPos(int pos) {
		GPNodePos ret = new GPNodePos();
		ret._pos = (ArrayList<Integer>) _pos.clone();
		ret._pos.add(pos);
		return ret;
	}



	public ArrayList<Integer> getPos() {
		return _pos;
	}
}
