package evoman.ec.gp;


public abstract class GPMutableNode extends GPNode {

	public GPMutableNode(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	public abstract void mutate();

}
