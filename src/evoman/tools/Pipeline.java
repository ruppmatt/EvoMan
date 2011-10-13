package evoman.tools;

import java.util.*;

public class Pipeline extends DAGraph {

	HashSet<DANode> _start = new HashSet<DANode>();
	HashSet<DANode> _term  = new HashSet<DANode>();
	int _required_starts    = 0;
	int _required_terminals = 0;
	public static final int UNCONSTRAINED = -1;
	
	
	public Pipeline(String name){
		this(name, UNCONSTRAINED, UNCONSTRAINED);
	}
	
	
	public Pipeline(String name, int starts, int terminals){
		super(name);
		_required_starts = starts;
		_required_terminals = terminals;
	}
	
	
	public boolean addStart(DANode n){
		if (add(n) == true){
			_start.add(n);
			return true;
		} else{
			return false;
		}
	}
	
	
	public boolean addTerminal(DANode n){
		if (add(n) == true){
			_term.add(n);
			return true;
		} else{
			return false;
		}
	}
	
	
	public boolean setAsStart(DANode n){
		if (_nodes.contains(n)){
			_start.add(n);
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean setAsTerminal(DANode n){
		if (_nodes.contains(n)){
			_term.add(n);
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean unsetStart(DANode n){
		if (_start.contains(n)){
			_start.remove(n);
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean unsetTerminal(DANode n){
		if (_term.contains(n)){
			_term.remove(n);
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean remove(DANode n){
		if (super.remove(n) == true){
			if (_start.contains(n)){
				_start.remove(n);
			}
			if (_term.contains(n)){
				_term.remove(n);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public Collection<DANode> getStart(){
		return _start;
	}
	
	public Collection<DANode> getTerm(){
		return _term;
	}
	
	public Collection<DANode> getBFSOrder(){
		resetVisited();
		Queue<DANode> bfs = new LinkedList<DANode>();
		for (DANode start : _start){
			bfs.add(start);
		}
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
		ArrayList<DANode> retval = new ArrayList<DANode>(_nodes);
		HashMapComparator<DANode> comp = new HashMapComparator<DANode>(_visited);
		Collections.sort(retval, comp);
		return retval;
	}
	
	
	public boolean validate(){
		if ( (_required_starts != UNCONSTRAINED && _start.size() != _required_starts) ||
			 (_required_terminals != UNCONSTRAINED && _term.size()  != _required_terminals) )
			return false;
		if (!checkStartReachable())
			return false;
		if (!checkTermReachable())
			return false;
		//Check if every node has a path to a terminal node
		return true;
	}
	
	
	protected boolean checkStartReachable(){
		resetVisited();
		for (DANode s : _start){
			BFS(s);
		}
		for (Integer i : _visited.values()){
			if (i == UNVISITED)
				return false;
		}
		return true;
	}
	
	
	protected boolean checkTermReachable(){
		for (DANode n : _nodes){
			if (_term.contains(n))
				continue;
			resetVisited();
			BFS(n);
			Boolean hasTerm = false;
			for (DANode t : _term){
				hasTerm = hasTerm || (_visited.get(t) != UNVISITED);
			}
			if (hasTerm == false)
				return false;
		}
		return true;
	}
}

class DictionaryValueCompare implements Comparator<DANode>{

	
	
}
