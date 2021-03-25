package fweinzerl.evosim.gene;

import fweinzerl.evosim.gene.Mutatable;
import fweinzerl.evosim.sim.Simulation;

public abstract class Genome implements Mutatable{
	protected Simulation sim;
	
	public Genome(Simulation sim){
		this.sim = sim;
	}
}
