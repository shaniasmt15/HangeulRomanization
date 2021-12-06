package com.meproject.hangeulromanization.model;

import com.meproject.hangeulromanization.entities.TestingResult;
import com.meproject.hangeulromanization.entities.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestingResultModel {

    public void insertTestingResult(TestingResult testingResult) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO hangeul_testing_results(training_data_id,word_id,result) VALUES(?,?,?)");
        preparedStatement.setInt(1,testingResult.getTrainingDataId());
        preparedStatement.setInt(2,testingResult.getWordId());
        preparedStatement.setInt(3,testingResult.getResult());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        conn.close();
    }

    public List<TestingResult> selectAllTestingResult() throws SQLException{
        List<TestingResult> testingResultList = new ArrayList<TestingResult>();

        Connection conn = Database.getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hangeul_testing_results");
        //System.out.println("yee1");
        //data += "id"+"\t"+"hangeul"+"\t"+"read"+"\n";
        while(resultSet.next()){
            TestingResult testingResult = new TestingResult();
            testingResult.setTestingResultid(resultSet.getInt("testing_result_id"));
            testingResult.setTrainingDataId(resultSet.getInt("training_data_id"));
            testingResult.setWordId(resultSet.getInt("word_id"));
            testingResult.setResult(resultSet.getInt("result"));
            testingResultList.add(testingResult);
        }

        resultSet.close();
        conn.close();

        return testingResultList;
    }
}
