package com.meproject.hangeulromanization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meproject.hangeulromanization.adapter.HelpAdapter;
import com.meproject.hangeulromanization.controller.Converter;
import com.meproject.hangeulromanization.controller.ImageProcessing;
import com.meproject.hangeulromanization.controller.TrainingDataController;
import com.meproject.hangeulromanization.controller.WeightController;
import com.meproject.hangeulromanization.controller.WordController;
import com.meproject.hangeulromanization.entities.TrainingData;
import com.meproject.hangeulromanization.entities.Weight;
import com.meproject.hangeulromanization.entities.Word;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ViewPager helpViewPager;
    LinearLayout helpBottom;
    HelpAdapter helpAdapter;
    ImageView backBtn;

    TextView[] helpDots;
    int[] imageId;
    String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();

        helpViewPager = findViewById(R.id.helpViewPager);
        helpBottom = findViewById(R.id.helpBottom);
        backBtn = findViewById(R.id.helpBackBtn);

        imageId = new int[]{
                R.drawable.help_menu,
                R.drawable.help_predict,
                R.drawable.help_add,
                R.drawable.help_train,
                R.drawable.help_dictionary
        };

        title = new String[]{
                "FITUR APLIKASI",
                "PENGENALAN HANGEUL",
                "TAMBAH DATA TRAINING",
                "LATIH DATA",
                "KAMUS HANGEUL"
        };

        helpAdapter = new HelpAdapter(this, imageId, title);
        helpViewPager.setAdapter(helpAdapter);
        addDotsIndicator(0);
        helpViewPager.addOnPageChangeListener(viewListener);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void addDotsIndicator(int position) {
        helpDots = new TextView[imageId.length];
        helpBottom.removeAllViews();

        for (int i = 0; i < helpDots.length; i++) {
            helpDots[i] = new TextView(this);
            helpDots[i].setText(Html.fromHtml("&#8226"));
            helpDots[i].setTextSize(35);
            helpDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            helpBottom.addView(helpDots[i]);
        }

        if (helpDots.length > 0) {
            helpDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
