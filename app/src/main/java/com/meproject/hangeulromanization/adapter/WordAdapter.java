package com.meproject.hangeulromanization.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meproject.hangeulromanization.R;
import com.meproject.hangeulromanization.entities.Word;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Word> wordList;

    public WordAdapter(Context context, List<Word> wordList){
        this.context = context;
        this.wordList = wordList;
    }
    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView==null){
            convertView = inflater.inflate(R.layout.word_item,null);
        }

        TextView word = convertView.findViewById(R.id.word_item_value);
        TextView latin = convertView.findViewById(R.id.word_item_latin);
        TextView translate = convertView.findViewById(R.id.word_item_translate);

        Word tmp = wordList.get(position); //posisi word nya untuk kotak 1 dst..
        word.setText(tmp.getWord());
        latin.setText(tmp.getLatin());
        translate.setText(tmp.getTranslate());
        return convertView;
    }
}
