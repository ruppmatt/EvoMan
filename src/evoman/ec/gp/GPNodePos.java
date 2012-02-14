package evoman.ec.gp;


import java.io.*;

import evoman.evo.*;



/**
 * GPNodePos gives information about where a node is located in the tree by
 * listing the order of its ascendents.
 * 
 * @author ruppmatt
 * 
 */
public class GPNodePos implements Serializable, Cloneable {

	private static final long	serialVersionUID	= 1L;
	protected byte[]			_pos;



	/**
	 * Create an empty position.
	 */
	public GPNodePos() {
		_pos = null;
	}



	/**
	 * Return the depth indicated by the position.
	 * 
	 * @return
	 */
	public int getDepth() {
		return (_pos == null) ? 1 : _pos.length + 1;
	}



	/**
	 * Return the position of its parent
	 * 
	 * @return
	 */
	public int getLastPos() {
		if (_pos.length < 2) {
			return Constants.UNDEFINED;
		} else {
			return _pos[_pos.length - 2];
		}
	}



	/**
	 * Create a new position
	 * 
	 * @param pos
	 *            The location of this new position in relation to other
	 *            children of the same node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public GPNodePos newPos(byte pos) {
		GPNodePos ret = new GPNodePos();
		int new_length = (_pos == null) ? 1 : _pos.length + 1;
		ret._pos = new byte[new_length];
		ret._pos[new_length - 1] = pos;
		return ret;
	}



	/**
	 * Return the position of the node
	 * 
	 * @return
	 */
	public byte[] getPos() {
		return _pos;
	}



	@Override
	@SuppressWarnings("unchecked")
	/**
	 * Clone the position information
	 */
	public Object clone() {
		GPNodePos cl = new GPNodePos();
		if (_pos == null) {
			return cl;
		} else {
			cl._pos = new byte[_pos.length];
			for (int k = 0; k < _pos.length; k++)
				cl._pos[k] = _pos[k];
			return cl;
		}
	}
}
