package fweinzerl.evosim.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import fweinzerl.evosim.sim.Simulation;

public class SimPanel extends JPanel{
	private Simulation sim;
	
	public SimPanel(Simulation s){
		super();
		sim = s;
		sim.setParent(this);
		this.setPreferredSize(new Dimension(sim.getWidth(), sim.getHeight()));
		this.setBackground(Color.BLACK);
	}
	
	public void paintComponent(Graphics g){
		Color c = g.getColor();
		
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, sim.getWidth(), sim.getHeight());
		sim.draw(g);
		
		g.setColor(c);
	}
}
