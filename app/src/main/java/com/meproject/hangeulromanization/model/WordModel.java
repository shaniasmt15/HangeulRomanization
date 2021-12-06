package com.meproject.hangeulromanization.model;

import android.util.Log;

import com.meproject.hangeulromanization.entities.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WordModel {

    public List<Word> selectAllWords() throws SQLException{
        List<Word> wordList = new ArrayList<Word>();

        Connection conn = Database.getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM hangeul_words");
        //System.out.println("yee1");
            //data += "id"+"\t"+"hangeul"+"\t"+"read"+"\n";
        while(resultSet.next()){
            Word word = new Word();
            word.setWordId(resultSet.getInt("word_id"));
            word.setWord(resultSet.getString("word"));
            word.setLatin(resultSet.getString("latin"));
            word.setTranslate(resultSet.getString("translate"));
            wordList.add(word);
        }

        resultSet.close();
        conn.close();

        return wordList;
    }
    
}

