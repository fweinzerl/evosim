package fweinzerl.evosim.sim.phys;

public class Sensor{
	private float xStart, yStart;
	private float k;
	private float d;
	private boolean right;
	
	private float kSqp1;
	
	public Sensor(float xStart, float yStart, float k, float d, boolean right){
		this.xStart = xStart;
		this.yStart = yStart;
		this.k = k;
		this.d = d;
		this.right = right;
		kSqp1 = k*k+1;
	}
	
	public Sensor(float xStart, float yStart, float xEnd, float yEnd){
		k = (yEnd-yStart)/(xEnd-xStart);
		d = yStart-k*xStart;
		right = xEnd > xStart;
		kSqp1 = k*k+1;
	}
	
	public Float calcDistance(float xm, float ym, float r){
		//a = kSqp1
		float b = 2*(k*(d-ym)-xm);
		float c = xm*xm+(d-ym)*(d-ym)-r*r;
		float discr = b*b-4*kSqp1*c;
		if(discr < 0)
			return null;
		
		float x = (-b) + (right?-1:1) * (float)Math.sqrt(discr) / 2 / kSqp1;
		if(right ^ x > xStart) //must be the right direction
			return null;
		
		float y = k*x+d - yStart; //it's actually deltaY
		x -= xStart; //now this is deltaX
		return (float) Math.sqrt(x*x + y*y);
	}
}
