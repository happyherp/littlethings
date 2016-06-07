package jsat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import jsat.classifiers.CategoricalData;
import jsat.classifiers.DataPoint;
import jsat.classifiers.neuralnetwork.BackPropagationNet;
import jsat.classifiers.trees.RandomForest;
import jsat.datatransform.LinearTransform;
import jsat.datatransform.NominalToNumeric;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.regression.LogisticRegression;
import jsat.regression.RegressionDataSet;
import jsat.regression.RegressionModelEvaluation;
import jsat.regression.Regressor;
import jsat.regression.evaluation.MeanAbsoluteError;
import jsat.regression.evaluation.MeanSquaredError;

public class WsftarDecode
{
    
    
    
    public static void main(String[] args) throws IOException{
        
        File f = new File("C:\\data\\wsftar_dec_10k.csv");
        
        List<DataPoint> datapoints = loadDataPoints(f);
        
        
        for (int targetIndex = 0;targetIndex<datapoints.get(0).getNumericalValues().length();targetIndex++){
            
            System.out.println("Target Index: "+targetIndex);
            try{
                RegressionDataSet rDataSet = new RegressionDataSet(datapoints, targetIndex);
                
                LinearTransform linearTransform = new LinearTransform(rDataSet, 0.0, 1.0);
                rDataSet.applyTransform(linearTransform);
                
                
                
                //BackPropagationNet regressor = new BackPropagationNet(1);    
                //LogisticRegression regressor = new LogisticRegression(); //Did not work at all. 
                RandomForest regressor = new RandomForest();
                
                regressor.train(rDataSet);
                
                evaluate(rDataSet, regressor);
                show(rDataSet, regressor);
                
                //System.out.println( regressor.getCoefficents());
                
            }catch (Exception e){
                e.printStackTrace();
            }
            
        }
        System.out.println("Done");

        
        
    }    //

    


    public static List<DataPoint> loadDataPoints(File f) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        
        reader.readLine();//Header
        String line = reader.readLine();
           
        
        List<DataPoint> datapoints = new ArrayList<>();
        List<Integer> filNrs = new LinkedList<>();
        while (line != null){
            line = line.replace("\"", "").replace(",", ".");
            String[] row = line.split("\\|");
            double[] rowI = new double[row.length];
            for (int i = 0;i<row.length;i++){
                rowI[i] = Double.parseDouble(row[i]);
            }
            Vec vec = new DenseVector(rowI);
            
            DataPoint dp = new DataPoint(vec);
            datapoints.add(dp);
        
            line = reader.readLine();
        }
        return datapoints;
    }
    
    public static void evaluate(RegressionDataSet rDataSet, Regressor regressor)
    {
        RegressionModelEvaluation eval = new RegressionModelEvaluation(regressor, rDataSet, Executors.newFixedThreadPool(8));
        eval.setKeepModels(true);
        eval.addScorer(new MeanSquaredError());
        eval.addScorer(new MeanAbsoluteError());
        eval.evaluateTestSet(rDataSet);
        eval.prettyPrintRegressionScores();
        System.out.println("Training-time: "+eval.getTotalTrainingTime());
        System.out.println("classification-time: "+eval.getTotalClassificationTime());
    }
    
    public static void show(RegressionDataSet rDataSet, Regressor regressor)
    {
        for(int i = 0; i < 10; i++)
        {
            int index = rDataSet.getSampleSize()*i/10;
            DataPoint dataPoint = rDataSet.getDataPoint(index);//It is important not to mix these up, the class has been removed from data points in 'cDataSet'
            
            double truth = rDataSet.getTargetValue(index);                
            double result = regressor.regress(dataPoint);
            System.out.println("Truth "+truth+ " result: "+result+ " diff:"+Math.abs(truth-result));
            
            //DataPoint original = linearTransform.inverse(dataPoint);
            //System.out.println("Original: "+original.getNumericalValues());

        }
    }
}
