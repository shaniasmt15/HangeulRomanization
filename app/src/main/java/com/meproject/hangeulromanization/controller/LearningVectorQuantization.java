package com.meproject.hangeulromanization.controller;

import android.graphics.Bitmap;
import android.util.Log;

import com.meproject.hangeulromanization.entities.TestingResult;
import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.TestingResultModel;
import com.meproject.hangeulromanization.model.WeightModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LearningVectorQuantization {

    ImageProcessing imageProcessing = new ImageProcessing();
    TestingResultModel testingResultModel = new TestingResultModel();
    TestingResultController testingResultController = new TestingResultController();
    WeightModel weightModel = new WeightModel();

    public static int MAX_EPOCH = 50;
    public static double LEARNING_RATE = 0.1;
    public static double MIN_ERROR = 0.01;
    public static double DECREASE_LR = 0.1;

    public void TrainingProcess(List<TrainingData> trainingDataList, List<Weight> weightList, List<Word> wordList) throws SQLException {
        Log.d("Hangeul","Start Training Data...");
        int epoch = 1;
        //setiap epoch akan melatih semua data train yg ada
        while(epoch<=MAX_EPOCH && LEARNING_RATE>=MIN_ERROR){
            Log.d("Hangeul","Epoch: "+epoch+"");
            //untuk ngelooping semua training dataList
            for(TrainingData data:trainingDataList){
                //setelah pre-processing akan mengambil hasil ekstraksi ciri  dari data dgn Flattening data
                double[] imgFeatures = imageProcessing.getExtractedFeature(Converter.ByteArraytoBitmap(data.getImage()));
                //prediksi untuk mencari jarak terkecil antara imgFeatures dgn semua bobot
                Word predictedWord = PredictHangeul(imgFeatures,weightList,wordList);
                //pengecekan apakah prediksi sama data inputannya
                Boolean isCorrectedPrediction = (predictedWord.getWordId()==data.getWordId());
                //update bobot
                Weight updatedWeight = getUpdatedWeight(imgFeatures,weightList.get(predictedWord.getWordId()-1),isCorrectedPrediction);
                weightList.set(predictedWord.getWordId()-1,updatedWeight);
            }
            //setelah pelatihan semua data, maka bobot terbaru 1 Epoch di update ke DB
            for(Weight weight:weightList){
                weightModel.updateWeight(weight);
            }
            LEARNING_RATE -= (DECREASE_LR*LEARNING_RATE);
            //dilakukan untuk epoch selanjutnya
            epoch++;
        }
        Log.d("Hangeul","Training Data Done");
    }

    public void TestingProcess(List<TrainingData> testingDataList, List<Weight> weightList, List<Word> wordList) throws SQLException{
        Log.d("Hangeul","Start Testing Data...");
        for(TrainingData data:testingDataList){
            double[] imgFeatures = imageProcessing.getExtractedFeature(Converter.ByteArraytoBitmap(data.getImage()));
            Word predictedWord = PredictHangeul(imgFeatures,weightList,wordList);
            TestingResult testingResult = new TestingResult();
            testingResult.setTrainingDataId(data.getTrainingDataId());
            testingResult.setWordId(data.getWordId());
            testingResult.setResult(predictedWord.getWordId());
            testingResultModel.insertTestingResult(testingResult);
        }
        Log.d("Hangeul","Testing Data Done");
    }

    public Word PredictHangeul(double[] imgFeatures, List<Weight> weightList, List<Word> wordList){
        //
        double[] distance = new double[wordList.size()];
        for(int i=0;i<wordList.size();i++){
            distance[i] = euclideanDistance(imgFeatures,weightList.get(i).getValue());
        }
        int smallestIndex = getSmallestIndex(distance);
        return wordList.get(smallestIndex);
    }

    public Weight getUpdatedWeight(double[]imgFeatures, Weight weight, Boolean isCorrectedPrediction){
        Weight updatedWeight = weight;
        double [] tmp = new double[imgFeatures.length];
        for(int i=0;i<imgFeatures.length;i++){
            double oldValue = weight.getValue()[i];
            double newValue = 0;
            if(isCorrectedPrediction){
                newValue = oldValue + LEARNING_RATE*(imgFeatures[i] - oldValue);
            }
            else{
                newValue = oldValue - LEARNING_RATE*(imgFeatures[i] - oldValue);
            }
            tmp[i] = newValue;
        }
        updatedWeight.setValue(tmp);
        return updatedWeight;
    }

    public double euclideanDistance(double[]data, double[]weights){
        double distance = 0;

        for(int i=0;i<data.length;i++){
            double tmp = Math.pow(data[i]-weights[i],2);
            distance += tmp;
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    public int getSmallestIndex(double[]data){
        int idx = 0;
        double pointer = data[0];
        for(int i=1;i<data.length;i++){
            if(data[i]<=pointer){
                idx = i;
                pointer = data[i];
            }
        }
        return idx;
    }

}
