package fweinzerl.evosim.util;

public class Util{
	/**
	 * Add one number to another where effect decreases the farther away from zero their sum is.
	 * Also ensure the value stays inside [min, max].
	 */
	public static double addSigmoid(double val, double addThis, double min, double max){
		// TODO: this function does not actually ensure the value stays inside [min, max] if addThis is too big
		// x = (val-min)/max                 <-    break down for sigmoid (=logistical function)
		// y = 1/(1+e^(ln(1/x-1)-addThis))   <-    reverse and forward sigmoid function, so that something actually gets added/subtracted
		// return y*max + min			     <-    lift up after sigmoid
		if(val <= min)
			val = min + 0.0000001f;
		if(val >= max)
			val = max - 0.0000001f;
		return max / (1 + Math.exp(Math.log(1 / ((val-min)/max) - 1)-addThis))  +  min;
	}
}
