package fweinzerl.evosim.sim;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;

import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.neuro.SimpleBrain;
import fweinzerl.evosim.neuro.gene.GeneConnection;
import fweinzerl.evosim.neuro.gene.GeneNode;
import fweinzerl.evosim.neuro.gene.NeuroGenome;
import fweinzerl.evosim.sim.gene.AdvancedWholeGenome;
import fweinzerl.evosim.sim.gene.SensorPhysicalGenome;
import fweinzerl.evosim.sim.gene.SimplePhysicalGenome;
import fweinzerl.evosim.sim.gene.SimpleWholeGenome;

public class Simulation{
	public float avgFoodSize = 6;
	
	//genetic
	public float minSize = 5;
	public float maxSize = 10;
	public float mutateRate = 0.0398f;
	
	//cheating specimen
	public float maxAccuracy = 100;

	public float speedFactor = 0.05f;
	public float saturationLimit = 100;
	public float foodNutrientFactor = 5;
	public float livingCostsPerTick = 0.00398f;
	public float movingCostsPerUnit = 0.01995f;
	public float sizeCostsPerUnit = 0;//0.0001f;
	public float brainCostPerConn = 0.0004f;
	
	//seasons
	public float seasonEffect = 0.84f;
	public float seasonLength = 8000;
	public float currSeasonalEffect = 0; //do not set this! it gets set in the loop
	
	public float specimenSpawnRate = 6000;
	public float foodRegrowRate = 3.15478f;
	public float reinstantiateWind = 70;
	
	public static final long UPDATE_CONSTANT = 60;
	public long simSpeed = 660000; //*UPDATE_CONSTANT = nanoseconds=ticks between simulation steps
	public boolean paused = false;

	public static final Random rand = new Random();
	
	private JComponent parent;
	private long lastStep;
	private long spawnCounter, growCounter;
	private double seasonCounter;
	
	private int w, h;
	private ArrayList<Specimen> losp; //list of specimen
	private ArrayList<Food> lof; //list of food pieces
	
	public Simulation(){
		w = 1200;
		h = 900;
		init();
	}
	
	public int getWidth(){ return w; }
	public int getHeight(){ return h; }
	
	public void setParent(JComponent parent){ this.parent = parent; }
	public ArrayList<Specimen> getListOfSpecimen(){ return losp; }
	public ArrayList<Food> getListOfFood(){ return lof; }
	
	public void addSpecimen(Specimen sp){
		losp.add(sp);
		sp.setParent(this);
	}
	
	public void addFood(Food f){
		lof.add(f);
		f.setParent(this);
	}
	
	public void init(){
		losp = new ArrayList<>();
		lof = new ArrayList<>();
		spawnCounter = 0;
		growCounter = 0;
		seasonCounter = 0;
		lastStep = System.nanoTime();
		
		addSpecimen(genRandomSpecimen());
		NeuroGenome.globalInnovationNrNode += 6;
	}
	
	public void simLoop(){
		while(true){
			long curTime = System.nanoTime();
			if(curTime-lastStep > UPDATE_CONSTANT*simSpeed){
				lastStep = curTime;
				
				if(!paused){
					for(int i = 0; i < losp.size(); i++)
						losp.get(i).update(UPDATE_CONSTANT);
					
					currSeasonalEffect = (float) Math.sin(seasonCounter/seasonLength*6.28318) * seasonEffect;
					
					for(;spawnCounter > specimenSpawnRate; spawnCounter -= specimenSpawnRate)
						addSpecimen(genRandomSpecimen());
					for(;growCounter > foodRegrowRate * (1-currSeasonalEffect); growCounter -= foodRegrowRate * (1-currSeasonalEffect))
						addFood(genRandomFood());
					
					parent.repaint();
					
					spawnCounter++;
					growCounter++;
					seasonCounter = (seasonCounter+1) % seasonLength;
				}
			}
			
		}
	}
	
	public void applyWind(ArrayList<? extends SimulationObject> aso, float windX, float windY){
		for(int i = 0; i < aso.size(); i++){
			SimulationObject so = aso.get(i);
			so.addToCoordinates(windX * so.spOfInfl, windY * so.spOfInfl);
		}
	}
	
	public Specimen genRandomSpecimen(){
		float size = rand.nextFloat()*maxSize*0.6f+0.4f*maxSize;
		
		return new ErroredCheatingSpecimen(rand.nextInt(w),//-(int)(2*size))+(int)size,
											rand.nextInt(h),//-(int)(2*size))+(int)size,
											size,
											saturationLimit*.75f,
											new SimpleBrain(2, 2),
											rand.nextInt(20));
		
		/*ArrayList<GeneConnection> agc = new ArrayList<>();
		//agc.add(new GeneConnection(0, 3, -.02f));
		agc.add(new GeneConnection(1, 4, .02f));
		//agc.add(new GeneConnection(1, 5, -.02f));
		//agc.add(new GeneConnection(1, 6, .02f));
		AdvancedWholeGenome swg = new AdvancedWholeGenome(this, new SensorPhysicalGenome(this, size, 1),
						new NeuroGenome(this, 2, 4, new GeneNode[0], agc));
		
		return new SensorSpecimen(rand.nextInt(w),//-(int)(2*size))+(int)size,
									rand.nextInt(h),//-(int)(2*size))+(int)size,
									saturationLimit*.75f,
									swg);*/
	}
	
	public Food genRandomFood(){
		float size = rand.nextFloat()*avgFoodSize + avgFoodSize/2;
		//float border = (maxSize > size)?maxSize:size;
		return new Food(rand.nextInt(w),//-(int)(2*border))+(int)border,
						rand.nextInt(h),//-(int)(2*border))+(int)border,
						size);
	}
	
	public void draw(Graphics g){
		try{
			for(Specimen sp : losp)
				sp.draw(g);
			for(Food f : lof)
				f.draw(g);
		}catch(NullPointerException npe){}
		catch(IndexOutOfBoundsException ioobe){}
		Toolkit.getDefaultToolkit().sync();
	}
}
