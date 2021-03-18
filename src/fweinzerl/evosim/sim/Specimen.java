package fweinzerl.evosim.sim;

import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.sim.gene.Genome;
import fweinzerl.evosim.sim.gene.WholeGenome;

public abstract class Specimen extends SimulationObject{
	protected float saturation;
	
	//genetic parameters
	protected WholeGenome g;
	
	protected Brain b;
	
	public Specimen(float x, float y, float size, float initSaturation, Brain b){
		super(x, y, size);
		saturation = initSaturation;
		this.b = b;
	}
	
	public Specimen(float x, float y, float initSaturation, WholeGenome genome){
		super(x, y, 0);
		saturation = initSaturation;
		g = genome;
	}
	
	@Override
	public void update(float dt){
		//eat if there is something to eat
		for(int i = 0; i < sim.getListOfFood().size(); i++){
			Food f = sim.getListOfFood().get(i);
			float dx = x-f.x, dy = y-f.y;
			if(Math.sqrt(dx*dx+dy*dy) < spOfInfl+f.spOfInfl){
				saturation += f.getNutritionalVal();
				sim.getListOfFood().remove(f);
			}
		}
		
		//let brain get to work
		perceive();
		b.process();
		//act
		float dx = calcMoveDistX();
		float dy = calcMoveDistY();
		addToCoordinates(dx, dy);
		float totalDistance = sim.speedFactor * (float)Math.sqrt(dx*dx+dy*dy) * dt;
		
		//burn calories
		saturation -= sim.livingCostsPerTick*dt + totalDistance*sim.movingCostsPerUnit
				+ sim.sizeCostsPerUnit*spOfInfl*totalDistance + sim.brainCostPerConn*b.getEnabledConnections()*dt;
		
		//die if starved / perform mitosis if saturated enough
		if(saturation < 0)
			sim.getListOfSpecimen().remove(this);
		else if(saturation >= sim.saturationLimit){
			saturation /= 2;
			sim.addSpecimen(mutate());
		}
	}
	
	protected abstract void perceive();
	protected abstract float calcMoveDistX();
	protected abstract float calcMoveDistY();
	
	protected abstract Specimen mutate();
}
