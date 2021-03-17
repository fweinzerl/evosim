package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.Graphics;

import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.neuro.SimpleBrain;

public class ErroredCheatingSpecimen extends Specimen{
	public static final int WIGGLE_COUNTER_LIMIT = 4;
	
	private float acc;
	private int wiggleCounter;
	private float brainValX, brainValY; //used to sustain perceived information
	
	public ErroredCheatingSpecimen(float x, float y, float size, float initSaturation,/* float speed,*/ SimpleBrain b, float accuracy){
		super(x, y, size, initSaturation,/* speed,*/ b);
		acc = accuracy;
		wiggleCounter = 0;
	}

	@Override
	protected void perceive(){
		if(wiggleCounter == 0){
			if(sim.getListOfFood().size() > 0){
				Food f = sim.getListOfFood().get(0);
				float nearestDx = f.x-x,		nearestDy = f.y-y;
				float nearestDist = (float)Math.sqrt(nearestDx*nearestDx+nearestDy*nearestDy);
				
				for(int i = 0; i < sim.getListOfFood().size(); i++){
					f = sim.getListOfFood().get(i);
					float dx = f.x-x,		dy = f.y-y;
					float dist = (float)Math.sqrt(dx*dx+dy*dy);
					if(dist < nearestDist){
						nearestDx = dx; nearestDy = dy;
						nearestDist = dist;
					}
				}//found nearest
				
				float errX = nearestDx * (1 - (100-acc)/50*sim.rand.nextFloat());
				float errY = nearestDy * (1 - (100-acc)/50*sim.rand.nextFloat());
				float errDist = (float)Math.sqrt(errX*errX+errY*errY);
				b.setInput(0, brainValX = errX/errDist);
				b.setInput(1, brainValY = errY/errDist);
			}else{
				b.setInput(0, 0);
				b.setInput(1, 0);
			}
	}else{
			b.setInput(0, brainValX);
			b.setInput(1, brainValY);
		}
		wiggleCounter = (wiggleCounter+1) % WIGGLE_COUNTER_LIMIT;
	}

	@Override
	protected float calcMoveDistX(){
		return b.getOutput(0);
	}

	@Override
	protected float calcMoveDistY(){
		return b.getOutput(1);
	}
	
	@Override
	protected Specimen mutate(){
		return new ErroredCheatingSpecimen(x+1, y,
				(float)super.addSigmoid(spOfInfl, (sim.rand.nextFloat()-0.5)*sim.mutateRate*(sim.maxSize-sim.minSize), sim.minSize, sim.maxSize),
				saturation,
				//(float)super.addSigmoid(getSpeed(), (sim.rand.nextFloat()-0.5)*sim.mutateRate*sim.maxSpeed, 0, sim.maxSpeed),
				new SimpleBrain(2, 2),
				(float)super.addSigmoid(acc, (sim.rand.nextFloat()-0.5)*sim.mutateRate*sim.maxAccuracy, 0, sim.maxAccuracy));
	}

	@Override
	public void draw(Graphics g){
		Color c = g.getColor();
		
		g.setColor(new Color(1, 0, 0, (saturation>100)?1:saturation/100));
		g.fillOval((int)(x-spOfInfl),
					(int)(y-spOfInfl),
					(int)(2*spOfInfl),
					(int)(2*spOfInfl));
		
		g.setColor(c);
	}
}
