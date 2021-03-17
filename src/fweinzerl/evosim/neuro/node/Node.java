package fweinzerl.evosim.neuro.node;

public abstract class Node{
	protected int innov; //innovation number as kind of index in history
	protected float state;
	
	public Node(int innov){
		this.innov = innov;
	}
	
	public int getInnovation(){ return innov; }
	public float getState(){ return state; }
	
	public abstract void process();
}
