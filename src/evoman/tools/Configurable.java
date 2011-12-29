package evoman.tools;

public interface Configurable {

	/**
	 * Add a default parameter to this Configurable
	 * @param name
	 * @param value
	 * @param descr
	 */
	public void addDefault(String name, Object value, String descr);
	
	/**
	 * Get a Parameter with a particular name (null if not set)
	 * @param name
	 * @return
	 */
	public Object get(String name);
	
	/**
	 * Set a key/value Parameter
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value);
	
	/**
	 * Unset (remove) a parameter if it is present
	 * @param name
	 */
	public void unset(String name);
	
	/**
	 * Returns true if a parameter with a particular name is set
	 * @param name
	 * @return
	 */
	public Boolean isSet(String name);
	
	/**
	 * Helper function to return a Parameter with a particular name to an Integer (or null if not present)
	 * @param name
	 * @return
	 */
	public Integer I(String name);
	
	/**
	 * Helper function to return a Parameter with a particular name to an Double (or null if not present)
	 * @param name
	 * @return
	 */
	public Double D(String name);
	
	/**
	 * Helper function to return a Parameter with a particular name to an String (or null if not present)
	 * @param name
	 * @return
	 */
	public String S(String name);
	
	/**
	 * Returns true if the Paramter (if present) has a default value.
	 * @param name
	 * @return
	 */
	public Boolean isDefault(String name);
	
}
