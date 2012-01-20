package evoman.ec.gp;


public abstract class GPMutableNode extends GPNode {

	private static final long	serialVersionUID	= 1L;



	public GPMutableNode(GPTree t, GPNodeConfig conf, GPNode parent, GPNodePos pos) {
		super(t, conf, parent, pos);
	}



	public abstract void mutate();

}
