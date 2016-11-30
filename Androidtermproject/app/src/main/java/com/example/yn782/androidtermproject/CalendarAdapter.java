package com.example.yn782.androidtermproject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-08.
 */

public class CalendarAdapter extends BaseAdapter {
    Context mcontext;
    private int mResource;
    int myear,mmonth;
    DBHandler dbhands;
    int sday;
    String Arraylist[]={"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31"};
    String schedulelist[]={" "," "," "};
    private ArrayList<MonthItem> mItems= new ArrayList<MonthItem>();
    int ADDNUM;
    int mday;

    public CalendarAdapter(Context c ,int re ,ArrayList<MonthItem> item , DBHandler dbhand,int year, int month , int dday , int NOM) {
        mcontext =c;
        mResource = re;
        mItems = item;
        dbhands= dbhand;
        myear=year; // 현재년도
        mmonth=month;//현재월
        mday=NOM;//현재 월의 일수
        ADDNUM = dday;//이전 월 만큼의 공백
    }
    @Override
    public int getCount() {
        return mday+ADDNUM;//현재 월의 일수 + 이전월의 공백
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position+ADDNUM;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView, sc1, sc2, sc3;

        for(int l=0;l<3;l++) {
            schedulelist[l] =" ";
        }
            int i =0;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);

        textView = (TextView) convertView.findViewById(R.id.gdtv);
        sc1 = (TextView) convertView.findViewById(R.id.sctv1);
        sc2 = (TextView) convertView.findViewById(R.id.sctv2);
        sc3 = (TextView) convertView.findViewById(R.id.sctv3);

        if (position >= ADDNUM) {
            try {
               // String sql2 = "Select * FROM ex11 WHERE year=" + myear + " AND month=" + mmonth + "AND day=" + (position - ADDNUM); // AND앞 띄어쓰기 유의
                String sql2 = "Select * FROM ex12 WHERE year=" + myear + " AND month="+mmonth +" AND day="+(position-ADDNUM+1);
                Cursor cursor = dbhands.getReadableDatabase().rawQuery(sql2, null); // dbhandler는 어차피 공유하므로 그대로 사용
                while (cursor.moveToNext()) { // db내용 가져옴/Item에 저장
                    //System.out.println(cursor.getString(1));
                    if(i<3) {
                    schedulelist[i]=cursor.getString(1);
                        i++;
                    }
                }
                sc1.setText(schedulelist[0]);
                sc2.setText(schedulelist[1]);
                sc3.setText(schedulelist[2]);
                if(!sc1.getText().equals(" ")){
                    if(position%2==0)
                        sc1.setBackgroundColor(Color.parseColor("#FFD9EC"));
                    else
                        sc1.setBackgroundColor(Color.parseColor("#FAF4C0"));
                }if(!sc2.getText().equals(" ")){
                    if(position%2==0)
                        sc2.setBackgroundColor(Color.parseColor("#D4F4FA"));
                    else
                        sc2.setBackgroundColor(Color.parseColor("#E4F7BA"));
                }if(!sc3.getText().equals(" ")){
                    if(position%2==0)
                        sc3.setBackgroundColor(Color.parseColor("#FFD8D8"));
                    else
                        sc3.setBackgroundColor(Color.parseColor("#DAD9FF"));
                }


            }catch (SQLException e){
                Log.e("SQLite ","update error");
            }
            textView.setText(Arraylist[position - ADDNUM]);//날짜 설정
            if ((position) % 7 == 0) {//일요일
                textView.setTextColor(Color.RED);
            } else if ((position) % 7 == 6) {//토요일
                textView.setTextColor(Color.BLUE);
            } else {
                textView.setTextColor(Color.BLACK);
            }
        } else {//이전 월은 공백으로 표기
            textView.setText(" ");
            textView.setTextSize(25);
        }
    }
        return convertView;
    }


}
class MonthItem {

    String daynum;
    String sc1,sc2,sc3;
    MonthItem(String daynumber, String scheduleText1,String scheduleText2, String scheduleText3) {
        //시간,장소데이터 가져오기는 하지만 layout에서 Gone으로 주어 보이지 않음
        daynum = daynumber;
        sc1 = scheduleText1;
        sc2 = scheduleText2;
        sc3 = scheduleText3;
    }
}