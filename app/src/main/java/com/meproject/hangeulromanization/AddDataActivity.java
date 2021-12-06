package com.meproject.hangeulromanization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.meproject.hangeulromanization.controller.Converter;
import com.meproject.hangeulromanization.controller.ImageProcessing;
import com.meproject.hangeulromanization.controller.TrainingDataController;
import com.meproject.hangeulromanization.controller.WordController;
import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Word;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddDataActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    LinearLayout galleryBtn, cameraBtn, rotateBtn, nextBtn;
    ImageView backBtn;
    Spinner wordSpinner;
    ImageView imgView;
    Bitmap imgData, resultImgData;
    TextView translateTxt;

    Dialog confirmDialog;
    Button cancelBtn, saveBtn;
    TextView dialogHangeulTxt, dialogTranslateTxt;
    ImageView dialogImgView;

    List<Word> wordList = new ArrayList<Word>();
    List<String> spinnerList = new ArrayList<String>();

    Word selectedWord=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        getSupportActionBar().hide();
        OpenCVLoader.initDebug();

        galleryBtn = findViewById(R.id.addGalleryBtn);
        cameraBtn = findViewById(R.id.addCameraBtn);
        rotateBtn = findViewById(R.id.addRotateBtn);
        nextBtn = findViewById(R.id.addNextBtn);
        backBtn = findViewById(R.id.addBackBtn);
        wordSpinner = findViewById(R.id.addWordSpinner);
        imgView = findViewById(R.id.addImgView);
        translateTxt = findViewById(R.id.addTranslateTxt);

        new SelectWordsTask().execute();
        wordSpinner.setAdapter(new ArrayAdapter<>(AddDataActivity.this, android.R.layout.simple_spinner_dropdown_item,spinnerList));


        wordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWord = wordList.get(position);
                translateTxt.setText(selectedWord.getTranslate());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
         });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDataActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery,GALLERY_REQUEST_CODE);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgData==null){
                    Toast.makeText(AddDataActivity.this,"Silahkan pilih gambar terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
                else{
                    ImageProcessing imageProcessing = new ImageProcessing();
                    imgData = Converter.MatToBitmap(imageProcessing.rotateImage(imgData,90));
                    imgView.setImageBitmap(imgData);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWord == null && imgData==null){
                    Toast.makeText(AddDataActivity.this,"Silahkan pilih gambar dan hangeul terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(selectedWord == null){
                    Toast.makeText(AddDataActivity.this,"Silahkan pilih hangeul terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else if(imgData==null){
                    Toast.makeText(AddDataActivity.this,"Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else{
                    new ImageProcessingTask().execute();
                }
            }
        });

    }

    public void confirmAlertDialog(){
        confirmDialog = new Dialog(AddDataActivity.this);
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setContentView(R.layout.dialog_add_data);
        confirmDialog.setTitle("Saving data?");
        cancelBtn = confirmDialog.findViewById(R.id.dialogCancelBtn);
        saveBtn = confirmDialog.findViewById(R.id.dialogSaveBtn);
        dialogHangeulTxt = confirmDialog.findViewById(R.id.dialogHangeulTxt);
        dialogTranslateTxt = confirmDialog.findViewById(R.id.dialogTranslateTxt);
        dialogImgView = confirmDialog.findViewById(R.id.dialogImgView);

        dialogImgView.setImageBitmap(resultImgData);
        dialogHangeulTxt.setText(selectedWord.getLatin()+" ("+selectedWord.getWord()+")");
        dialogTranslateTxt.setText(selectedWord.getTranslate());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertTrainingDataTask().execute();
                confirmDialog.cancel();
                Intent intent = new Intent(AddDataActivity.this,AddDataActivity.class);
                startActivity(intent);
            }
        });

        confirmDialog.show();

    }


    class ImageProcessingTask extends AsyncTask<Void, Void, Void> {

        String msg="";
        Mat preprocess = new Mat();
        ImageProcessing imageProcessing = new ImageProcessing();

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                preprocess = imageProcessing.Preprocess(imgData);
                resultImgData = Converter.MatToBitmap(preprocess);
            }
            catch(Exception e){
                msg = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            confirmAlertDialog();
        }

    }


    class SelectWordsTask extends AsyncTask<Void, Void, Void> {
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
                for (Word word : wordList) {
                    String tmpString = word.getLatin() + " (" + word.getWord() + ")";
                    spinnerList.add(tmpString);
                }
            }

        }

    }

    class InsertTrainingDataTask extends AsyncTask<Void, Void, Void> {
        String msg = "";
        TrainingDataController trainingDataController = new TrainingDataController();
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                ImageProcessing imageProcessing = new ImageProcessing();
                TrainingData trainingData = new TrainingData();
                trainingData.setWordId(selectedWord.getWordId());
                trainingData.setImage(Converter.BitmapToByteArray(resultImgData));
                trainingDataController.saveTrainingData(trainingData);
            }
            catch(Exception e){
                msg = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(msg==""){
                Toast.makeText(AddDataActivity.this,"Insert Data: Success!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddDataActivity.this,"Insert Data: Failed!",Toast.LENGTH_SHORT).show();
                Log.d("Hangeul",msg);
            }
        }

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            openCamera();
        }
        else{
            openCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else{
                Toast.makeText(this,"Camera Permission is Required to Use Camera",Toast.LENGTH_SHORT).show(); //kalau belum ada permission utk pakai kamera
            }
        }


    }


    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,  CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) { //kalau sama dg 102, maka akan nampilkan gambar ke imageView
            imgData = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(imgData);
        }
        if (requestCode == GALLERY_REQUEST_CODE) { //kalau sama dg 105, maka akan nampilkan gambar ke imageView
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                try{
                    imgData = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                imgView.setImageBitmap(imgData);
            }

        }
    }

}