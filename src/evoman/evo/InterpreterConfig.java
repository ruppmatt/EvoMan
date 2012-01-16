package evoman.evo;


import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
public @interface InterpreterConfig {

	String name();



	Class<?>[] parents();



	Class<?>[] required_children();
}
