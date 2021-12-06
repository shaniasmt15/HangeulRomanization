package com.meproject.hangeulromanization.model;

import com.meproject.hangeulromanization.controller.Converter;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WeightModel {

    public void insertWeight(Weight weight) throws SQLException {
        Connection conn = Database.getConnection();

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO hangeul_weights(word_id,value) VALUES (?,?)");
        preparedStatement.setInt(1,weight.getWordId());
        preparedStatement.setBytes(2,Converter.DoubleToByteArray(weight.getValue()));
        preparedStatement.executeUpdate();

        preparedStatement.close();
        conn.close();
    }

    public int countWeight() throws SQLException{
        Connection conn = Database.getConnection();

        int count = 0;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM hangeul_weights");
        while(resultSet.next()){
            count = resultSet.getInt("count");
        }

        statement.close();
        resultSet.close();
        conn.close();

        return count;
    }

    public List<Weight> selectAllWeight() throws SQLException{
        Connection conn = Database.getConnection();

        List<Weight> weightList = new ArrayList<Weight>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hangeul_weights ORDER BY word_id ASC");
        while(resultSet.next()){
            Weight weight = new Weight();
            weight.setWeightId(resultSet.getInt("weight_id"));
            weight.setWordId(resultSet.getInt("word_id"));
            weight.setValue(Converter.ByteToDoubleArray(resultSet.getBytes("value")));
            weightList.add(weight);
        }

        statement.close();
        resultSet.close();
        conn.close();

        return weightList;
    }



    public void updateWeight(Weight weight) throws SQLException{
        Connection conn = Database.getConnection();

        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE hangeul_weights SET value=? WHERE word_id=?");
        preparedStatement.setBytes(1,Converter.DoubleToByteArray(weight.getValue()));
        preparedStatement.setInt(2,weight.getWordId());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        conn.close();
    }
    
}
