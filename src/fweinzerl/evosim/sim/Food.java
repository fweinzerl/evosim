package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.Graphics;

public class Food extends SimulationObject{
	public Food(float x, float y, float radius){
		super(x, y, radius);
	}
	
	public float getNutritionalVal(){
		return this.radius * this.sim.foodNutrientFactor;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		
		g.setColor(Color.GREEN);
		g.fillRect((int)(x-this.radius),
					(int)(y-this.radius),
					(int)(2*this.radius),
					(int)(2*this.radius));
		
		g.setColor(c);
	}
}
