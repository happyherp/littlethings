package jsat;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import jsat.classifiers.DataPoint;
import jsat.classifiers.linear.LogisticRegressionDCD;
import jsat.classifiers.linear.PassiveAggressive;
import jsat.classifiers.neuralnetwork.BackPropagationNet;
import jsat.classifiers.svm.DCD;
import jsat.classifiers.trees.DecisionTree;
import jsat.classifiers.trees.RandomForest;
import jsat.datatransform.LinearTransform;
import jsat.regression.KernelRidgeRegression;
import jsat.regression.LogisticRegression;
import jsat.regression.MultipleLinearRegression;
import jsat.regression.RegressionDataSet;
import jsat.regression.RegressionModelEvaluation;
import jsat.regression.Regressor;
import jsat.regression.evaluation.MeanAbsoluteError;
import jsat.regression.evaluation.MeanSquaredError;

public class Regression
{

    public static final String UCI_NUMERIC = "C:\\data\\UCI\\numeric\\";

    public static void main(String[] args)
    {
        //File file = new File(UCI_NUMERIC + "sleep.arff");
        File file = new File("C:\\data\\UCI\\regression-datasets\\house_16H.arff");
        
        DataSet dataSet =ARFFLoader.loadArffFile(file);
        
        
        dataSet =  dataSet.getMissingDropped();
        
        
        
        //dataSet.applyTransform(new NominalToNumeric(dataSet));
        LinearTransform linearTransform = new LinearTransform(dataSet, 0.0, 1.0);
        dataSet.applyTransform(linearTransform);
        
        int targetIndex = 16;
        
        RegressionDataSet rDataSet = new RegressionDataSet(  dataSet.getDataPoints(), targetIndex);

        
        
        Regressor[] regressors = new Regressor[]{
                new BackPropagationNet(3,2),
                new DCD(),
                new DecisionTree(),
                //new KernelRidgeRegression(),
                new LogisticRegression(),
                new RandomForest(),
                new MultipleLinearRegression(),
                new BackPropagationNet(10,6,3),
                new BackPropagationNet(5,5,5,5,5),
                };
        
        
        for (Regressor regressor: regressors){
            
            System.out.println("Regressor: "+regressor);
            
            
            RegressionModelEvaluation eval = new RegressionModelEvaluation(regressor, rDataSet, Executors.newFixedThreadPool(8));
            eval.setKeepModels(true);
            eval.addScorer(new MeanSquaredError());
            eval.addScorer(new MeanAbsoluteError());
            eval.evaluateCrossValidation(10);
            eval.prettyPrintRegressionScores();
            System.out.println("Training-time: "+eval.getTotalTrainingTime());
            System.out.println("classification-time: "+eval.getTotalClassificationTime());
            
            
            
            
            regressor.train(rDataSet);
            for(int i = 0; i < 10; i++)
            {
                int index = rDataSet.getSampleSize()*i/10;
                DataPoint dataPoint = rDataSet.getDataPoint(index);//It is important not to mix these up, the class has been removed from data points in 'cDataSet'
                
                double truth = rDataSet.getTargetValue(index);                
                double result = regressor.regress(dataPoint);
                System.out.println("Truth "+truth+ " result: "+result+ " diff:"+Math.abs(truth-result));

                double originalTruth = linearTransform.inverse(dataSet.getDataPoint(index)).getNumericalValues().get(targetIndex);
                DataPoint clone = linearTransform.inverse(dataSet.getDataPoint(index));
                clone.getNumericalValues().set(targetIndex, result);
                double reversedResult = linearTransform.inverse(clone).getNumericalValues().get(targetIndex);
                System.out.println("Original Truth:"+ originalTruth
                        + " reversed Result:"+reversedResult
                        + "  diff: "+Math.abs(originalTruth-reversedResult));
                
                
            }
        }
    }
}
