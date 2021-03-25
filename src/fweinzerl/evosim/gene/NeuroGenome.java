package fweinzerl.evosim.gene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import fweinzerl.evosim.sim.Simulation;
import fweinzerl.evosim.sim.Specimen;
import fweinzerl.evosim.gene.Genome;

/**
 * This class defines the genome of a neural network in the Simulation.
 * It is used in i.e. NodeBrain, but is not limited to that object.
 * The order of innovation numbers is: inputs [0..iC-1], outputs [iC..iC+oC-1],
 * mid-nodes { i | i >= iC+oC }
 * 
 * @author fweinzerl
 *
 */
public class NeuroGenome extends Genome{
	public static final int NODE_TYPE_SIGMOID = 1;
	public static int globalInnovationNrNode = 0;
	public static int globalInnovationNrConn = 0;
	
	private int inputCount, outputCount; //these should not be changed during mutation
	private GeneNode[] nodes;
	private ArrayList<GeneConnection> conns;
	private ArrayList<GeneConnection> disConns; //disabled connections
	
	public NeuroGenome(Simulation s, int inputCount, int outputCount, GeneNode[] nodes, ArrayList<GeneConnection> enConnections){
		super(s);
		this.inputCount = inputCount;
		this.outputCount = outputCount;
		this.nodes = nodes;
		conns = enConnections;
		disConns = new ArrayList<>();
	}
	
	public int getInputCount(){ return inputCount; }
	public int getOutputCount(){ return outputCount; }
	public GeneNode[] getNodeGenes(){ return nodes; }
	public GeneConnection[] getConnectionGenes(){ return conns.toArray(new GeneConnection[0]); }
	//public int getDisabledConnectionCount(){ return disConns.size(); }
	
	@Override
	public NeuroGenome mutate(double mutationRate){
		// create new NeuroGenome to be mutated
		NeuroGenome m = new NeuroGenome(sim, inputCount, outputCount, nodes.clone(), null);
		m.conns = (ArrayList<GeneConnection>) conns.clone();
		m.disConns = (ArrayList<GeneConnection>) disConns.clone();
		
		float mutationType = sim.rand.nextFloat();
		if(conns.size() > 0 && mutationType < 0.7){// 70%: alter connection
			this.mutate_alterConnection(m, sim.rand, 0.05f, (float) mutationRate);
		}
		
		else if(mutationType < 0.82){// 12%: en-/disable connection
			mutate_toggleConnectionActivation(m, sim.rand);
		}
		
		else{// if(mutationType < 1){// 18%: add new connection
			mutate_addConnection(m, sim.rand, (float) mutationRate);
		}
		
		return m;
	}
	
	/**
	 * @param randomChance chance, that a new weight gets chosen completely randomly as opposed to
	 * 					   the old one only being slightly changed.
	 */
	private void mutate_alterConnection(NeuroGenome mutated, Random r, float randomChance, float mutationRate){
		int index = r.nextInt(conns.size());
		GeneConnection conn = this.conns.get(index); // get connection to be altered
		if(r.nextFloat() < randomChance){ // choose new random one
			mutated.conns.set(index, new GeneConnection(conn.getSrcNode(),
												  conn.getDestNode(),
												  (r.nextFloat()-.5f) * 20*mutationRate));
		}else{ // perturb weight
			float change = r.nextFloat()-.5f; // in [-.5f, .5f]
			mutated.conns.set(index, new GeneConnection(conn.getSrcNode(),
												  conn.getDestNode(),
												  conn.getWeight() + change*change * mutationRate));
		}
	}
	
	private void mutate_toggleConnectionActivation(NeuroGenome mutated, Random r){
		if(this.conns.size() > 0){
			int index = r.nextInt(this.conns.size());
		
			GeneConnection dis = mutated.conns.get(index).clone();
			mutated.conns.remove(index);
			dis.enabled = false;
			mutated.disConns.add(dis);
			
		}else if(this.disConns.size() > 0){
			int index = r.nextInt(this.disConns.size());
		
			GeneConnection en = mutated.disConns.get(index).clone();
			mutated.disConns.remove(index);
			en.enabled = true;
			mutated.conns.add(en);
		}
	}
	
	private void mutate_addConnection(NeuroGenome mutated, Random r, float mutationRate){
		int fromIndex = r.nextInt(this.inputCount + this.nodes.length);
		if(fromIndex > this.inputCount)
			fromIndex += this.outputCount;
		if(fromIndex >= this.inputCount + this.outputCount)// take innovation-nr if out of ins/outs
			fromIndex = this.nodes[fromIndex - this.inputCount - this.outputCount].getInnovation();
		
		int toIndex = this.inputCount + r.nextInt(this.outputCount + this.nodes.length);
		if(toIndex >= this.inputCount + this.outputCount)// take innovation-nr if out of ins/outs
			toIndex = nodes[toIndex - this.inputCount - this.outputCount].getInnovation();
		
		mutated.conns.add(new GeneConnection(fromIndex,
											 toIndex,
											 (r.nextFloat()-.5f) * 20*mutationRate));
	}
}
