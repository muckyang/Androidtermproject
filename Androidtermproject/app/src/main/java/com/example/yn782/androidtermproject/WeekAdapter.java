package com.example.yn782.androidtermproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class WeekAdapter extends BaseAdapter{
    Context mContext;
    private int mResource;
    String schedulelist[]={" "," "," "," "};
    String scheduletimelist[]={"","","",""};
    String [][]titlename=new String[4][7];
    String [][]timename=new String[4][7];
    int dayofnowmonth;
    int dayte;
    int seven=7;
    ArrayList<WeekItem> item;
    int myear,mmonth,sday,NOM;
    DBHandler dbhands;
    TextView tv;
    View dialogView;
    TextView sc1,sc2,sc3,sc4,sct1,sct2,sct3,sct4;
    int i ;
    WeekFragment fragment2 = new WeekFragment();

    WeekAdapter(Context c ,int resource ,ArrayList<WeekItem> items, DBHandler dbhand,int year , int month ,int day,int numofmonth){
        mContext= c;
        mResource =resource;
        item=items;
        dbhands=dbhand;
        myear=year;
        sday=day;
        NOM=numofmonth;
        mmonth=month;

    }
    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        dayte= position+sday;//현재날짜 + 포지션값

        for(int l=0;l<3;l++) {
            schedulelist[l] ="";
            scheduletimelist[l]="";
        }
        int i =0;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);
        }
        tv= (TextView) convertView.findViewById(R.id.dayofweek1);

        sc1 = (TextView) convertView.findViewById(R.id.weeksc1);
        sc2 = (TextView) convertView.findViewById(R.id.weeksc2);
        sc3 = (TextView) convertView.findViewById(R.id.weeksc3);
        sc4 = (TextView) convertView.findViewById(R.id.weeksc4);
        sct1 = (TextView)convertView.findViewById(R.id.wsctime1);
        sct2 = (TextView)convertView.findViewById(R.id.wsctime2);
        sct3 = (TextView)convertView.findViewById(R.id.wsctime3);
        sct4 = (TextView)convertView.findViewById(R.id.wsctime4);
        if(dayte>NOM){ //다음달로 넘어갈 경우
            System.out.println(dayte+"   "+NOM);
            dayofnowmonth=(dayte - NOM);//이번달 남은일수
            tv.setText(dayofnowmonth + "일");
        }else {//이번달 내에 해결될 경우
            tv.setText(dayte + "일");
        }


            String sql2;
            if(dayte>NOM){
                if(mmonth==12) {//12월 1월 같이 보일경우 1월에 대한 쿼리문
                    sql2 = "Select * FROM ex12 WHERE year=" + myear + " AND month=" + 1 + " AND day=" + (dayofnowmonth);
                }else{//월 경계있을경우 쿼리문
                    sql2 = "Select * FROM ex12 WHERE year=" + myear + " AND month=" + (mmonth + 1) + " AND day=" + (dayofnowmonth);
                }
            }else{//일반 쿼리문
                sql2 = "Select * FROM ex12 WHERE year=" + myear + " AND month=" + mmonth + " AND day=" + (position + sday);
            }
            Cursor cursor = dbhands.getReadableDatabase().rawQuery(sql2, null); // dbhandler는 어차피 공유하므로 그대로 사용
            while (cursor.moveToNext()) { // db내용 가져옴/Item에 저장
                if(i<4) {
                schedulelist[i]=cursor.getString(1);
                scheduletimelist[i]=cursor.getString(8);
                    i++;
                }
            }
        ///쿼리문 돌린 결과 (타이틀 최대 4개 저장)
            sc1.setText(schedulelist[0]);
            sc2.setText(schedulelist[1]);
            sc3.setText(schedulelist[2]);
            sc4.setText(schedulelist[3]);
        sct1.setText(scheduletimelist[0]);
        sct2.setText(scheduletimelist[1]);
        sct3.setText(scheduletimelist[2]);
        sct4.setText(scheduletimelist[3]);


        //제목명을 2차원배열에 저장
        titlename[0][position]=(String) sc1.getText();
        titlename[1][position]=(String) sc2.getText();
        titlename[2][position]=(String) sc3.getText();
        titlename[3][position]=(String) sc4.getText();
        timename[0][position]=(String) sct1.getText();
        timename[1][position]=(String) sct2.getText();
        timename[2][position]=(String) sct3.getText();
        timename[3][position]=(String) sct4.getText();
        System.out.println(timename[1][position]);
        tv.setOnClickListener(new View.OnClickListener() { // 일정추가 액티비티 실행
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,addScheduleActivity.class);
                intent.putExtra("year",myear);
                if((position + sday)>NOM) {
                    if(mmonth==12) {
                        intent.putExtra("month", 1);
                        intent.putExtra("day", (position + sday - NOM));
                    }else{
                        intent.putExtra("month",mmonth+1);
                        intent.putExtra("day", (position + sday - NOM));
                    }
                }else{
                    intent.putExtra("day", position + sday);
                    intent.putExtra("month",mmonth);
                }
                mContext.startActivity(intent);
            }
        });
        sc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickwsc(0,position);
            }

        });
        sc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickwsc(1,position);
            }
        });
        sc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickwsc(2,position);
            }
        });
        sc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickwsc(3,position);
            }
        });
        sc1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongclick(v,0,position);
                return true;
            }
        });
        sc2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongclick(v,1,position);
                return true;
            }
        });
        sc3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongclick(v,2,position);
                return true;
            }
        });
        sc4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongclick(v,3,position);
                return true;
            }
        });
        tvsetbackcolor(tv,position);//일별 색상설정

        return convertView;
    }

    //일정 클릭시 수정 및 조회
    public void onclickwsc(int index,int position){
        if(!titlename[index][position].equals("")){
            String wdate="";
            String wtitle="";
            String wstime="";
            String wetime="";
            String wplace="";
            String wnowtime="";
            String sql = String.format(
                    "Select * FROM ex12 WHERE nowtime='%s'",timename[index][position]);
            Cursor cursor = dbhands.getReadableDatabase().rawQuery(sql, null);

            while (cursor.moveToNext()){
                wdate = cursor.getString(5) + "년" + cursor.getString(6) + "월" + cursor.getString(7) + "일";
                wtitle=cursor.getString(1);
                wstime = cursor.getString(2);
                wetime =cursor.getString(3);
                wplace =cursor.getString(4);
                wnowtime = cursor.getString(8);
            }
            Intent intent = new Intent(mContext, ScheActivity.class);
            intent.putExtra("Date", wdate);
            intent.putExtra("Title", wtitle);
            intent.putExtra("sTime", wstime);
            intent.putExtra("eTime", wetime);
            intent.putExtra("Place", wplace);
            intent.putExtra("NowTime", wnowtime);
            mContext.startActivity(intent);
        }

    }
    //일정 삭제 이벤트
    public void onlongclick(final View v ,int index, final int position){
        if(!titlename[index][position].equals("")) {
            dialogView = (View) View.inflate(mContext,R.layout.delete_dialog,null);
            AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
            final String time1 = timename[index][position];
            dlg.setTitle("일정삭제하기");
            dlg.setView(dialogView);
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog ,int i){
                    String sql = String.format(
                            "DELETE FROM ex12 WHERE nowtime='%s'",time1);
                    dbhands.getWritableDatabase().execSQL(sql);
                    
                }
            });
            dlg.setNegativeButton("취소",null);
            dlg.show();
        }
    }
    //색상설정 및 요일추가
    public void tvsetbackcolor(TextView tv,int position){
        switch (position){
            case 0:
                tv.setBackgroundColor(Color.RED);
                tv.setText(tv.getText()+"(일)");
                break;
            case 1:
                tv.setBackgroundColor(Color.parseColor("#FFB85A"));
                tv.setText(tv.getText()+"(월)");
                break;
            case 2:
                tv.setBackgroundColor(Color.parseColor("#FFE400"));
                tv.setText(tv.getText()+"(화)");
                break;
            case 3:
                tv.setBackgroundColor(Color.parseColor("#86E57F"));
                tv.setText(tv.getText()+"(수)");
                break;
            case 4:
                tv.setBackgroundColor(Color.parseColor("#6B66FF"));
                tv.setText(tv.getText()+"(목)");
                break;
            case 5:
                tv.setBackgroundColor(Color.parseColor("#8041D9"));
                tv.setText(tv.getText()+"(금)");
                break;
            case 6:
                tv.setBackgroundColor(Color.parseColor("#F361DC"));
                tv.setText(tv.getText()+"(토)");
                break;
        }
    }


}

class WeekItem {

    String weekofday;
    String sc1;
    String sc2;
    String sc3;
    String sc4;


    WeekItem(String weekday,String schedule1,String schedule2,String schedule3,String schedule4) {

        weekofday = weekday;
        sc1=schedule1;
        sc2=schedule2;
        sc3=schedule3;
        sc4=schedule4;

    }
}
