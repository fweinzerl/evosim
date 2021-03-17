package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.sim.Simulation;

public class WholeGenome extends Genome{
	protected PhysicalGenome pg;
	protected NeuroGenome ng;
	
	public WholeGenome(Simulation s, PhysicalGenome physGene, NeuroGenome neurGene){
		super(s);
		pg = physGene;
		ng = neurGene;
	}
	
	public NeuroGenome getNeuroGenome(){ return ng; }
	public PhysicalGenome getPhysicalGenome(){ return pg; }
	
	@Override
	public WholeGenome mutate(float rate){
		return new WholeGenome(sim, pg.mutate(rate), ng.mutate(rate));
	}
}
