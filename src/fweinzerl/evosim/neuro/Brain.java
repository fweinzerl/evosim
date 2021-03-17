package fweinzerl.evosim.neuro;

public abstract class Brain{
	protected float[] inputs;
	protected float[] outputs;
	
	public Brain(int inputCount, int outputCount){
		inputs = new float[inputCount];
		outputs = new float[outputCount];
	}
	
	public abstract void process();
	
	public void setInput(int index, float value){
		inputs[index] = value;
	}
	
	public float getOutput(int index){
		return outputs[index];
	}
	
	public int getNoOfInputs(){ return inputs.length; }
	public int getNoOfOutputs(){ return outputs.length; }
	
	public abstract int getEnabledConnections();
}
