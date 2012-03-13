package evoman.config;


public @interface ConfigProxy {

	Class<?> proxy_for();



	boolean as_object() default true;
}
