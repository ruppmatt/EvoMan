package evoman.ec.gp.init;


import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
public @interface GPInitDescriptor {

	String name();
}
