package fweinzerl.evosim.sim.gene;

import fweinzerl.evosim.sim.Simulation;

public class SensorPhysicalGenome extends SimplePhysicalGenome{
	int sensorCount;
	
	public SensorPhysicalGenome(Simulation sim, float size, int sensorCount){
		super(sim, size);
		this.sensorCount = sensorCount;
	}
	
	public int getSensorCount(){ return sensorCount; }
}
