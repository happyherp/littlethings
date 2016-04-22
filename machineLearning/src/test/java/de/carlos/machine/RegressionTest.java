package de.carlos.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RegressionTest {
	
	List<DataPoint> dataLinear = new ArrayList<>();
	List<DataPoint> dataTwoLinear = new ArrayList<>();

	List<DataPoint> dataFlat = new ArrayList<>();
	
	List<DataPoint> dataRand = new ArrayList<>();
	
	List<DataPoint> firstVarRnd = new ArrayList<>();
	
	List<DataPoint> classification1 = new ArrayList<>();

	
	Heuristic linear1 = new LinearHeuristic(Arrays.asList(0d,1d));
	Heuristic linear2 = new LinearHeuristic(Arrays.asList(0d,-1d));
	Heuristic h5Vars = new LinearHeuristic(Arrays.asList(0d,0.5d, 0.1d, 0.2d, 0.4d, 0.0d));
	
	
	Heuristic hUneven = new LinearHeuristic(Arrays.asList(-100d,0.2d, 300d));

	
	public RegressionTest() {
		
		dataLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0D)), 0D));
		dataLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(1D)), 1D));
		dataLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(2D)), 2D));
		dataLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(3D)), 3D));
		dataLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(4D)), 4D));
		
		dataTwoLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0D,0D)), 0D));
		dataTwoLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(1D,1D)), 1D));
		dataTwoLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(2D,2D)), 2D));
		dataTwoLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(3D,3D)), 3D));
		dataTwoLinear.add(new DataPoint(new ArrayList<Double>(Arrays.asList(4D,4D)), 4D));

		
		dataFlat.add(new DataPoint(new ArrayList<Double>(Arrays.asList(1D)), 4D));
		dataFlat.add(new DataPoint(new ArrayList<Double>(Arrays.asList(2D)), 4D));
		dataFlat.add(new DataPoint(new ArrayList<Double>(Arrays.asList(3D)), 4D));
		dataFlat.add(new DataPoint(new ArrayList<Double>(Arrays.asList(4D)), 4D));
		dataFlat.add(new DataPoint(new ArrayList<Double>(Arrays.asList(5D)), 4D));

		
		dataRand.add(new DataPoint(new ArrayList<Double>(Arrays.asList(1D)), 4D));
		dataRand.add(new DataPoint(new ArrayList<Double>(Arrays.asList(2D)), 3D));
		dataRand.add(new DataPoint(new ArrayList<Double>(Arrays.asList(3D)), 2D));
		dataRand.add(new DataPoint(new ArrayList<Double>(Arrays.asList(4D)), 5D));
		dataRand.add(new DataPoint(new ArrayList<Double>(Arrays.asList(5D)), 10D));

		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(5D,0d)), 0D));
		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(4D,1d)), -1D));
		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(5D,2d)), -2D));
		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(1D,3d)), -3D));
		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(6D,4d)), -4D));
		firstVarRnd.add(new DataPoint(new ArrayList<Double>(Arrays.asList(5D,5d)), -5D));
		
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.0d)), 0D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.2d)), 0D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.3d)), 0D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.4d)), 0D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.5d)), 0D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.6d)), 1D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.7d)), 1D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.8d)), 1D));
		classification1.add(new DataPoint(new ArrayList<Double>(Arrays.asList(0.9d)), 1D));
		
		
	}
	
	
	@Test
	public void testLinearHeuristic(){

		Heuristic flatZero = new LinearHeuristic(Arrays.asList(0D,0D));
		
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(0D)),0.0D);
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(1D)),0.0D);
		Assert.assertEquals(0.0D, (double) flatZero.apply(Arrays.asList(2D)),0.0D);
		Assert.assertEquals(0.0d, (double) flatZero.apply(Arrays.asList(3D)),0.0D);
		
		CostFunction costFunction = new SquaredError();
		
		Assert.assertEquals((1d+4d+9d+16d)/(2d*5d), costFunction.calculateCost(flatZero, dataLinear),0.0D);
		
		Heuristic linear = new LinearHeuristic(Arrays.asList(0D,1D));
		Assert.assertEquals(0d, costFunction.calculateCost(linear, dataLinear),0.0D);

		Heuristic linearPlusONe = new LinearHeuristic(Arrays.asList(1D,1D));
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
	
	public List<DataPoint> buildForFunction(Heuristic h){
		
		Random r = new Random(0);
		
		List<DataPoint> data = new ArrayList<>();
		for (int i = 0; i<100; i++){
			
			List<Double> values = new ArrayList<Double>();
			for(int v = 0; v<h.getParameters().size()-1;v++){
				values.add(r.nextDouble());
			}
			
			data.add(new DataPoint(values, h.apply(values)));
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
		List<DataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(100d,1d)));				
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
		List<DataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(0d,10000d)));				
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
		List<DataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(0.2d,0.3d)));				
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
	
	@Test
	public void testWithOneMissing(){
		
		
		List<DataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(3d, 10d, 0.1d, 4d)));		

		//remove one variable from the learning data. 
		data.stream().forEach(d->d.values.remove(0));
		
		AlphaUpdatingGD gd = new AlphaUpdatingGD(data);
		Assert.assertTrue(gd.getCost() > 10.0d);
		gd.converge();
		Assert.assertTrue(gd.getCost() < 5.00d);		
	}
	
	
	@Test
	public void trainingVsTest(){
		
		List<DataPoint> data = this.buildForFunction(new LinearHeuristic(Arrays.asList(10d, -2d, 0.1d, 4.3d)));	
		
		int splitIndex = (int) (data.size()*0.7);
		List<DataPoint> trainingData = data.subList(0, splitIndex);
		List<DataPoint> testData = data.subList(splitIndex, data.size());
		
		
		FeatureScaling scale = new FeatureScaling(trainingData); 
		AlphaUpdatingGD gd = new AlphaUpdatingGD(scale.convertDataPointsToScale(trainingData));
		gd.converge();
		
		double costTraining = new SquaredError().calculateCost(gd.getH(), scale.convertDataPointsToScale(trainingData));		
		double costTest =     new SquaredError().calculateCost(gd.getH(), scale.convertDataPointsToScale(testData));
		
		Assert.assertEquals(costTraining, costTest ,0.01d);	
	}	
	
	
	@Test
	public void testClassificationHeuristic(){
		Heuristic h = new ClassificationHeuristic(Arrays.asList(0D,1D));
		for (int i = 0; i<100;i++){
			double x = i/10D-5D;
			double p = h.apply(Arrays.asList(x));
			System.out.println("x:"+x+" p:"+p);
			Assert.assertTrue(p>=0.0d);
			Assert.assertTrue(p<=1.0d);
		}
	}
	
	@Test
	public void testLogisticRegression(){
		
		ClassificationHeuristic h = new ClassificationHeuristic(Arrays.asList(0d,0d));
		GradientDescent d=  new GradientDescent(h, this.classification1, 1D);
		Assert.assertTrue(d.getCost() > 0.1d);
		
		d.converge();
		
		for (DataPoint dp: this.classification1){
			double prediction = d.getH().apply(dp.values);
			System.out.println("For "+dp+" predicted "+prediction);
			if (dp.result == 0){
				Assert.assertTrue(prediction < 0.5d);
			}else{
				Assert.assertTrue(prediction > 0.5d);
			}
		}
		
		Assert.assertEquals(0.0, d.getCost(), 0.01d);	
	}
	
}
