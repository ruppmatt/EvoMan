package evoman.config;


import java.util.*;



public class BlockFrame {

	protected BlockFrame	_parent;
	ArrayList<String>		_text		= new ArrayList<String>();
	ArrayList<BlockFrame>	_children	= new ArrayList<BlockFrame>();



	public BlockFrame() {
		_parent = null;
	}



	public BlockFrame(BlockFrame parent) {
		_parent = parent;
	}



	public BlockFrame parent() {
		return _parent;
	}



	public void addChild(BlockFrame bl) {
		_children.add(bl);
	}



	public ArrayList<BlockFrame> children() {
		return _children;
	}



	public void addText(String text) {
		String no_leading_ws = text.replaceAll("^\\s+", "");
		_text.add(no_leading_ws);
	}



	public ArrayList<String> text() {
		return _text;
	}

}
