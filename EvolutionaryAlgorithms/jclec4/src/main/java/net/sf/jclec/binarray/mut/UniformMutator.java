package net.sf.jclec.binarray.mut;

import net.sf.jclec.IConfigure;

import net.sf.jclec.binarray.BinArrayMutator;
import net.sf.jclec.binarray.BinArrayIndividual;

import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.configuration.Configuration;

/**
 * Uniform mutator for BinArrayIndividual (and subclasses).
 * 
 * @author Sebastian Ventura 
 */

public class UniformMutator extends BinArrayMutator implements IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 1709293115607625737L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Crossover probability */
	
	private double locusMutationProb;
		
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public UniformMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties

	/**
	 * @return Returns the crossprob.
	 */
	
	public final double getLocusMutationProb() 
	{
		return locusMutationProb;
	}
	
	/**
	 * @param mutProb New locus mutation probability
	 */
	
	public final void setLocusMutationProb(double mutProb) 
	{
		this.locusMutationProb = mutProb;
	}
	
	// IConfigure interface

	/**
	 * Configuration method.
	 * 
	 * Configuration parameters for UniformMutator are:
	 * 
	 * <ul>
	 * <li>
	 * <code>[@evaluate]: boolean (default = true)</code></p> 
	 * If this parameter is set to <code>true</true> individuals will
	 * be evaluated after its creation. 
	 * </li>
	 * <li>
	 * <code>[@locus-mutation-prob]: double (default = 0.5)</code></p>
	 * Locus mutation probability.  
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration configuration) 
	{
		// Get the 'locus-mutation-prob' property
		double locusMutationProb = configuration.getDouble("[@locus-mutation-prob]", 0.5);
		setLocusMutationProb(locusMutationProb);
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		if (other instanceof UniformMutator) {
			// Type conversion
			UniformMutator cother = (UniformMutator) other;
			// Equals Builder
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(locusMutationProb, cother.locusMutationProb);
			// Returns 
			return eb.isEquals();
		}
		else {
			return false;
		}
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	/**
	 *  {@inheritDoc}
	 */
	
	@Override
	protected void mutateNext() 
	{
		// Genome length
		int gl = species.getGenotypeLength();
		// Individual to be mutated
		BinArrayIndividual mutant = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter);
		// Creates mutant genotype
		byte [] mgenome = new byte[gl];
		System.arraycopy(mutant.getGenotype(), 0, mgenome, 0, gl);
		// Mutate loci...
		for (int i=0; i<gl; i++) {
			if (randgen.coin(locusMutationProb)) flip(mgenome, i);
		}
		// Returns mutant
		sonsBuffer.add(species.createIndividual(mgenome));
	}
	
	/*
	 * El mtodo realiza las siguientes acciones:
	 * 
	 * 1) Crea el genotipo del individuo, copiando el del padre
	 * 2) Para cada uno de los loci del individuo, generamos un valor
	 *    aleatorio entre 0 y 1. Si el valor no excede locusMutationProb,
	 *    el locus es mutado. Si no, persiste el valor del padre.
	 */	
}