package fweinzerl.evosim.neuro.node;

import java.util.ArrayList;

public abstract class ConnectionNode extends Node{
	protected ArrayList<Node> dends; //dendrites
	protected ArrayList<Float> weights;
	
	public ConnectionNode(int innov){
		super(innov);
		dends = new ArrayList<Node>();
		weights = new ArrayList<Float>();
	}
	
	public ConnectionNode(int innov, ArrayList<Node> dendrites, float[] weights){
		super(innov);
		this.weights = faToFL(weights);
		dends = dendrites;
	}
	
	public void addDendrite(Node d, float weight){
		dends.add(d);
		weights.add(weight);
	}
	
	public void setDendrites(ArrayList<Node> dendrites, float[] weights){
		dends = dendrites;
		this.weights = faToFL(weights);
	}
	
	public ArrayList<Float> faToFL(float[] fa){
		ArrayList<Float> fL = new ArrayList<>(fa.length);
		for(int i = 0; i < fa.length; i++)
			fL.add(fa[i]);
		return fL;
	}
}
