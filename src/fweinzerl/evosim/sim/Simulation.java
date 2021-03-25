package fweinzerl.evosim.sim;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;

import fweinzerl.evosim.neuro.NodeBrain;
import fweinzerl.evosim.neuro.SimpleBrain;
import fweinzerl.evosim.gene.GeneConnection;
import fweinzerl.evosim.gene.GeneNode;
import fweinzerl.evosim.gene.NeuroGenome;
import fweinzerl.evosim.gene.SimplePhysicalGenome;

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
	public float sizeCostsPerUnit = 0.0001f;
	public float brainCostPerConn = 0.0004f;
	
	//seasons
	public float seasonEffect = 0.64f;
	public float seasonLength = 8000;
	public float currSeasonalEffect = 0; //do not set this! it gets set in the loop
	
	public float specimenSpawnRate = 60000;
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
	
	public Specimen genRandomSpecimen(){
		float size = (rand.nextFloat()*0.6f + 0.4f) * maxSize;
		
		/*return new ErroredCheatingSpecimen(rand.nextInt(w), rand.nextInt(h),
											size, saturationLimit*.75f,
											new SimpleBrain(), rand.nextInt(20));*/
		
		ArrayList<GeneConnection> agc = new ArrayList<>();
		agc.add(new GeneConnection(0, 2, .02f));
		NodeBrain brain = new NodeBrain(new NeuroGenome(this, 2, 2, new GeneNode[0], agc));
		
		return new ErroredCheatingSpecimen(rand.nextInt(w), rand.nextInt(h),
										size, saturationLimit*.75f,
										brain, rand.nextInt(20));
	}
	
	public Food genRandomFood(){
		float size = rand.nextFloat()*avgFoodSize + avgFoodSize/2;
		return new Food(rand.nextInt(w),
						rand.nextInt(h),
						size);
	}
	
	public void draw(Graphics g){
		for(int i = 0; i < losp.size(); i++)// for-each loops will throw exceptions, I don't know why though
			losp.get(i).draw(g);
		for(int i = 0; i < lof.size(); i++)
			lof.get(i).draw(g);
		Toolkit.getDefaultToolkit().sync();
	}
}
