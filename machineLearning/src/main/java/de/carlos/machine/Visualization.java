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
	

	private static void displayIterationCollection(XYSeriesCollection collection) {
		ApplicationFrame af = new ApplicationFrame("Visualization of GradientDescent");
		JFreeChart lineChart = ChartFactory.createXYLineChart("", "Iteration", "Cost", collection);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
	
		af.setContentPane(chartPanel);

		af.pack();
		RefineryUtilities.centerFrameOnScreen(af);
		af.setVisible(true);
	}
	
	public static void main(String[] args){
		ClassificationHeuristic h = new ClassificationHeuristic(Arrays.asList(0d,0d));
		GradientDescent d1=  new GradientDescent(h,  RegressionTest.classification1, 5D);
		GradientDescent d2=  new AlphaUpdatingGD(h,  RegressionTest.classification1, 5D);
		
		
		Visualization.showGDs(300, d1,d2);
	}

}
