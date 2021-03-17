package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.sim.Simulation;

public class AdvancedWholeGenome extends WholeGenome{
	public AdvancedWholeGenome(Simulation s, SensorPhysicalGenome sepg, NeuroGenome ng){
		super(s, sepg, ng);
	}
	
	@Override
	public SensorPhysicalGenome getPhysicalGenome(){ return (SensorPhysicalGenome) pg; }
	
	@Override
	public AdvancedWholeGenome mutate(float rate){
		return new AdvancedWholeGenome(sim, (SensorPhysicalGenome)pg.mutate(rate), ng.mutate(rate));
	}
}
