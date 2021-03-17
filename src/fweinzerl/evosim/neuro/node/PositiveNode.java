package fweinzerl.evosim.neuro.node;

import java.util.ArrayList;

public class PositiveNode extends ConnectionNode{
	public PositiveNode(int innov){
		super(innov);
	}
	
	public PositiveNode(int innov, ArrayList<Node> dendrites, float[] weights){
		super(innov, dendrites, weights);
	}
	
	public void process(){
		state = 0;
		for(int i = 0; i < dends.size(); i++)
			state += dends.get(i).getState() * weights.get(i);
		if(state < 0)
			state = 0;
	}
}
