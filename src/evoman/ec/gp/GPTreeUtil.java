package evoman.ec.gp;


import java.util.*;



public class GPTreeUtil {

	public static int maxDepth(GPNode n) {
		Stack<GPNode> stack = new Stack<GPNode>();
		stack.add(n);
		int max_depth = -1;
		while (!stack.isEmpty()) {
			GPNode cur = stack.pop();
			if (cur.numChildren() == 0) {
				max_depth = (max_depth < cur.getDepth()) ? cur.getDepth() : max_depth;
			} else {
				for (int k = 0; k < cur.numChildren(); k++) {
					stack.add(cur.getChildren()[k]);
				}
			}
		}
		return max_depth;
	}



	public static int maxSubtreeDepth(GPNode n) {
		int max_depth = GPTreeUtil.maxDepth(n);
		return max_depth - n.getDepth() + 1; // count ourselves
	}

}
