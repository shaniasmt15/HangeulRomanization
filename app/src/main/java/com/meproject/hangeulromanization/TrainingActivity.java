package com.meproject.hangeulromanization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meproject.hangeulromanization.adapter.WordAdapter;
import com.meproject.hangeulromanization.controller.DataPartition;
import com.meproject.hangeulromanization.controller.LearningVectorQuantization;
import com.meproject.hangeulromanization.controller.TestingResultController;
import com.meproject.hangeulromanization.controller.TrainingDataController;
import com.meproject.hangeulromanization.controller.WeightController;
import com.meproject.hangeulromanization.controller.WordController;
import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.TestingResultModel;

import java.util.ArrayList;
import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    ProgressBar trainProgressBar;
    TextView infoTxt, trainTxt1, trainTxt2;
    Dialog finishDialog;
    Button okBtn;
    LinearLayout trainLayout;

    Boolean dataIsEmpty = true;
    int k=10;
    int kIdx=0;
    double acc_rate = 0;

    List<Word> wordList = new ArrayList<Word>();
    List<Weight> weightList = new ArrayList<Weight>();
    List<TrainingData> firstDataList, trainingDataList, testingDataList = new ArrayList<TrainingData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        getSupportActionBar().hide();

        trainProgressBar = findViewById(R.id.trainingProgressBar);
        trainTxt1 = findViewById(R.id.trainingInfoTxt);
        trainTxt2 = findViewById(R.id.trainingInfoTxt2);
        trainLayout = findViewById(R.id.trainLayout);
        //infoTxt.setText("Training process is in progress, please wait..");
        new TrainingProcess().execute();

    }

    public void doneAlertDialog(){
        finishDialog = new Dialog(TrainingActivity.this);
        finishDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        finishDialog.setContentView(R.layout.dialog_training);
        okBtn = finishDialog.findViewById(R.id.dialogTrainOkBtn);
        infoTxt = finishDialog.findViewById(R.id.dialogTrainAccTxt);

        infoTxt.setText("Testing Accuracy : "+acc_rate*100+"%");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog.cancel();
                Intent intent = new Intent(TrainingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        finishDialog.show();

    }

    class TrainingProcess extends AsyncTask<Void, Void, Void> {
        String msg = "";

        DataPartition dataPartition = new DataPartition(k,kIdx);
        LearningVectorQuantization lvq = new LearningVectorQuantization();

        WordController wordController = new WordController();
        WeightController weightController = new WeightController();
        TrainingDataController trainingDataController = new TrainingDataController();
        TestingResultController testingResultController = new TestingResultController();


        List<TrainingData[]> dataList;

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Log.d("Hangeul","Fetching dataset..");
                wordList = wordController.getAllWords();

                if(trainingDataController.dataIsEmpty()){
                    msg = "Data is empty";
                }
                else{
                    //mengambil semua data train di db
                    dataList = trainingDataController.getTrainingDataList(wordList);
                    //membagi 3 bagian data pertama bobot awal, training dan testing
                    dataPartition.divideData(dataList);
                    //mengambil data pertama bobot awal, training dan testing
                    firstDataList = dataPartition.getFirstList();
                    trainingDataList = dataPartition.getTrainingList();
                    testingDataList = dataPartition.getValidationList();
                    //menginisialisasi atau set bobot awal dari first datalist
                    weightController.initializingWeight(firstDataList);
                    //mengambil nilai bobot
                    weightList = weightController.getWeightList();
                    //dataIsEmpty = false;
                    //dilakukan proses pelatihan lvq
                    lvq.TrainingProcess(trainingDataList,weightList,wordList);
                    //dilakukan proses pengujian lvq
                    lvq.TestingProcess(testingDataList,weightList,wordList);
                    //menampilkan hasi akurasinya
                    acc_rate = testingResultController.getAccuracy();
                }
            }
            catch(Exception e){
                msg = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(msg==""){
                trainLayout.removeAllViews();
                doneAlertDialog();
            }
            else{
                infoTxt.setText(msg);
                Log.d("Hangeul",msg);
            }
        }

    }

}