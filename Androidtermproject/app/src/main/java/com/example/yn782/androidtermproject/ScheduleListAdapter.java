package com.example.yn782.androidtermproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-08.
 */

public class ScheduleListAdapter extends BaseAdapter {
    Context mContext;
    private int mResource;
    private ArrayList<MyItem> mItems= new ArrayList<MyItem>();
    int myear,mmonth;

    ScheduleListAdapter(Context c ,  int resource , ArrayList<MyItem> items, int year , int month){
        mContext= c;
        mResource =resource;
        mItems=items;
        myear=year;
        mmonth=month;
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);
        }

        // 날짜 설정
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(mItems.get(position).nDate);
        // 제목 설정
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(mItems.get(position).nTitle);
        //시간 설정
        TextView durtime = (TextView) convertView.findViewById(R.id.timedur);
        durtime.setText(mItems.get(position).nsTime+"~"+mItems.get(position).neTime);
        if(durtime.getText().equals("시작시간~종료시간")){
            durtime.setText("시간미정");
        }
        Button stime = (Button) convertView.findViewById(R.id.stime);
        stime.setText(mItems.get(position).nsTime);
        Button etime = (Button) convertView.findViewById(R.id.etime);
        stime.setText(mItems.get(position).neTime);
        //장소 설정
        TextView place = (TextView) convertView.findViewById(R.id.place);
        place.setText(mItems.get(position).nPlace);
        if(place.getText().equals("")){
            place.setText("장소미정");
        }
        return convertView;


    }


}
class MyItem {

    String nDate;
    String nTitle;
    String nsTime;
    String neTime;
    String nPlace;
    String nnowTime;
    MyItem(String aDate, String aTitle,String asTime, String aeTime,String aPlace,String anowTime) {
        //시간,장소데이터 가져오기는 하지만 layout에서 Gone으로 주어 보이지 않음
        nDate = aDate;
        nTitle = aTitle;
        nsTime = asTime;
        neTime = aeTime;
        nPlace = aPlace;
        nnowTime = anowTime;
    }
}

