package evoman.ec.gp;


public abstract class GPMutableNode extends GPNode {

	public GPMutableNode(GPTree t, GPNodeConfig conf, GPNodePos pos) {
		super(t, conf, pos);
	}



	public abstract void mutate();

}
