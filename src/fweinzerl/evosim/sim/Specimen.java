package fweinzerl.evosim.sim;

import fweinzerl.evosim.gene.Mutatable;
import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.phys.Physiology;

public abstract class Specimen extends SimulationObject implements Mutatable{
	protected float saturation;
	
	protected Brain brain;
	protected Physiology physiology;
	
	public Specimen(float x, float y, float radius, float initSaturation, Brain brain){
		super(x, y, radius);
		saturation = initSaturation;
		this.brain = brain;
	}
	
	public Specimen(float x, float y, float initSaturation,
					Brain brain, Physiology physiology){
		super(x, y, physiology.getApproximateRadius());
		saturation = initSaturation;
		this.brain = brain;
		this.physiology = physiology;
	}
	
	@Override
	public void update(float dt){
		//eat if there is something to eat
		for(int i = 0; i < sim.getListOfFood().size(); i++){
			Food f = sim.getListOfFood().get(i);
			float dx = x-f.x, dy = y-f.y;
			if(Math.sqrt(dx*dx+dy*dy) < radius+f.radius){
				saturation += f.getNutritionalVal();
				sim.getListOfFood().remove(f);
			}
		}
		
		//let brain get to work
		perceive();
		this.brain.process();
		
		//act
		float dx = calcMoveDistX();
		float dy = calcMoveDistY();
		this.x += dx;
		this.y += dy;
		float totalDistance = sim.speedFactor * (float)Math.sqrt(dx*dx+dy*dy) * dt;
		
		//burn calories
		saturation -= sim.livingCostsPerTick*dt
				+ sim.movingCostsPerUnit * totalDistance
				+ sim.sizeCostsPerUnit * 3.14*radius*radius
				+ sim.brainCostPerConn * this.brain.getEnabledConnectionCount();
		
		//die if starved / perform mitosis if saturated enough
		if(saturation < 0)
			sim.getListOfSpecimen().remove(this);
		else if(saturation >= sim.saturationLimit){
			saturation /= 2;
			sim.addSpecimen(this.mutate(this.sim.mutateRate));
		}
	}
	
	protected abstract void perceive();
	protected abstract float calcMoveDistX();
	protected abstract float calcMoveDistY();
	
	@Override
	public abstract Specimen mutate(double mutationRate);
}
