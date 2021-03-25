package fweinzerl.evosim.sim;

import java.awt.Graphics;

public abstract class SimulationObject {
	protected Simulation sim;
	protected float x, y;
	protected float radius;
	
	public SimulationObject(float x, float y, float radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void setParent(Simulation sim){
		this.sim = sim;
	}
	
	public void update(float dt){}
	
	public abstract void draw(Graphics g);
}
