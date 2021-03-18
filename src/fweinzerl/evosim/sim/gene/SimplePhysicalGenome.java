package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.sim.Simulation;
import fweinzerl.evosim.util.Util;

public class SimplePhysicalGenome extends PhysicalGenome{
	private float size;
	
	public SimplePhysicalGenome(Simulation sim, float size){
		super(sim);
		this.size = size;
	}
	
	public float getSize(){ return size; }
	
	@Override
	public SimplePhysicalGenome mutate(float rate){
		return new SimplePhysicalGenome(sim, (float) Util.addSigmoid(size, (sim.rand.nextFloat()-0.5)*rate*(sim.maxSize-sim.minSize), sim.minSize, sim.maxSize));
	}
}
