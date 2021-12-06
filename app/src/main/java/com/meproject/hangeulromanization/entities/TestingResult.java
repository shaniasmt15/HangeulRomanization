package com.meproject.hangeulromanization.entities;

public class TestingResult {
    int testingResultid;
    int trainingDataId;
    int wordId;
    int result;

    public int getTestingResultid() {
        return testingResultid;
    }

    public void setTestingResultid(int testingResultid) {
        this.testingResultid = testingResultid;
    }

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
