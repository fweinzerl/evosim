package fweinzerl.evosim.neuro.gene;

import java.util.ArrayList;
import java.util.Arrays;

import fweinzerl.evosim.sim.Simulation;
import fweinzerl.evosim.sim.Specimen;
import fweinzerl.evosim.sim.gene.Genome;

/**
 * This class defines the genome of a neural network in the Simulation.
 * It is used in i.e. NodeBrain, but is not limited to that object.
 * The order of innovation numbers is: inputs [0..iC-1], outputs [iC..iC+oC-1],
 * mid-nodes [ i | i >= iC+oC ]
 * 
 * @author schnitzel
 *
 */
public class NeuroGenome extends Genome{
	public static final int NODE_TYPE_SIGMOID = 1;
	public static int globalInnovationNrNode = 0;
	public static int globalInnovationNrConn = 0;
	
	private int inputCount, outputCount; //these should not be changed during mutation
	private GeneNode[] nodes; // send nodes ;)
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
	public NeuroGenome mutate(float rate){
		NeuroGenome m = new NeuroGenome(sim, inputCount, outputCount, nodes.clone(), null); //mutated
		m.conns = (ArrayList<GeneConnection>) conns.clone();
		m.disConns = (ArrayList<GeneConnection>) disConns.clone();
		
		float mutation = sim.rand.nextFloat();
		if(conns.size() > 0 && mutation < 0.7){// 70%: alter connection
			int index = sim.rand.nextInt(conns.size());
			if(sim.rand.nextFloat() < 0.95){ // 95%: perturb weight
				float change = sim.rand.nextFloat()-.5f;
				m.conns.set(index, new GeneConnection(
									conns.get(index).getSrcNode(),
									conns.get(index).getDestNode(),
									conns.get(index).getWeight() + change*change*rate));
			}else{ //5%: choose new random one
				m.conns.set(index, new GeneConnection(
									conns.get(index).getSrcNode(),
									conns.get(index).getDestNode(),
									(sim.rand.nextFloat()-.5f) * 20*rate));
			}
			
			return m;
		}
		
		else if(mutation < 0.82){// 12%: en-/disable connection
			if(sim.rand.nextFloat() < 1 && conns.size() > 0){
				int index = sim.rand.nextInt(conns.size());
			
				GeneConnection dis = m.conns.get(index).clone();
				m.conns.remove(index);
				dis.enabled = false;
				m.disConns.add(dis);
				
			}else if(disConns.size() > 0){
				int index = sim.rand.nextInt(disConns.size());
			
				GeneConnection en = m.disConns.get(index).clone();
				m.disConns.remove(index);
				en.enabled = true;
				m.conns.add(en);
			}
		}
		
		else{//if(mutation < 1){// 18%: add new connection
			int fromIndex = sim.rand.nextInt(inputCount+nodes.length);
			if(fromIndex > inputCount)
				fromIndex += outputCount;
			if(fromIndex >= inputCount+outputCount)// take innovation-nr if out of ins/outs
				fromIndex = nodes[fromIndex-inputCount-outputCount].getInnovation();
			
			int toIndex = inputCount + sim.rand.nextInt(outputCount+nodes.length);
			if(toIndex >= inputCount+outputCount)// take innovation-nr if out of ins/outs
				toIndex = nodes[toIndex-inputCount-outputCount].getInnovation();
			
			m.conns.add(new GeneConnection(
							fromIndex,
							toIndex,
							(sim.rand.nextFloat()-.5f) * 20*rate));
		}
		
		return m;
	}
	
	public static void switchInArray(Object[] a, int i1, int i2){
		Object temp = a[i1];
		a[i1] = a[i2];
		a[i2] = temp;
	}
}
