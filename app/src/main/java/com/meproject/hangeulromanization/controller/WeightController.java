package com.meproject.hangeulromanization.controller;

import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.WeightModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeightController {

    ImageProcessing imageProcessing = new ImageProcessing();
    WeightModel weightModel = new WeightModel();

    public void initializingWeight(List<TrainingData> firstData) throws SQLException {
        Boolean isEmptyWeight = weightModel.countWeight()==0;
        for(TrainingData data:firstData){
            double[] imgFeatures = imageProcessing.getExtractedFeature(Converter.ByteArraytoBitmap(data.getImage()));
            Weight weight = new Weight();
            weight.setWordId(data.getWordId());
            weight.setValue(imgFeatures);
            if(isEmptyWeight) weightModel.insertWeight(weight);
            else weightModel.updateWeight(weight);
        }
    }

    public List<Weight> getWeightList() throws SQLException{
        List<Weight> weightList = weightModel.selectAllWeight();
        return weightList;
    }

}
