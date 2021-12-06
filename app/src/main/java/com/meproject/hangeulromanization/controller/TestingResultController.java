package com.meproject.hangeulromanization.controller;

import com.meproject.hangeulromanization.entities.TestingResult;
import com.meproject.hangeulromanization.model.TestingResultModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestingResultController {

    TestingResultModel testingResultModel = new TestingResultModel();

    public double getAccuracy() throws SQLException {
        List<TestingResult> testingResultList = testingResultModel.selectAllTestingResult();
        double accuracy = 0;

        for(TestingResult result:testingResultList){
            if(result.getWordId()==result.getResult()){
                accuracy+=1;
            }
        }
        accuracy = accuracy/testingResultList.size();
        return accuracy;
    }

    public void insertAllResult(List<TestingResult> testingResultList) throws SQLException{
        for(TestingResult t: testingResultList){
            testingResultModel.insertTestingResult(t);
        }
    }


}
