package evoman.ec.gp;


import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
public @interface GPNodeDescriptor {

	String name();



	Class<?> return_type();



	Class<?>[] child_types() default {};



	boolean mutable() default false;
}
