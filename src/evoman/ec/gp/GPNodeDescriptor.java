package evoman.ec.gp;


import java.lang.annotation.*;



/**
 * Used to annotate GPNode classes to provide easy to find information about the
 * configuration name, return type, and child types of the node. Nodes that have
 * a dynamic child or return type (such as actions) can have these values
 * altered at validation time.
 * 
 * @author ruppmatt
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GPNodeDescriptor {

	String name(); // Configuration name



	Class<?> return_type(); // Return type of the node (can be changed at
							// run-time)



	Class<?>[] child_types(); // Return type of the children (can be changed at
								// run-time)

}
