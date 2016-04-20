package de.carlos.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import org.junit.Test;

public class RegressionTest {
	
	List<DoubleDataPoint> dataLinear = new ArrayList<>();
	List<DoubleDataPoint> dataTwoLinear = new ArrayList<>();

	List<DoubleDataPoint> dataFlat = new ArrayList<>();
	
	List<DoubleDataPoint> dataRand = new ArrayList<>();
	
	List<DoubleDataPoint> firstVarRnd = new ArrayList<>();
	
	LinearHeuristic linear1 = new LinearHeuristic(Arrays.asList(0d,1d));
	LinearHeuristic linear2 = new LinearHeuristic(Arrays.asList(0d,-1d));
	LinearHeuristic h5Vars = new LinearHeuristic(Arrays.asList(0d,0.5d, 0.1d, 0.2d, 0.4d, 0.0d));
	
	
	LinearHeuristic hUneven = new LinearHeuristic(Arrays.asList(-100d,0.2d, 300d));

	
	public RegressionTest() {
		
		dataLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(0D)), 0D));
		dataLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(1D)), 1D));
		dataLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(2D)), 2D));
		dataLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(3D)), 3D));
		dataLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(4D)), 4D));
		
		dataTwoLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(0D,0D)), 0D));
		dataTwoLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(1D,1D)), 1D));
		dataTwoLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(2D,2D)), 2D));
		dataTwoLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(3D,3D)), 3D));
		dataTwoLinear.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(4D,4D)), 4D));

		
		dataFlat.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(1D)), 4D));
		dataFlat.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(2D)), 4D));
		dataFlat.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(3D)), 4D));
		dataFlat.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(4D)), 4D));
		dataFlat.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(5D)), 4D));

		
		dataRand.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(1D)), 4D));
		dataRand.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(2D)), 3D));
		dataRand.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(3D)), 2D));
		dataRand.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(4D)), 5D));
		dataRand.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(5D)), 10D));

		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(5D,0d)), 0D));
		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(4D,1d)), -1D));
		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(5D,2d)), -2D));
		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(1D,3d)), -3D));
		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(6D,4d)), -4D));
		firstVarRnd.add(new DoubleDataPoint(new ArrayList<Double>(Arrays.asList(5D,5d)), -5D));
		
	}
	
	
	@Test
	public void testHeuristic(){

		Heuristic<List<Double>, Double> flatZero = new LinearHeuristic(Arrays.asList(0D,0D));
		
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(0D)),0.0D);
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(1D)),0.0D);
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(2D)),0.0D);
		Assert.assertEquals(0.0d, (double) flatZero.apply(Arrays.asList(3D)),0.0D);
		
		CostFunction<List<Double>, Double> costFunction = CostFunction.doubleCostFunction();
		
		Assert.assertEquals((1d+4d+9d+16d)/(2d*5d), costFunction.calculateCost(flatZero, dataLinear),0.0D);
		
		Heuristic<List<Double>, Double> linear = new LinearHeuristic(Arrays.asList(0D,1D));
		Assert.assertEquals(0d, costFunction.calculateCost(linear, dataLinear),0.0D);

		Heuristic<List<Double>, Double> linearPlusONe = new LinearHeuristic(Arrays.asList(1D,1D));
		Assert.assertEquals(5d/(5d*2d), costFunction.calculateCost(linearPlusONe, dataLinear),0.0D);	

	}
	
	@Test
	public void testGradientDescentToZero(){
		

		
		
		checkConvergesToZero(new GradientDescent(this.dataLinear, 0.1D));
		checkConvergesToZero(new GradientDescent(this.dataLinear, 0.1D));
		
		checkConvergesToZero(new GradientDescent(this.dataFlat, 0.06D));
		
		
		checkConvergesToZero(new GradientDescent(
				this.buildForFunction(this.linear1), 
				0.01D));
		
		checkConvergesToZero(new GradientDescent(
				this.buildForFunction(this.linear2), 
				0.01D));		
		
		checkConvergesToZero(new GradientDescent(
				this.buildForFunction(this.h5Vars), 
				0.01D));	
		
		
		checkConvergesToZero(
				new AlphaUpdatingGD(
						FeatureScaling.scaleIt(
								this.buildForFunction(
										new LinearHeuristic(Arrays.asList(-200d, 0.001))))));
		
		checkConvergesToZero(
				new AlphaUpdatingGD(
						FeatureScaling.scaleIt(
								this.buildForFunction(
										new LinearHeuristic(Arrays.asList(-200.001d, 2000d, -8000d, 0.23d, -1d))))));		

	}
	
	@Test
	public void testTwoVar(){
		checkConvergesToZero(new GradientDescent(this.dataTwoLinear, 0.05D));
		
		GradientDescent descent = new GradientDescent(this.firstVarRnd, 0.005D);
		double tolerance = 0.001D;
		Assert.assertTrue(descent.getCost() > tolerance);
		//System.out.println(descent.getCost() + "  "+descent.getH().getParameters());
		double prevCost = descent.getCost();
		for (int i = 0; i<10;i++){
			descent.doIteration();
			//System.out.println(descent.getCost() + "  "+descent.getH().getParameters());
			Assert.assertTrue(prevCost >= descent.getCost());
			prevCost = descent.getCost();
		}		

		
		
	}
	
	@Test
	public void testDescentRand(){
		GradientDescent descent = new GradientDescent(this.dataFlat, 0.05D);
		double tolerance = 0.001D;
		Assert.assertTrue(descent.getCost() > tolerance);
		//System.out.println(descent.getCost() + "  "+descent.getH().getParameters());
		double prevCost = descent.getCost();
		for (int i = 0; i<10;i++){
			descent.doIteration();
			//System.out.println(descent.getCost() + "  "+descent.getH().getParameters());
			Assert.assertTrue(prevCost >= descent.getCost());
			prevCost = descent.getCost();
		}		
	}
	
	@Test
	public void testUneven(){
		checkConvergesToZero(new GradientDescent(
				FeatureScaling.scaleIt(this.buildForFunction(this.hUneven)), 
				0.005D));	
	}


	private void checkConvergesToZero(GradientDescent descent) {
		descent.converge();
		Assert.assertEquals("Did not converge",0.0D, descent.getCost(), 0.001D);
	}
	
	public List<DoubleDataPoint> buildForFunction(LinearHeuristic h){
		
		Random r = new Random(0);
		
		List<DoubleDataPoint> data = new ArrayList<>();
		for (int i = 0; i<100; i++){
			
			List<Double> values = new ArrayList<Double>();
			for(int v = 0; v<h.getParameters().size()-1;v++){
				values.add(r.nextDouble());
			}
			
			data.add(new DoubleDataPoint(values, h.apply(values)));
		}
		
		return data;
	}
	
	@Test
	public void testFeatureScaling(){
		
		FeatureScaling scaling = new FeatureScaling(dataLinear);
		Assert.assertEquals(1,scaling.meanNormalizers.size());
		Assert.assertEquals(1,scaling.scalers.size());
		Assert.assertEquals(2d, (double) scaling.meanNormalizers.get(0), 0.01d);
		Assert.assertEquals(4d, (double) scaling.scalers.get(0), 0.01d);
	}
	
	@Test
	public void testMeanNormalization(){
		//Check that mean-normalization helps.
		List<DoubleDataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(100d,1d)));				
		double learningRate = 0.01;		
		GradientDescent descent = new GradientDescent(data, learningRate);
		try{
			checkConvergesToZero(descent);
			Assert.fail();
		}catch (AssertionError e){
			//Expected
		}		
		descent = new GradientDescent(FeatureScaling.scaleIt(data), learningRate);
		checkConvergesToZero(descent);

	}
	
	@Test
	public void testScaling(){
		//Check that mean-normalization helps.
		List<DoubleDataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(0d,10000d)));				
		double learningRate = 0.02;		
		GradientDescent descent = new GradientDescent(data, learningRate);
		try{
			checkConvergesToZero(descent);
			Assert.fail();
		}catch (AssertionError e){
			//Expected
		}		
		descent = new GradientDescent(FeatureScaling.scaleIt(data), learningRate);
		checkConvergesToZero(descent);

	}
	
	@Test
	public void testAlphaUpdating(){
		List<DoubleDataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(0.2d,0.3d)));				
		double learningRate = 0.1;		
		GradientDescent descent = new GradientDescent(data, learningRate);
		try{
			checkConvergesToZero(descent);
			Assert.fail();
		}catch (NoDescentException e){
			//Expected
		}		
		descent = new AlphaUpdatingGD(FeatureScaling.scaleIt(data), learningRate);
		checkConvergesToZero(descent);
		
		
	}
	
	

}
