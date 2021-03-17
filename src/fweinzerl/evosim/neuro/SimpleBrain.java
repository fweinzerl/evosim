package fweinzerl.evosim.neuro;

public class SimpleBrain extends Brain{
	public SimpleBrain(int inputCount, int outputCount){
		super(inputCount, outputCount);
	}

	public void process(){
		outputs[0] = inputs[0];
		outputs[1] = inputs[1];
	}
	
	public int getEnabledConnections(){
		return 2;
	}
}
