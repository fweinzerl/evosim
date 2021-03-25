package fweinzerl.evosim.gene;

public class GeneNode{
	private int innov;
	private int type;
	
	public GeneNode(int type){
		innov = NeuroGenome.globalInnovationNrNode++;
		this.type = type;
	}

	public int getInnovation(){ return innov; }
	public int getType(){ return type; }
}
