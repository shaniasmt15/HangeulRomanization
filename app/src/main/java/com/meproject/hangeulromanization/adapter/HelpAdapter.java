package com.meproject.hangeulromanization.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.meproject.hangeulromanization.R;

import java.util.ArrayList;

public class HelpAdapter extends PagerAdapter {

    Context context;
    int [] imageId;
    String [] title;

    public HelpAdapter(Context context, int[] imageId, String[] title){
        this.context = context;
        this.imageId = imageId;
        this.title = title;
    }

    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.help_item, container, false);

        ImageView imgView = view.findViewById(R.id.itemHelpImgView);
        TextView titleTxt = view.findViewById(R.id.itemHelpTitle);

        imgView.setImageResource(imageId[position]);
        titleTxt.setText(title[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
