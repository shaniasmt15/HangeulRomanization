package com.meproject.hangeulromanization.controller;

import android.util.Log;

import com.meproject.hangeulromanization.entities.TrainingData;

import java.util.ArrayList;
import java.util.List;

public class DataPartition {
    int k;
    int kIdx;
    List<TrainingData> firstList = new ArrayList<TrainingData>();
    List<TrainingData> trainingList = new ArrayList<TrainingData>();
    List<TrainingData> validationList = new ArrayList<TrainingData>();

    public DataPartition(int k, int kIdx){
        this.k = k;
        this.kIdx = kIdx;
    }

    public int[] getIndex(TrainingData[] data){
        int dataCount = data.length;
        int d = dataCount/k;
        //Log.d("Hangeul","d: "+dataCount);
        int[] idx = new int[2];
        idx[0] = (kIdx-1)*d;
        idx[1] = kIdx*d;
        return idx;
    }

    public void divideData(List<TrainingData[]> dataList){
        for(TrainingData[] data:dataList){
            int[] idx = getIndex(data);
            //Log.d("DataPartition",idx[0]+" "+idx[1]);
            for(int i=0;i<data.length;i++){
                if(i>=idx[0] && i<idx[1]){
                    validationList.add(data[i]);
                }
                else if(kIdx==1 && i==idx[1]){
                    firstList.add(data[i]);
                }
                else if(kIdx>1 && i==0){
                    firstList.add(data[i]);
                }
                else{
                    trainingList.add(data[i]);
                }
            }
        }
    }

    public List<TrainingData> getFirstList() {
        return firstList;
    }

    public List<TrainingData> getTrainingList() {
        return trainingList;
    }

    public List<TrainingData> getValidationList() {
        return validationList;
    }
}
