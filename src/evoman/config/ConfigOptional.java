package evoman.config;


public @interface ConfigOptional {

	ConfigArgs[] args() default {};
}
