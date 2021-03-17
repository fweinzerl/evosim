package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.sim.Simulation;

public abstract class Genome{
	protected Simulation sim;
	
	public Genome(Simulation sim){
		this.sim = sim;
	}
	
	public abstract Genome mutate(float rate);
}
