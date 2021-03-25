package fweinzerl.evosim.neuro;

public class SimpleBrain extends Brain{
	public SimpleBrain(){
		super(2, 2);
	}

	public void process(){
		outputs[0] = inputs[0];
		outputs[1] = inputs[1];
	}
	
	public int getEnabledConnectionCount(){
		return 2;
	}
	
	public SimpleBrain mutate(double mutationRate){
		return new SimpleBrain();
	}
}
