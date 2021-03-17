package fweinzerl.evosim.neuro.node;

public class InitNode extends Node{
	public InitNode(int innov){
		super(innov);
		state = 1;
	}
	
	public void setState(float newState){
		state = newState;
	}
	
	public void process(){}
}
