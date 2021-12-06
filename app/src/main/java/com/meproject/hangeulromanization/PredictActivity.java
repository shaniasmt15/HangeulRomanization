package com.meproject.hangeulromanization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meproject.hangeulromanization.controller.Converter;
import com.meproject.hangeulromanization.controller.ImageProcessing;
import com.meproject.hangeulromanization.controller.LearningVectorQuantization;
import com.meproject.hangeulromanization.controller.WeightController;
import com.meproject.hangeulromanization.controller.WordController;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PredictActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    ImageView imgView;
    ImageView backBtn;
    LinearLayout galleryBtn, cameraBtn, rotateBtn, predictBtn;
    TextView hangeulTxt, latinTxt, translateTxt;
    Bitmap imgData;
    double[] imgFeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        getSupportActionBar().hide();

        OpenCVLoader.initDebug();

        imgView = findViewById(R.id.predictImgView);
        galleryBtn = findViewById(R.id.predictGalleryBtn);
        cameraBtn = findViewById(R.id.predictCameraBtn);
        rotateBtn = findViewById(R.id.predictRotateBtn);
        predictBtn = findViewById(R.id.predictConfirmBtn);
        backBtn = findViewById(R.id.predictBackBtn);
        hangeulTxt = findViewById(R.id.predictHangeulTxt);
        latinTxt = findViewById(R.id.predictLatinTxt);
        translateTxt = findViewById(R.id.predictTranslateTxt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PredictActivity.this,HomeActivity.class);
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
                    Toast.makeText(PredictActivity.this,"Silahkan pilih gambar terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
                else{
                    ImageProcessing imageProcessing = new ImageProcessing();
                    imgData = Converter.MatToBitmap(imageProcessing.rotateImage(imgData,90));
                    //imgView.setRotation(degree);
                    //imgData = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                    imgView.setImageBitmap(imgData);
                }
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PredictionTask().execute();
            }
        });

    }


    class PredictionTask extends AsyncTask<Void, Void, Void> {

        String msg="";
        Mat preprocess = new Mat();
        ImageProcessing imageProcessing = new ImageProcessing();
        WeightController weightController = new WeightController();
        WordController wordController = new WordController();
        LearningVectorQuantization lvq = new LearningVectorQuantization();

        Bitmap resultImgData; // untuk menampung hasil preprocessing image
        Word predictedWord; //untuk menampung hasil prekdiksi

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                List<Word> wordList = wordController.getAllWords();
                List<Weight> weightList = weightController.getWeightList();

                preprocess = imageProcessing.Preprocess(imgData);
                resultImgData = Converter.MatToBitmap(preprocess);
                double[] imgFeature = imageProcessing.getExtractedFeature(resultImgData);
                predictedWord = lvq.PredictHangeul(imgFeature,weightList,wordList);

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
                //imgView.setImageBitmap(resultImgData);
                hangeulTxt.setTypeface(Typeface.DEFAULT_BOLD);
                hangeulTxt.setText(predictedWord.getWord());
                latinTxt.setTypeface(Typeface.DEFAULT_BOLD);
                latinTxt.setText(predictedWord.getLatin());
                translateTxt.setTypeface(Typeface.DEFAULT_BOLD);
                translateTxt.setText(predictedWord.getTranslate());
            }

        }

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            //openCamera();
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
                Toast.makeText(PredictActivity.this,"Camera Permission is Required to Use Camera",Toast.LENGTH_SHORT).show(); //kalau belum ada permission utk pakai kamera
            }
        }


    }


    private void openCamera() { //untuk menampilkan tampilan camera
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