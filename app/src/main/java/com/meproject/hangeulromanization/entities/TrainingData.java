package com.meproject.hangeulromanization.entities;

import java.sql.Blob;

public class TrainingData {
    int trainingDataId;
    int wordId;
    byte[] image;

    public int getTrainingDataId() {
        return trainingDataId;
    }

    public void setTrainingDataId(int trainingDataId) {
        this.trainingDataId = trainingDataId;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
