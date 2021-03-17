package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.sim.Simulation;

public class SimpleWholeGenome extends WholeGenome{
	public SimpleWholeGenome(Simulation s, SimplePhysicalGenome spg, NeuroGenome ng){
		super(s, spg, ng);
	}
	
	@Override
	public SimplePhysicalGenome getPhysicalGenome(){ return (SimplePhysicalGenome) pg; }
	
	@Override
	public SimpleWholeGenome mutate(float rate){
		return new SimpleWholeGenome(sim, (SimplePhysicalGenome)pg.mutate(rate), ng.mutate(rate));
	}
}
