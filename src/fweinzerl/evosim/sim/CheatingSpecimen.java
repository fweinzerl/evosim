package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.sim.gene.Genome;
import fweinzerl.evosim.sim.gene.SimpleWholeGenome;
import fweinzerl.evosim.sim.gene.WholeGenome;

public class CheatingSpecimen extends Specimen{
	public static final int WIGGLE_COUNTER_LIMIT = 4;
	
	private float acc;
	private int wiggleCounter;
	private float brainValX, brainValY; //used to sustain perceived information
	
	public CheatingSpecimen(float x, float y, float size, float initSaturation, Brain b, float accuracy){
		super(x, y, size, initSaturation, b);
		acc = accuracy;
		wiggleCounter = 0;
	}
	
	public CheatingSpecimen(float x, float y, float initSaturation, SimpleWholeGenome genome){
		super(x, y, initSaturation, genome);
		spOfInfl = genome.getPhysicalGenome().getSize();
		b = new NodeBrain(genome.getNeuroGenome());
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
				
				b.setInput(0, brainValX = nearestDx/nearestDist);
				b.setInput(1, brainValY = nearestDy/nearestDist);
			}else{
				b.setInput(0, 0);
				b.setInput(1, 0);
			}
		}else{
			b.setInput(0, brainValX);
			b.setInput(1, brainValY);
		}
		b.setInput(2, 1);
		wiggleCounter = (wiggleCounter+1) % WIGGLE_COUNTER_LIMIT;
	}

	@Override
	protected float calcMoveDistX(){
		return b.getOutput(1)-b.getOutput(0);
	}

	@Override
	protected float calcMoveDistY(){
		return b.getOutput(3)-b.getOutput(2);
	}
	
	@Override
	protected Specimen mutate(){
		SimpleWholeGenome newG = (SimpleWholeGenome)g.mutate(sim.mutateRate);
		
		return new CheatingSpecimen(x, y, saturation, newG);
	}

	@Override
	public void draw(Graphics g){
		Color c = g.getColor();
		
		//draw circle
		g.setColor(new Color(1, 0, 0, (saturation>100)?1:saturation/100));
		g.fillOval((int)(x-spOfInfl),
					(int)(y-spOfInfl),
					(int)(2*spOfInfl),
					(int)(2*spOfInfl));
		
		//write connections
		g.setColor(new Color(1, 1, 1, (saturation>100)?1:saturation/100));
		int points = b.getEnabledConnections();
		int oldLineX = (int)x, oldLineY = (int)y;
		for(int i = 0; i < points; i++){
			int lineX = (int)(x + spOfInfl*(i)/points*Math.cos(i/*6.283185/points*/));
			int lineY = (int)(y + spOfInfl*(i)/points*Math.sin(i/*6.283185/points*/));
			g.drawLine(oldLineX, oldLineY, oldLineX=lineX, oldLineY=lineY);
		}
		
		g.setColor(c);
	}
}
