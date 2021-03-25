package fweinzerl.evosim.neuro;

import fweinzerl.evosim.gene.Mutatable;
import fweinzerl.evosim.gene.NeuroGenome;

public abstract class Brain implements Mutatable{
	protected NeuroGenome neuroGenome;
	
	protected float[] inputs;
	protected float[] outputs;
	
	public Brain(int inputCount, int outputCount){
		this.inputs = new float[inputCount];
		this.outputs = new float[outputCount];
	}
	
	public Brain(NeuroGenome neuroGenome){
		this.neuroGenome = neuroGenome;
		this.inputs = new float[this.neuroGenome.getInputCount()];
		this.outputs = new float[this.neuroGenome.getOutputCount()];
	}
	
	public abstract void process();
	
	public void setInput(int index, float value){
		this.inputs[index] = value;
	}
	
	public float getOutput(int index){
		return this.outputs[index];
	}
	
	public int getNoOfInputs(){ return this.inputs.length; }
	public int getNoOfOutputs(){ return this.outputs.length; }
	
	public abstract int getEnabledConnectionCount();
}
