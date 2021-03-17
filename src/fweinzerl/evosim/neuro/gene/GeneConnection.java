package fweinzerl.evosim.neuro.gene;

public class GeneConnection implements Cloneable{
	private int innov;
	public boolean enabled;
	private int from;
	private int to;
	private float weight;
	
	public GeneConnection(/*int innovation, */int from, int to, float weight){
		innov = NeuroGenome.globalInnovationNrConn++;//innovation;
		enabled = true;
		this.from = from;
		this.to= to;
		this.weight = weight;
	}
	
	private GeneConnection(int innovation, int from, int to, float weight, boolean enabled){
		innov = innovation;
		this.enabled = enabled;
		this.from = from;
		this.to= to;
		this.weight = weight;
	}
	
	/*public void setInnovation(int innovation){ innov = innovation; }
	public void setSrcNode(int source){ from = source; }
	public void setDestNode(int destination){ to = destination; }
	public void setWeight(float weight){ this.weight = weight; }*/

	public int getInnovation(){ return innov; }
	public int getSrcNode(){ return from; }
	public int getDestNode(){ return to; }
	public float getWeight(){ return weight; }
	
	@Override
	public GeneConnection clone(){
		return new GeneConnection(innov, from, to, weight, enabled);
	}
}
