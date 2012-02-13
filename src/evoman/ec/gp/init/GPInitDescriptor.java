package evoman.ec.gp.init;


import java.lang.annotation.*;



/**
 * The GPInitDescriptor provides run-time information about the names of
 * different GPTree initializers.
 * 
 * @author ruppmatt
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GPInitDescriptor {

	String name();
}
