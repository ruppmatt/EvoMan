package evoman.config;


public @interface ConfigRequire {

	ConfigArgs[] args() default {};
}
