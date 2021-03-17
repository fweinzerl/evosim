package fweinzerl.evosim.sim;

import java.awt.Graphics;

public abstract class SimulationObject {
	protected Simulation sim;
	protected float x, y;
	protected float spOfInfl; //radius of the sphere of influence
	
	public SimulationObject(float x, float y, float sphereOfInfluence){
		this.x = x;
		this.y = y;
		spOfInfl = sphereOfInfluence;
	}
	
	public void setParent(Simulation sim){
		this.sim = sim;
	}
	
	public void addToCoordinates(float dx, float dy){
		x = (x+dx + sim.getWidth()) % sim.getWidth();
		y = (y+dy + sim.getHeight()) % sim.getHeight();
	}
	
	public void update(float dt){}
	
	public abstract void draw(Graphics g);
}
