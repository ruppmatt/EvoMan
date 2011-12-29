package evoman.bindings.ecj;

import java.util.*;

public class GPNodeConstraint {
	
	protected String _returns = null;
	protected ArrayList<String> _children = new ArrayList<String>();
	
	public GPNodeConstraint(String returns){
		_returns = returns;
	}
	
	public GPNodeConstraint(String returns, String ...children){
		_returns = returns;
		for (String child : children){
			_children.add(child);
		}
	}
	
	public GPNodeConstraint(GPOperator op){
		_returns = op.S("return_type");
		for (String child : op.getChildTypes()){
			_children.add(child);
		}
	}
	

	
	public String getReturns(){
		return _returns;
	}
	
	public ArrayList<String> getChildTypes(){
		return _children;
	}
	
	public int getNumChildren(){
		return _children.size();
	}
	
	public boolean equals(Object o){
		if (this == o){
			return true;
		}
		if (o instanceof GPNodeConstraint){
			GPNodeConstraint other = (GPNodeConstraint) o;
			boolean equal =  _returns == other._returns;
			equal = equal && _children.size() == other._children.size();
			if (equal == true){
				for (int k=0; k < _children.size(); k++){
					equal = equal && _children.get(k) == other._children.get(k);
				}
			}
			return equal;
			
		} else {
			return false;
		}
	}
	
	public int hashCode(){
		final int multiplier = 29;
		int code = 101;
		code *= multiplier + _returns.hashCode();
		for (String child : _children){
			code *= multiplier + child.hashCode();
		}
		return code;
	}
	
	

}
