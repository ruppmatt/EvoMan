package evoman.tools;

import java.util.*;

/**
 * 
 * @author ruppmatt
 *
 *	DAGraph implements a Directed Acyclic (DA) graph using DANodes.  It is an HNode, so it can plug into other Hierarchical
 *  data structures.
 *
 */
public class DAGraph extends Identifiable{
	
	HashSet<DANode> _nodes = new HashSet<DANode>();
	HashMap<DANode,Integer> _visited = new HashMap<DANode,Integer>();
	LinkedHashMap<DANode,HashSet<DANode>> _egress = new LinkedHashMap<DANode,HashSet<DANode>>();
	public static final Integer UNVISITED = -1;
	
	public DAGraph(String name){
		super(name);
	}
	
	
	public boolean add(DANode n){
		if (!_nodes.contains(n)){
			_nodes.add(n);
			_visited.put(n,false);
			_egress.put(n, new HashSet<DANode>());
			return true;
		} else{
			return false;
		}
	}
	
	
	public boolean remove(DANode n){
		if (_nodes.contains(n)){
			_nodes.remove(n);
			_visited.remove(n);
			for (HashSet<DANode> to : _egress.values()){
				if (to.contains(n))
					to.remove(n);
			}
			n._graph = null;
			return true;
		} else{
			return false;
		}
	}
	
	public boolean connect(DANode from, DANode to){
		if (canConnect(from,to)){
			if (!_egress.containsKey(from)){
				_egress.put(from, new HashSet<DANode>());
			}
			_egress.get(from).add(to);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean disconnect(DANode from, DANode to){
		if (_egress.containsKey(from)){
			if (_egress.get(from).contains(to)){
				_egress.get(from).remove(to);
				return true;
			} else{
				return false;
			}
		} else {
			return false;
		}
	}
	
	public Collection<DANode> getConnectedTo(DANode from){
		return _egress.get(from);
	}
	
	public Collection<DANode> getConnectedFrom(DANode to){
		Collection<DANode> retval = new ArrayList<DANode>();
		for (DANode n : _egress.keySet()){
			if (_egress.get(n).contains(to))
				retval.add(n);
		}
		return retval;
	}
	
	public boolean canConnect(DANode from, DANode to){
		if (_nodes.contains(from) && _nodes.contains(to)){
			resetVisited();
			BFS(to);
			if (_visited.get(from) != UNVISITED){
				return true;
			} else {
				return false;
			}
		} else{
			return false;
		}
	}
	
	public Collection<DANode> getNodes(){
		return _nodes;
	}
	
	protected void resetVisited(){
		for (DANode n : _visited.keySet()){
			_visited.put(n, UNVISITED);
		}
	}
	
	
	
	protected void BFS(DANode start){
		Queue<DANode> bfs = new LinkedList<DANode>();
		bfs.add(start);
		int cur_order = 0;
		while(!bfs.isEmpty()){
			DANode here = bfs.poll();
			_visited.put(here,cur_order++);	
			for (DANode n : _egress.get(here)){
				if (_visited.get(n) == UNVISITED){
					bfs.add(n);
				}
			}
		}
	}
	
}

