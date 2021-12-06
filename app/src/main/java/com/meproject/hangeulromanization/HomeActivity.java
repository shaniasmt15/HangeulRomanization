package com.meproject.hangeulromanization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Word;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    CardView predictBtn, addBtn, dictionaryBtn, trainingBtn, helpBtn;
    List<Word> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        predictBtn =findViewById(R.id.homePredictBtn);
        addBtn = findViewById(R.id.homeAddBtn);
        dictionaryBtn = findViewById(R.id.homeDictionaryBtn);
        trainingBtn = findViewById(R.id.homeTrainBtn);
        helpBtn = findViewById(R.id.homeHelpBtn);

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,PredictActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

        dictionaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,DictionaryActivity.class);
                startActivity(intent);
            }
        });

        trainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"training yee",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HelpActivity.class);
                startActivity(intent);
            }
        });

    }

}