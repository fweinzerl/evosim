package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.Graphics;

import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.neuro.SimpleBrain;
import fweinzerl.evosim.phys.Physiology;
import fweinzerl.evosim.util.Util;

public class ErroredCheatingSpecimen extends Specimen{
	public static final int WIGGLE_COUNTER_LIMIT = 4;
	
	private float acc;
	private int wiggleCounter;
	private float brainValX, brainValY; //used to sustain perceived information
	
	public ErroredCheatingSpecimen(float x, float y, float radius, float initSaturation,
					Brain brain, float accuracy){
		super(x, y, radius, initSaturation, brain);
		this.acc = accuracy;
		this.wiggleCounter = 0;
	}
	
	public ErroredCheatingSpecimen(float x, float y, float initSaturation,
					Brain brain, Physiology physiology, float accuracy){
		super(x, y, initSaturation, brain, physiology);
		this.acc = accuracy;
		this.wiggleCounter = 0;
	}

	@Override
	protected void perceive(){
		if(this.wiggleCounter == 0){
			if(this.sim.getListOfFood().size() > 0){
				Food f = this.sim.getListOfFood().get(0);
				float nearestDx = f.x-x,		nearestDy = f.y-y;
				float nearestDist = (float)Math.sqrt(nearestDx*nearestDx + nearestDy*nearestDy);
				
				for(int i = 0; i < this.sim.getListOfFood().size(); i++){
					f = this.sim.getListOfFood().get(i);
					float dx = f.x-x,		dy = f.y-y;
					float dist = (float)Math.sqrt(dx*dx + dy*dy);
					if(dist < nearestDist){
						nearestDx = dx; nearestDy = dy;
						nearestDist = dist;
					}
				}// found nearest
				
				float errX = nearestDx * (1 - (100-acc)/50*sim.rand.nextFloat());
				float errY = nearestDy * (1 - (100-acc)/50*sim.rand.nextFloat());
				float errDist = (float)Math.sqrt(errX*errX+errY*errY);
				this.brain.setInput(0, brainValX = errX/errDist);
				this.brain.setInput(1, brainValY = errY/errDist);
			}else{
				this.brain.setInput(0, 0);
				this.brain.setInput(1, 0);
			}
		}
		wiggleCounter = (wiggleCounter+1) % WIGGLE_COUNTER_LIMIT;
	}

	@Override
	protected float calcMoveDistX(){
		return this.brain.getOutput(0);
	}

	@Override
	protected float calcMoveDistY(){
		return this.brain.getOutput(1);
	}
	
	@Override
	public Specimen mutate(double mutationRate){
		return new ErroredCheatingSpecimen(x+1, y,
						(float) Util.addSigmoid(this.radius,
										(sim.rand.nextFloat()-0.5)*mutationRate*(sim.maxSize-sim.minSize),
										sim.minSize, sim.maxSize),
						saturation, new SimpleBrain(),
						(float) Util.addSigmoid(this.acc,
										(sim.rand.nextFloat()-0.5)*mutationRate*sim.maxAccuracy,
										0, sim.maxAccuracy));
	}

	@Override
	public void draw(Graphics g){
		Color c = g.getColor();
		
		g.setColor(new Color(1, 0, 0, (saturation>100) ? 1 : saturation/100));
		g.fillOval((int)(this.x - this.radius),
					(int)(this.y - this.radius),
					(int)(2 * this.radius),
					(int)(2 * this.radius));
		
		g.setColor(c);
	}
}
