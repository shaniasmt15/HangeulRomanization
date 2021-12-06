package com.meproject.hangeulromanization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.meproject.hangeulromanization.adapter.WordAdapter;
import com.meproject.hangeulromanization.controller.WordController;
import com.meproject.hangeulromanization.entities.Word;
import com.meproject.hangeulromanization.model.Database;
import com.meproject.hangeulromanization.model.WordModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DictionaryActivity extends AppCompatActivity {

    GridView gv_words;
    ImageView backBtn;
    List<Word> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        getSupportActionBar().hide();

        gv_words = findViewById(R.id.dictionary_gv_words);
        backBtn = findViewById(R.id.dictionaryBackBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DictionaryActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        new WordTask().execute();

    }

    class WordTask extends AsyncTask<Void, Void, Void> {
        String msg = "";
        WordController wordController = new WordController();
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                wordList = wordController.getAllWords();
            }
            catch(Exception e){
                msg = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (msg!="")
                Log.d("Hangeul",msg);
            else{
                WordAdapter wordAdapter = new WordAdapter(DictionaryActivity.this,wordList);
                gv_words.setAdapter(wordAdapter);
            }
        }

    }
}