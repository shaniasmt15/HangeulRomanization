package com.meproject.hangeulromanization.controller;

import android.provider.ContactsContract;

import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.TrainingDataModel;
import com.meproject.hangeulromanization.model.WeightModel;
import com.meproject.hangeulromanization.model.WordModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainingDataController {

    TrainingDataModel trainingDataModel = new TrainingDataModel();
    WordModel wordModel= new WordModel();


    public List<TrainingData[]> getTrainingDataList(List<Word> wordList ) throws SQLException{

        List<TrainingData[]> dataList = new ArrayList<TrainingData[]>();
        for(Word word:wordList){
            TrainingData[] data = trainingDataModel.selectTrainingData(word);
            dataList.add(data);
        }

        return dataList;
    }

    public void saveTrainingData(TrainingData data) throws SQLException{
        trainingDataModel.insertTrainingData(data);
    }

    public Boolean dataIsEmpty() throws SQLException{
        int count = trainingDataModel.countTrainingData();
        if(count==0) return true;
        else return false;
    }

}
