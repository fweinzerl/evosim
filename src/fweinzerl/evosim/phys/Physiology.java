package fweinzerl.evosim.phys;

import fweinzerl.evosim.gene.Mutatable;
import fweinzerl.evosim.gene.PhysicalGenome;

public abstract class Physiology implements Mutatable{
	protected PhysicalGenome physGenome;
	
	public Physiology(PhysicalGenome physGenome){
		this.physGenome = physGenome;
	}
	
	public abstract float getApproximateRadius();
}
