package de.carlos.machine;

import java.awt.Panel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Visualization {


	public static void showGDs(int iterations,GradientDescent... gds) {

		XYSeriesCollection cost = new XYSeriesCollection();
		XYSeriesCollection learningRate = new XYSeriesCollection();
		
		List<XYSeriesCollection> parameters = new ArrayList<>();
		for (Object o:  gds[0].getH().getParameters()){
			parameters.add(new XYSeriesCollection());
		}
		
		
		for (int gdIndex=0;gdIndex<gds.length;gdIndex++){
			GradientDescent gd = gds[gdIndex];
			
			XYSeries costDataset = new XYSeries("cost gd#"+gdIndex);
			XYSeries learningRateDataset = new XYSeries("learningrate gd#"+gdIndex);
			for (int pindex = 0;pindex < gd.getH().getParameters().size();pindex++){				
				parameters.get(pindex).addSeries(new XYSeries("gd#"+gdIndex+" parameter#"+pindex));
			}
			
			for(int i = 0;i<=iterations;i++){
				costDataset.add(i, gd.getCost());
				learningRateDataset.add(i, gd.getLearningRate());
				
				for (int pindex = 0;pindex < gd.getH().getParameters().size();pindex++){				
					parameters.get(pindex).getSeries(gdIndex).add(i, gd.getH().getParameters().get(pindex));;
				}
				
				gd.doIteration();
			}		
			cost.addSeries(costDataset);
			learningRate.addSeries(learningRateDataset);
		}

		Panel mainPanel = new Panel();

		
		JFreeChart costlineChart = ChartFactory.createXYLineChart("", "Iteration", "Cost", cost);
		ChartPanel costCharPlanel = new ChartPanel(costlineChart);
		costCharPlanel.setPreferredSize(new java.awt.Dimension(560, 367));
		mainPanel.add(costCharPlanel);
		
		JFreeChart learningratelineChart = ChartFactory.createXYLineChart("", "Iteration", "learningrate", learningRate);
		ChartPanel learningrateCharPlanel = new ChartPanel(learningratelineChart);
		learningrateCharPlanel.setPreferredSize(new java.awt.Dimension(560, 367));
		mainPanel.add(learningrateCharPlanel);
		
		for (XYSeriesCollection parameterCollection: parameters){
			JFreeChart parameterlineChart = ChartFactory.createXYLineChart("", "Iteration", "parameter", parameterCollection);
			ChartPanel parameterCharPlanel = new ChartPanel(parameterlineChart);
			parameterCharPlanel.setPreferredSize(new java.awt.Dimension(560, 367));	
			mainPanel.add(parameterCharPlanel);
		}
		
		

		ApplicationFrame af = new ApplicationFrame("Visualization of GradientDescent");
		af.setContentPane(mainPanel);
		af.pack();
		RefineryUtilities.centerFrameOnScreen(af);
		af.setVisible(true);

	}	
	

	
	public static void main(String[] args){
		Heuristic h = new ClassificationHeuristic(2);
		List<DataPoint> data = DataPoint.buildForFunction(
				input -> {
					double v = input.get(0)*0.3 + input.get(1)*-0.4 + 0.5;
					return v>0.5?1d:0d;
				} 
				,2);
		//data = FeatureScaling.scaleIt(data);
		GradientDescent d=  new AlphaUpdatingGD(h,  data, 1.1D);
		
		
		Visualization.showGDs(100, d);
	}
	
	public static void gdVsAlphaUpdating(){
		Heuristic h = new LinearHeuristic(Arrays.asList(0d,0d,0d,0d));
		List<DataPoint> data = DataPoint.buildForFunction(new LinearHeuristic(Arrays.asList(3d, 10d, 0.1d, 4d)));
		data = FeatureScaling.scaleIt(data);
		GradientDescent d1=  new GradientDescent(h,  data, 0.01D);
		GradientDescent d2=  new AlphaUpdatingGD(h.asZeroParmater(),  data, 5D);
		
		
		Visualization.showGDs(10, d1,d2);
	}

}
