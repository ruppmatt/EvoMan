package evoman.interpreter;


import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigRequired {

	String[] names() default {};
}
