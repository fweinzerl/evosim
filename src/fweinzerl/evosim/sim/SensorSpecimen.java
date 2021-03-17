package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.Graphics;

import fweinzerl.evosim.neuro.Brain;
import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.sim.gene.AdvancedWholeGenome;
import fweinzerl.evosim.sim.gene.SensorPhysicalGenome;
import fweinzerl.evosim.sim.gene.SimpleWholeGenome;
import fweinzerl.evosim.sim.phys.Sensor;

public class SensorSpecimen extends Specimen{
	public static final float SQ_TO_CIRC = 1.20710679f;
	
	//perceived colors
	public static final int PERCEIVED_CL_GREEN = 1;
	public static final int PERCEIVED_CL_RED = -1;
	
	private Sensor[] sensors;
	
	public SensorSpecimen(float x, float y, float size, float initSaturation, Brain b){
		super(x, y, size, initSaturation, b);
		sensors = new Sensor[1];
		sensors[0] = new Sensor(x, y, x+spOfInfl, y);
	}
	
	public SensorSpecimen(float x, float y, float initSaturation, AdvancedWholeGenome genome){
		super(x, y, initSaturation, genome);
		spOfInfl = genome.getPhysicalGenome().getSize();
		b = new NodeBrain(genome.getNeuroGenome());
		int sensorCount = ((SensorPhysicalGenome)genome.getPhysicalGenome()).getSensorCount();
		//provisional
		sensors = new Sensor[1];
		sensors[0] = new Sensor(x, y, x+spOfInfl, y);
	}

	@Override
	protected void perceive(){
		b.setInput(sensors.length, 1);
		
		for(int s = 0; s < sensors.length; s++){
			b.setInput(s, 0); //if nothing fits, this will be the answer
			
			Float nearestFoodDist = (float) (sim.getWidth() * sim.getHeight()); //safely outside boundaries
			if(sim.getListOfFood().size() > 0){
				Food f;// = sim.getListOfFood().get(0);
				//nearestFoodDist = sensors[s].calcDistance(f.x, f.y, SQ_TO_CIRC*f.spOfInfl);
				
				for(int i = 0; i < sim.getListOfFood().size(); i++){
					f = sim.getListOfFood().get(i);
					Float dist = sensors[s].calcDistance(f.x, f.y, SQ_TO_CIRC*f.spOfInfl);
					if(dist == null)
						continue;
					if(dist < nearestFoodDist)
						nearestFoodDist = dist;
				}//found nearest food
			}
			
			Float nearestSpecDist = (float) (sim.getWidth() * sim.getHeight());
			if(sim.getListOfSpecimen().size() > 0){
				Specimen sp;// = sim.getListOfSpecimen().get(0);
				//nearestSpecDist = sensors[s].calcDistance(sp.x, sp.y, sp.spOfInfl);
				
				for(int i = 0; i < sim.getListOfSpecimen().size(); i++){
					sp = sim.getListOfSpecimen().get(i);
					Float dist = sensors[s].calcDistance(sp.x, sp.y, sp.spOfInfl);
					if(dist == null)
						continue;
					if(dist < nearestSpecDist)
						nearestSpecDist = dist;
				}//found nearest specimen
			}
			
			if(nearestFoodDist - nearestSpecDist > 0.00001){ //not sure if '==' always works with float
				b.setInput(s, PERCEIVED_CL_GREEN);
			}else if(nearestSpecDist - nearestFoodDist > 0.00001){
				b.setInput(s, PERCEIVED_CL_RED);
			}
		}
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
		AdvancedWholeGenome newG = (AdvancedWholeGenome)g.mutate(sim.mutateRate);
		
		return new SensorSpecimen(x, y, saturation, newG);
	}

	@Override
	public void draw(Graphics g){
		Color c = g.getColor();
		
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
					int lineX = (int)(x + spOfInfl*(i)/points*Math.cos(i));
					int lineY = (int)(y + spOfInfl*(i)/points*Math.sin(i));
					g.drawLine(oldLineX, oldLineY, oldLineX=lineX, oldLineY=lineY);
				}
		
		g.setColor(c);
	}
}