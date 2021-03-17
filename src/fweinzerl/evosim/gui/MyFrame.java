package fweinzerl.evosim.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import fweinzerl.evosim.sim.Simulation;

public class MyFrame extends JFrame{
	public MyFrame(Simulation s){
		this.setLayout(new BorderLayout());
		this.add(new SimPanel(s), BorderLayout.CENTER);
		this.add(new ControlPanel(s), BorderLayout.WEST);
		this.pack();
	}
}
