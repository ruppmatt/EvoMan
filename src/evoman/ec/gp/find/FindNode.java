package evoman.ec.gp.find;


import java.util.*;

import evoman.ec.gp.*;



/**
 * FindNode objects collect nodes in GPTrees fitting specific criteria at
 * run-time. For example, they are used during mutation to find leaves or nodes
 * with a particular return-type.
 * 
 * FindNode objects are passed to a GPTree's bfs or dfs method to perform the
 * search and gather.
 * 
 * @author ruppmatt
 * 
 */
public interface FindNode {

	/**
	 * Examines a node. Classes implementing FindNode place node-selection
	 * criteria here.
	 * 
	 * @param n
	 */
	public void examine(GPNode n);



	/**
	 * Return a list of GPNodes that meet the criteria of the FindNode class
	 * 
	 * @return
	 */
	public ArrayList<GPNode> collect();



	/**
	 * Tests whther the FindNode object is finished with its task
	 * 
	 * @return
	 *         True if there is nothing left to find
	 * 
	 */
	public boolean done();
}
