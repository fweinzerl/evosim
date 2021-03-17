package fweinzerl.evosim.sim;

import java.awt.Color;
import java.awt.Graphics;

public class Food extends SimulationObject{
	public Food(float x, float y, float size){
		super(x, y, size);
	}
	
	public float getNutritionalVal(){
		return spOfInfl*sim.foodNutrientFactor;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		
		g.setColor(Color.GREEN);
		g.fillRect((int)(x-spOfInfl),
					(int)(y-spOfInfl),
					(int)(2*spOfInfl),
					(int)(2*spOfInfl));
		
		g.setColor(c);
	}
}
