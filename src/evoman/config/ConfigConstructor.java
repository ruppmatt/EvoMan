package evoman.config;


public @interface ConfigConstructor {

	ConfigArgs[] args() default {};
}
