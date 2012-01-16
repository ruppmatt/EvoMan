package evoman.interpreter;


import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigDescriptor {

	String name();
}
