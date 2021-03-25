package fweinzerl.evosim.gene;

import fweinzerl.evosim.sim.Simulation;

public abstract class PhysicalGenome extends Genome{
	public PhysicalGenome(Simulation sim){
		super(sim);
	}
	
	@Override
	public abstract PhysicalGenome mutate(double rate);
}
