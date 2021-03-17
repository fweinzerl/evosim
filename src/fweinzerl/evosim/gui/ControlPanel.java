package fweinzerl.evosim.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fweinzerl.evosim.sim.Simulation;

public class ControlPanel extends JPanel{
	private Simulation sim;
	
	public ControlPanel(Simulation s){
		super();
		sim = s;
		
		GridLayout gLayout = new GridLayout(10, 2);
		//gLayout.setVgap(100);
		this.setLayout(gLayout);
		
		JToggleButton pause = new JToggleButton("Pause");
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				sim.paused = !sim.paused;
			}
		});
		this.add(pause);
		
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				sim.init();
			}
		});
		this.add(restart);
		
		
		addSliders();
	}
	
	private void addSliders(){
		JSlider sliderSpeed = genSlider(5, 25, 5, 1);
		sliderSpeed.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.simSpeed = (long)  (660000 / Math.pow(10, (Integer)sliderSpeed.getValue()/10. - 1));
			} });
		sliderSpeed.setValue(10);
		addTextSlidePair("Speed:", sliderSpeed);
		
		JSlider sliderMinSize = genSlider(0, 20, 10, 1);
		sliderMinSize.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) { sim.minSize = (float) sliderMinSize.getValue(); }
		});
		sliderMinSize.setValue(5);
		addTextSlidePair("Min Size:", sliderMinSize);
		
		JSlider sliderMaxSize = genSlider(10, 50, 10, 1);
		sliderMaxSize.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) { sim.maxSize = (float) sliderMaxSize.getValue(); }
		});
		sliderMaxSize.setValue(14);
		addTextSlidePair("Max Size:", sliderMaxSize);
		
		JSlider sliderLivingCost = genSlider(0, 30, 10, 1);
		sliderLivingCost.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.livingCostsPerTick = (float)  (0.01f * Math.pow(10, (Integer)sliderLivingCost.getValue()/10. - 3));
			} });
		sliderLivingCost.setValue(12);
		addTextSlidePair("Living Costs:", sliderLivingCost);
		
		JSlider sliderMovingCost = genSlider(-10, 20, 10, 1);
		sliderMovingCost.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.movingCostsPerUnit = (float)  (0.01 * Math.pow(10, (Integer)sliderMovingCost.getValue()/10. - 2));
			} });
		sliderMovingCost.setValue(14);
		addTextSlidePair("Moving Costs:", sliderMovingCost);
		
		JSlider sliderBrainSizeCost = genSlider(-20, 20, 10, 1);
		sliderBrainSizeCost.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.brainCostPerConn = (float)  (0.01 * Math.pow(10, (Integer)sliderBrainSizeCost.getValue()/10. - 2));
			} });
		sliderBrainSizeCost.setValue(-1);
		addTextSlidePair("Brain Size Costs:", sliderBrainSizeCost);
		
		JSlider sliderSpecimenSpawn = genSlider(0, 30, 10, 1);
		sliderSpecimenSpawn.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.specimenSpawnRate = (float)  (6000 / Math.pow(10, (Integer)sliderSpecimenSpawn.getValue()/10.-1));
			} });
		sliderSpecimenSpawn.setValue(18);
		addTextSlidePair("Specimen Spawn Rate:", sliderSpecimenSpawn);
		
		JSlider sliderFoodSpawn = genSlider(-10, 15, 10, 1);
		sliderFoodSpawn.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.foodRegrowRate = (float)  (.5f / Math.pow(10, (Integer)sliderFoodSpawn.getValue()/10. - 2));
			} });
		sliderFoodSpawn.setValue(-3);
		addTextSlidePair("Food Spawn Rate:", sliderFoodSpawn);
		
		JSlider sliderMutation = genSlider(0, 20, 7, 1);
		sliderMutation.addChangeListener(new ChangeListener(){
			@Override public void stateChanged(ChangeEvent e) {
				sim.mutateRate = (float)  (0.01 * Math.pow(10, (Integer)sliderMutation.getValue()/10. - 1));
			} });
		sliderMutation.setValue(12);
		addTextSlidePair("Mutation Rate:", sliderMutation);
	}
	
	private JSlider genSlider(int from, int to, int bigSpacing, int smallSpacing){
		JSlider slider = new JSlider(JSlider.HORIZONTAL, from, to, from);
		slider.setMajorTickSpacing(bigSpacing);
		slider.setMinorTickSpacing(smallSpacing);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		return slider;
	}
	
	private void addTextSlidePair(String text, JSlider slider){
		this.add(new JLabel(text));
		this.add(slider);
	}
}
