package evoman.evo.pop;


import evoman.evo.structs.*;



public interface PopulationCompositer {

	public int numSources();



	/**
	 * Add a population to a composite population. This method will remove
	 * (nullify) the reference to the population in the origin evopool.
	 * 
	 * @param origin
	 *            EvoPool origin
	 */
	public void addSubPopulation(EvoPool origin);



	public void setSubPopulation(EvoPool origin, Population p);



	public void addSubGenotype(Genotype g, EvoPool origin);



	public void removeSubGenotype(Genotype g, EvoPool origin);



	public EvoPool getSource(Genotype g);



	public EvoPool[] getSources();



	public Population getSubPopulation(EvoPool ep);

}
