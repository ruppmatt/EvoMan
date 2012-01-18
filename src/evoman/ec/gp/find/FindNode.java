package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



public interface FindNode {

	public void examine(GPNode n);



	public ArrayList<GPNode> collect();



	public boolean done();
}
