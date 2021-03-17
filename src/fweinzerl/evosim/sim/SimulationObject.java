package fweinzerl.evosim.sim;

import java.awt.Graphics;

public abstract class SimulationObject {
	protected Simulation sim;
	protected float x, y;
	protected float spOfInfl; //radius of the sphere of influence
	
	public SimulationObject(float x, float y, float sphereOfInfluence){
		this.x = x;//(x%sim.getWidth() + sim.getWidth()) % sim.getWidth();
		this.y = y;//(y%sim.getHeight() + sim.getHeight()) % sim.getHeight();
		spOfInfl = sphereOfInfluence;
	}
	
	public void setParent(Simulation sim){
		this.sim = sim;
	}
	
	public void addToCoordinates(float dx, float dy){
		x = (x+dx + sim.getWidth()) % sim.getWidth();
		//if(x-spOfInfl < 0) x = spOfInfl;
		//if(x+spOfInfl > sim.getWidth()) x = sim.getWidth()-spOfInfl;
		y = (y+dy + sim.getHeight()) % sim.getHeight();
		/*y += dy;
		if(y-spOfInfl < 0) y = spOfInfl;
		if(y+spOfInfl > sim.getHeight()) y = sim.getHeight()-spOfInfl;*/
	}
	
	public void update(float dt){}
	
	public abstract void draw(Graphics g);
}
