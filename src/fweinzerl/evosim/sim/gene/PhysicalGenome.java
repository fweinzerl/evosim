package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.sim.Simulation;

public abstract class PhysicalGenome extends Genome{
	public PhysicalGenome(Simulation sim){
		super(sim);
	}
	
	@Override
	public abstract PhysicalGenome mutate(float rate);
}
