package fweinzerl.evosim.neuro.gene;

public class GeneNode{
	private int innov;
	private int type;
	
	public GeneNode(/*int innovation, */int type){
		innov = NeuroGenome.globalInnovationNrNode;//innovation;
		this.type = type;
	}
	
	/*public void setInnovation(int innovation){ innov = innovation; }
	public void setType(int type){ this.type = type; }*/

	public int getInnovation(){ return innov; }
	public int getType(){ return type; }
}
