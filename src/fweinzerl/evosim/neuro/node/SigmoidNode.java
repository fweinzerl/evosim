package fweinzerl.evosim.neuro.node;

import java.util.ArrayList;
import java.util.Arrays;

public class SigmoidNode extends ConnectionNode{
	public SigmoidNode(int innov){
		super(innov);
	}
	
	public SigmoidNode(int innov, ArrayList<Node> dendrites, float[] weights){
		super(innov, dendrites, weights);
	}
	
	public void process(){
		float sum = 0;
		for(int i = 0; i < dends.size(); i++)
			sum += dends.get(i).getState() * weights.get(i);
		state = (float) Math.tanh(sum);
	}
}
