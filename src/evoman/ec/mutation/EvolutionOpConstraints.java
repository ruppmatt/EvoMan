package evoman.ec.mutation;


import java.io.*;



/**
 * EvolutionOpConstraints are generated from an EvolutionOperator's
 * EvolutionDescriptor annotation.
 * 
 * @author ruppmatt
 * 
 */
public class EvolutionOpConstraints implements Serializable {

	private static final long	serialVersionUID	= 1L;



	public static EvolutionOpConstraints scan(Class<? extends EvolutionOperator> o) {
		EvolutionDescriptor descr = o.getAnnotation(EvolutionDescriptor.class);
		if (descr != null) {
			return new EvolutionOpConstraints(descr);
		} else {
			return null;
		}
	}

	protected int						_in_min;
	protected int						_in_max;
	protected boolean					_selection;
	protected Class<? extends Object>	_rep_type;



	public EvolutionOpConstraints(EvolutionDescriptor desc) {
		_in_min = desc.min_in();
		_in_max = desc.max_in();
		_selection = desc.selection();
		_rep_type = desc.reptype();
	}



	public int inMax() {
		return _in_max;
	}



	public int inMin() {
		return _in_min;
	}



	public boolean usesSelection() {
		return _selection;
	}



	public Class<? extends Object> repType() {
		return _rep_type;
	}
}
