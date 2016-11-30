package com.example.yn782.androidtermproject;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yn782 on 2016-11-17.
 */

public class addScheduleActivity extends AppCompatActivity{
    String hourStr;
    String minuteStr;
    Date date;//현재시간
    long now = System.currentTimeMillis();
    int hour,minute;
    //TextView textView;
    int year,month,day;
    TextView tv1;
    EditText ettitle1;
    Button stimebtn1;
    Button etimebtn1;
    EditText etplace1;
    Button memobtn1;
    Button addbtn1;
    Button closebtn1;
    DBHandler dbhand;
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        setTitle("일정추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        tv1 = (TextView)findViewById(R.id.tvdate);
        ettitle1 = (EditText)findViewById(R.id.ettitle);
        stimebtn1 = (Button)findViewById(R.id.stimebtn);
        etimebtn1 = (Button)findViewById(R.id.etimebtn);
        etplace1 = (EditText)findViewById(R.id.etplace);
        memobtn1 = (Button)findViewById(R.id.memobtn);
        addbtn1 = (Button)findViewById(R.id.editbtn);
        closebtn1 = (Button)findViewById(R.id.closebtn);
        Intent intent = getIntent();
        year=intent.getIntExtra("year",0);
        month=intent.getIntExtra("month",0);
        day=intent.getIntExtra("day",0);

        dbhand =new DBHandler(getApplicationContext());
        tv1.setText(year+"년"+month+"월"+day+"일");
        memobtn1.setText("메모");
        addbtn1.setText("일정추가");

        stimebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title ="시작시간";
                Intent intent =new Intent(getApplicationContext(),TimePickerActivity.class);
                intent.putExtra("title",title);
                startActivityForResult(intent,0); // 시작시간은 resultcode = 0
            }
        });
        etimebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title ="종료시간";
                Intent intent =new Intent(getApplicationContext(),TimePickerActivity.class);
                intent.putExtra("title",title);
                startActivityForResult(intent,1);//종료시간은 resultcode = 1
            }
        });
        addbtn1.setOnClickListener(new View.OnClickListener() {// 일정추가!
            @Override
            public void onClick(View v) {
                date = new Date(now);//DB에 input시킬때 시간받아서 동일데이터 없도록 만듬
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String strNow = sdfNow.format(date);
                try {
                 String sql = String.format(
                   "INSERT INTO ex12 (PID, title, stime, endtime, place, year, month, day,nowtime)\n" +
                           "VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s')",
                         ettitle1.getText(), stimebtn1.getText(),etimebtn1.getText(),etplace1.getText(),year,month,day,strNow);
                  dbhand.getWritableDatabase().execSQL(sql);
                }catch (SQLException e){
                    Log.e("SQLite ","insert error");
                }

                finish();
            }
        });

        closebtn1.setOnClickListener(new View.OnClickListener() { // 앱 종료
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //타임피커 결과값 받아오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != requestCode || data == null)
            return;
        hour=data.getIntExtra("rehour",0);
        minute =data.getIntExtra("reminute",0);
        if(requestCode==0) { // 시작시간 선택했을 때
            if(hour<10) hourStr= "0"+hour; // 0~9시 일경우
            else hourStr =""+hour;

            if(minute<10) minuteStr= "0"+minute;// 0~9분 일경우
            else minuteStr =""+minute;

            stimebtn1.setText(hourStr + ":" + minuteStr);
        }else if(requestCode==1) { // 종료시간 선택했을 때

            if(hour<10) hourStr= "0"+hour; // 0~9시 일경우
            else hourStr =""+hour;

            if(minute<10) minuteStr= "0"+minute;// 0~9분 일경우
            else minuteStr =""+minute;

            etimebtn1.setText(hourStr + ":" + minuteStr);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
