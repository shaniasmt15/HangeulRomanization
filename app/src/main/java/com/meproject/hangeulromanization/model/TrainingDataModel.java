package com.meproject.hangeulromanization.model;

import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Word;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrainingDataModel {

    public void insertTrainingData(TrainingData trainingData) throws SQLException{
        Connection conn = Database.getConnection();

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO hangeul_training_datas(word_id,image) VALUES (?,?)");
        preparedStatement.setInt(1,trainingData.getWordId());
        preparedStatement.setBytes(2,trainingData.getImage());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        conn.close();
    }

    public TrainingData[] selectTrainingData(Word word) throws SQLException {
        Connection conn = Database.getConnection();

        List<TrainingData> dataList = new ArrayList<TrainingData>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hangeul_training_datas WHERE word_id="+word.getWordId());
        while (resultSet.next()){
            TrainingData trainingData = new TrainingData();
            trainingData.setTrainingDataId(resultSet.getInt("training_data_id"));
            trainingData.setWordId(resultSet.getInt("word_id"));
            trainingData.setImage(resultSet.getBytes("image"));
            dataList.add(trainingData);
        }

        TrainingData[] data = new TrainingData[dataList.size()];
        dataList.toArray(data);

        resultSet.close();
        conn.close();

        return data;
    }

    public int countTrainingData() throws SQLException{
        Connection conn = Database.getConnection();

        int count = 0;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM hangeul_training_datas");
        while(resultSet.next()){
            count = resultSet.getInt("count");
        }

        statement.close();
        resultSet.close();
        conn.close();

        return count;
    }

}
