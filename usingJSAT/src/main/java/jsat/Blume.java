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
import jsat.classifiers.svm.DCDs;
import jsat.classifiers.trees.DecisionTree;
import jsat.classifiers.trees.ERTrees;
import jsat.datatransform.LinearTransform;
import jsat.datatransform.NominalToNumeric;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.regression.RegressionDataSet;
import jsat.regression.RegressionModelEvaluation;
import jsat.regression.Regressor;
import jsat.regression.evaluation.MeanAbsoluteError;
import jsat.regression.evaluation.MeanSquaredError;

public class Blume
{
    
    public static void main(String[] args) throws IOException{
        
        //File f = new File("C:\\data\\wawi_art2027.csv");
        File f = new File("C:\\data\\wawi_art2027_10k.csv");
        //File f = new File("C:\\data\\wawi_art2027_100k.csv");        
        RegressionDataSet rDataSet = loadRegressionDataFilialeUndTag(f);
        
        //With wawi_art2027.csv
        //BackProb 3,3,3: Mean abs error: 29
        //BackProb 10,10,10: Mean abs error: 19
        //Backprob 20,20 -> 20
        //Backprop 20,20,10,10,3 -> 1277/24,7 overfitting?
        //Backprob 5,5,5,5,5 -> 1472/26,6
        // 10,5,5,5 -> 1047/21,75
        
        //with wawi_art2027_10k
        // 3,3,3 -> 622/15.1
        // 10,10,10 -> 562/14.5
        // 15,10,5 -> 561/14.5
        // 2,2 -> 766/17.9
        //30,30,30 -> 549/14.2 in 1177s
        //using DCD 100,false: 1716/34
        // DCD 1000,false: 673/17
        // DCD 10000,false: 651/17
        // DCDs 1000,false : 737/17 (but much faster traning than DCD)
        //DecisionTree: 1232/22 in 11s
        //ErTrees ERTrees(10): didnt finish in an hour.
        
        //with wai_art2027_100k
        // backprop 3,3,3 -> 1133/22 in 1225s
        // DCDs 1000,false -> 1694/31
        // backprop 10,10,10 ->
        
        
        //with wai_art2027_10k and loadRegressionDataFilialeUndTag
        // backprop 3,3,3 -> 
                
        Regressor regressor = new BackPropagationNet(3,3,3);        
        evaluate(rDataSet, regressor);
        trainRealAndShow(rDataSet, regressor);
        
        System.out.println("Done");
        
        
    }

