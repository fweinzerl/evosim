package fweinzerl.evosim.neuro;

import java.util.ArrayList;
import java.util.Arrays;

import fweinzerl.evosim.neuro.gene.GeneConnection;
import fweinzerl.evosim.neuro.gene.GeneNode;
import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.neuro.node.ConnectionNode;
import fweinzerl.evosim.neuro.node.InitNode;
import fweinzerl.evosim.neuro.node.Node;
import fweinzerl.evosim.neuro.node.PositiveNode;
import fweinzerl.evosim.neuro.node.SigmoidNode;

public class NodeBrain extends Brain{
	private InitNode[] inNodes;
	private ConnectionNode[] midNodes;
	private ConnectionNode[] outNodes;
	private int numOfEnbldConnections;
	
	public NodeBrain(int inputCount, int outputCount){
		super(inputCount, outputCount);
		inNodes = new InitNode[inputCount];
		for(int i = 0; i < inNodes.length; i++)
			inNodes[i] = new InitNode(0);
		midNodes = new ConnectionNode[0];
		outNodes = new ConnectionNode[outputCount];
		for(int i = 0; i < outNodes.length; i++){
			outNodes[i] = new SigmoidNode(0);
			outNodes[i].addDendrite(inNodes[i], 0.5f);
		}
		
		numOfEnbldConnections = 2;
	}
	
	public NodeBrain(NeuroGenome ng){
		super(ng.getInputCount(), ng.getOutputCount());
		GeneNode[] nodes = ng.getNodeGenes();
		GeneConnection[] conns = ng.getConnectionGenes();
		
		numOfEnbldConnections = conns.length;
		
		inNodes = new InitNode[inputs.length];
		for(int i = 0; i < inNodes.length; i++)
			inNodes[i] = new InitNode(i);
		
		outNodes = new ConnectionNode[outputs.length];
		for(int i = 0; i < outNodes.length; i++)
			outNodes[i] = new PositiveNode(inputs.length+i);
		
		midNodes = new ConnectionNode[nodes.length];
		for(int i = 0; i < midNodes.length; i++)
			midNodes[i] = new SigmoidNode(nodes[i].getInnovation());
		
		for(GeneConnection gc : conns)
			if(gc.enabled)
				((PositiveNode)getNodeByInnov(gc.getDestNode())).addDendrite(getNodeByInnov(gc.getSrcNode()), gc.getWeight());
		// TODO: if nodes are not in topological order here, there might be problems with processing later
	}

	public void process(){
		//set inputs
		for(int i = 0; i < inNodes.length; i++)
			inNodes[i].setState(inputs[i]);
		
		//let all nodes process
		for(Node n : inNodes) n.process();
		for(Node n : midNodes) n.process();
		for(Node n : outNodes) n.process();
		
		//read output
		for(int i = 0; i < outputs.length; i++)
			outputs[i] = outNodes[i].getState();
	}
	
	public int getEnabledConnections(){
		return numOfEnbldConnections;
	}
	
	/**
	 * For navigating in the big array (in+mid+out)
	 * 
	 * @param index
	 * @return
	 */
	private Node getNode(int index){
		if(index < inNodes.length)
			return inNodes[index];
		
		index -= inNodes.length;
		if(index < midNodes.length)
			return midNodes[index];
		
		index -= midNodes.length;
		if(index < outNodes.length)
			return outNodes[index];
		
		return null;
	}
	
	private Node getNodeByInnov(int innovation){
		if(innovation < inNodes.length)
			return inNodes[innovation];
		
		if(innovation < inNodes.length+outNodes.length)
			return outNodes[innovation-inNodes.length];
		
		for(Node n : midNodes)
			if(n.getInnovation() == innovation)
				return n;
		
		return null;
	}
	
	private boolean setNode(int index, Node n){
		if(index < inNodes.length)
			try{
				inNodes[index] = (InitNode)n; return true;
			}catch(ClassCastException e){
				return false;
			}
		
		index -= inNodes.length;
		if(index < midNodes.length){
			try{
				midNodes[index] = (ConnectionNode)n; return true;
			}catch(ClassCastException e){
				return false;
			}
		}
		
		index -= midNodes.length;
		if(index < outNodes.length){
			try{
				outNodes[index] = (ConnectionNode)n; return true;
			}catch(ClassCastException e){
				return false;
			}
		}
		
		return false;
	}
}
