package evoman.ec.mutation;


import java.lang.annotation.*;



/**
 * All EvolutionOperators should be annotated with an EvolutionDescriptor.
 * 
 * EvolutionPipelines validate the connectivity between EvolutionOperators using
 * this descritptor, which is accessible from an EvolutionOpConfig's
 * getConstraints()
 * 
 * @author ruppmatt
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EvolutionDescriptor {

	/**
	 * The name of the operator class, not to be confused with the name of the
	 * EvolutionOperator's config.
	 * 
	 * @return
	 */
	String name();



	/**
	 * Does the operator use selection?
	 * 
	 * @return
	 */
	boolean selection() default false;



	/**
	 * What type of representation does this mutation operator require?
	 * 
	 * @return
	 */
	Class<?> reptype() default Object.class;



	/**
	 * Minimum number of inputs
	 * 
	 * @return
	 */
	int min_in() default 1;



	/**
	 * Maxmimum number of inputs
	 * 
	 * @return
	 */
	int max_in() default 1;

}