    public static void trainRealAndShow(RegressionDataSet rDataSet, Regressor regressor)
    {
        regressor.train(rDataSet,Executors.newFixedThreadPool(8));
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

    public static void evaluate(RegressionDataSet rDataSet, Regressor regressor)
    {
        RegressionModelEvaluation eval = new RegressionModelEvaluation(regressor, rDataSet, Executors.newFixedThreadPool(8));
        eval.setKeepModels(true);
        eval.addScorer(new MeanSquaredError());
        eval.addScorer(new MeanAbsoluteError());
        eval.evaluateCrossValidation(3);
        eval.prettyPrintRegressionScores();
        System.out.println("Training-time: "+eval.getTotalTrainingTime());
        System.out.println("classification-time: "+eval.getTotalClassificationTime());
    }

    public static RegressionDataSet loadRegressionData(File f) throws FileNotFoundException, IOException
    {

        BufferedReader reader = new BufferedReader(new FileReader(f));
        
        reader.readLine();//Header
        String line = reader.readLine();
   
        CategoricalData[] catData = new CategoricalData[]{new CategoricalData(0), new CategoricalData(7)};
        
        List<DataPoint> datapoints = new ArrayList<>();
        List<Integer> filNrs = new LinkedList<>();
        while (line != null){
            
            line = line.replace("\"", "").replace(",", ".");
            String[] row = line.split("\\|");
            
            if (row.length == 8){
                int filNr =  Integer.parseInt( row[0]);
                if (!filNrs.contains(filNr)){
                    filNrs.add(filNr);
                }
                int filIndex = filNrs.indexOf(filNr);
                
                int date = Integer.parseInt( row[1]);
                
                
                Calendar cal = Calendar.getInstance();
                cal.set(1900, 1, 1, 0, 0, 0);
                cal.add(Calendar.DAY_OF_YEAR, date);
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                
                double vkpreis = Double.parseDouble(row[2]);
                
                int BESTMGGANF = Integer.parseInt(row[3]);
                int BESTMGRANF = Integer.parseInt(row[4]);
                double BESTWTVKANF = Double.parseDouble(row[5]);
                //int BESTWTVKANF_1 = Integer.parseInt(row[6]);
                int VERKMGSTGE = Integer.parseInt(row[7]);            
                
                Vec vec = new DenseVector(new double[]{date, dayOfYear, vkpreis, BESTMGGANF, BESTMGRANF, BESTWTVKANF, VERKMGSTGE});
                int[] catValues = new int[]{filIndex, cal.get(Calendar.DAY_OF_WEEK)-1};
                
                
                
                DataPoint dp = new DataPoint(vec,catValues, catData);        
                datapoints.add(dp);
            }
            line = reader.readLine();
        }

        catData[0] = new CategoricalData(filNrs.size());
        
        int targetIndex = 6;
        RegressionDataSet rDataSet = new RegressionDataSet(datapoints, targetIndex);

        
        
        NominalToNumeric nominalTransform = new NominalToNumeric(rDataSet);
        rDataSet.applyTransform(nominalTransform);
        
        LinearTransform linearTransform = new LinearTransform(rDataSet, 0.0, 1.0);
        rDataSet.applyTransform(linearTransform);
        
        System.out.println("Size: "+rDataSet.getSampleSize());
        System.out.println("Number of Numerical vars" + rDataSet.getNumNumericalVars());
        return rDataSet;
    }
    
    
    public static RegressionDataSet loadRegressionDataFilialeUndTag(File f) throws FileNotFoundException, IOException
    {

        BufferedReader reader = new BufferedReader(new FileReader(f));
        
        reader.readLine();//Header
        String line = reader.readLine();
   
        CategoricalData[] catData = new CategoricalData[]{new CategoricalData(0), new CategoricalData(7)};
        
        List<DataPoint> datapoints = new ArrayList<>();
        List<Integer> filNrs = new LinkedList<>();
        while (line != null){
            
            line = line.replace("\"", "").replace(",", ".");
            String[] row = line.split("\\|");
            
            if (row.length == 8){
                int filNr =  Integer.parseInt( row[0]);
                if (!filNrs.contains(filNr)){
                    filNrs.add(filNr);
                }
                int filIndex = filNrs.indexOf(filNr);
                
                int date = Integer.parseInt( row[1]);
                
                
                Calendar cal = Calendar.getInstance();
                cal.set(1900, 1, 1, 0, 0, 0);
                cal.add(Calendar.DAY_OF_YEAR, date);
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                
                double vkpreis = Double.parseDouble(row[2]);
                
                int BESTMGGANF = Integer.parseInt(row[3]);
                int BESTMGRANF = Integer.parseInt(row[4]);
                double BESTWTVKANF = Double.parseDouble(row[5]);
                //int BESTWTVKANF_1 = Integer.parseInt(row[6]);
                int VERKMGSTGE = Integer.parseInt(row[7]);            
                
                Vec vec = new DenseVector(new double[]{VERKMGSTGE, date, dayOfYear,});
                int[] catValues = new int[]{filIndex, cal.get(Calendar.DAY_OF_WEEK)-1};
                
                
                
                DataPoint dp = new DataPoint(vec,catValues, catData);        
                datapoints.add(dp);
            }
            line = reader.readLine();
        }

        catData[0] = new CategoricalData(filNrs.size());
        
        int targetIndex = 0;
        RegressionDataSet rDataSet = new RegressionDataSet(datapoints, targetIndex);

        
        
        NominalToNumeric nominalTransform = new NominalToNumeric(rDataSet);
        rDataSet.applyTransform(nominalTransform);
        
        LinearTransform linearTransform = new LinearTransform(rDataSet, 0.0, 1.0);
        rDataSet.applyTransform(linearTransform);
        
        System.out.println("Size: "+rDataSet.getSampleSize());
        System.out.println("Number of Numerical vars" + rDataSet.getNumNumericalVars());
        return rDataSet;
    }

}
