package com.meproject.hangeulromanization.controller;

import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.WordModel;

import java.sql.SQLException;
import java.util.List;

public class WordController {

    WordModel wordModel = new WordModel();

    public List<Word> getAllWords() throws SQLException {
        List<Word> wordList = wordModel.selectAllWords();
        return wordList;
    }

}
