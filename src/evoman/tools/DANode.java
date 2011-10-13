package evoman.tools;

import java.util.*;

public class DANode extends Identifiable {
	
	DAGraph _graph = null;
	
	public DANode(String name, DAGraph graph){
		super(name);
		_graph = graph;
		_graph.add(this);
	}
	
	public Collection<DANode> connectedTo(){
		if (_graph != null){
			return _graph.getConnectedTo(this);
		} else {
			return null;
		}
	}
	
	public Collection<DANode> connectedFrom(){
		if (_graph != null){
			return _graph.getConnectedFrom();
		} else {
			return false;
		}
	}
	
	public boolean connectTo(DANode to){
		if (_graph != null && to.getGraph() == _graph){
			return _graph.connect(this, to);
		} else
			return false;
	}
	
	public boolean disconnectTo(DANode to){
		if (_graph != null && to.getGraph() == _graph){
			return _graph.disconnect(this, to);
		} else
			return false;
	}
	
	
	public boolean connectFrom(DANode from){
		if (_graph != null && from.getGraph() == _graph){
			return _graph.connect(from, this);
		} else
			return false;
	}
	
	public boolean disconnectFrom(DANode from){
		if (_graph != null && from.getGraph() == _graph){
			return _graph.disconnect(from, this);
		} else 
			return false;
	}

	public DAGraph getGraph(){
		return _graph;
	}
}
