package fweinzerl.evosim;

import fweinzerl.evosim.gui.MyFrame;
import fweinzerl.evosim.neuro.SimpleBrain;
import fweinzerl.evosim.sim.*;

public class Start{
	public static void main(String args[]){
		Simulation sim = new Simulation();
		MyFrame frame = new MyFrame(sim);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		//frame.setSize(1000, 1000);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		sim.simLoop();
	}
}
