package com.example.yn782.androidtermproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-29.
 */

public class DayAdapter extends BaseAdapter{
    Context mContext;
    private int mResource;
    private ArrayList<DaylistItem> mItems= new ArrayList<DaylistItem>();
    DBHandler dbhands;
    int dyear,dmonth,dday;
    final AlertDialog.Builder dlg ;

    DayAdapter(Context c , int resource , ArrayList<DaylistItem> items, DBHandler dbhand, int year , int month , int day){
        mContext= c;
        mResource =resource;
        mItems=items;
        dbhands=dbhand;
        dyear=year;
        dmonth=month;
        dday=day;
        dlg = new AlertDialog.Builder(mContext);

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
        if(position%5==0){
            convertView.setBackgroundColor(Color.parseColor("#FFC19E"));
        }else if (position%5==1){
            convertView.setBackgroundColor(Color.parseColor("#FAED7D"));
        }else if(position%5==2){
            convertView.setBackgroundColor(Color.parseColor("#D9E5FF"));
        }else if(position%5==3){
            convertView.setBackgroundColor(Color.parseColor("#FFD9FA"));
        }else if(position%5==4){
            convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }
        TextView title = (TextView) convertView.findViewById(R.id.dtitle);
        title.setText(mItems.get(position).title);

        TextView dutime = (TextView) convertView.findViewById(R.id.ddutime);
        dutime.setText(mItems.get(position).dutime);
        if(dutime.getText().equals("시작시간~종료시간")){
            dutime.setText("시간미정");
        }
        TextView place = (TextView) convertView.findViewById(R.id.dplace);
        place.setText(mItems.get(position).place);
        if(place.getText().equals("")){
            place.setText("장소미정");
        }

        TextView dnowtime = (TextView) convertView.findViewById(R.id.dnowtime);
        dnowtime.setText(mItems.get(position).dnowtime);

        return convertView;

    }
}



class DaylistItem{

    String title;
    String dutime,place,dnowtime;

        DaylistItem(String dtitle, String ddutime,String dplace,String nowtime) {
        //시간,장소데이터 가져오기는 하지만 layout에서 Gone으로 주어 보이지 않음
        title = dtitle;
        dutime = ddutime;
        place = dplace;
        dnowtime=nowtime;

    }
}