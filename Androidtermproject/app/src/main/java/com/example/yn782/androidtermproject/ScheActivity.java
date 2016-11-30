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

/**
 * Created by yn782 on 2016-11-17.
 */

public class ScheActivity extends AppCompatActivity {
   String mdate,mtitle,mstime,metime,mplace,mnowtime;
    String hourStr;
    String minuteStr;
    int year,month,day;
    int hour,minute;
    TextView tvdate;
    EditText ettitle;
    Button stimebtn;
    Button etimebtn;
    EditText etplace;
    Button memobtn;
    Button editbtn;
    Button closebtn;
    DBHandler dbhand;
    String a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTitle("일정편집");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        dbhand = new DBHandler(getApplicationContext());
         tvdate = (TextView)findViewById(R.id.tvdate);
         ettitle = (EditText)findViewById(R.id.ettitle);
         stimebtn = (Button)findViewById(R.id.stimebtn);
         etimebtn = (Button)findViewById(R.id.etimebtn);
         etplace = (EditText)findViewById(R.id.etplace);
         memobtn = (Button)findViewById(R.id.memobtn);
        editbtn = (Button)findViewById(R.id.editbtn);
         closebtn = (Button)findViewById(R.id.closebtn);
        //인텐트로 해당 리스트 뷰 정보가져오기
        Intent intent =getIntent();
        mdate=intent.getStringExtra("Date");
        mtitle = intent.getStringExtra("Title");
        mstime=intent.getStringExtra("sTime");
        metime=intent.getStringExtra("eTime");
        mplace=intent.getStringExtra("Place");
        mnowtime=intent.getStringExtra("NowTime");
        year=intent.getIntExtra("year",0);
        month=intent.getIntExtra("month",0);
        day=intent.getIntExtra("day",0);
        //있던정보 보여주기
        tvdate.setText(mdate);
        ettitle.setText(mtitle);
        stimebtn.setText(mstime);
        etimebtn.setText(metime);
        etplace.setText(mplace);
        editbtn.setText("적용");




        stimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title ="시작시간";
                Intent intent =new Intent(getApplicationContext(),TimePickerActivity.class);
                intent.putExtra("title",title);
                startActivityForResult(intent,0); // 시작시간은 resultcode = 0
            }
        });
        etimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title ="종료시간";
                Intent intent =new Intent(getApplicationContext(),TimePickerActivity.class);
                intent.putExtra("title",title);
                startActivityForResult(intent,1);//종료시간은 resultcode = 1
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
//                        String sql = String.format(
//                                "INSERT INTO ex11 (PID, title, stime, endtime, place, year, month, day,nowtime)\n" +
//                                        "VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s')",
//                                ettitle1.getText(), stimebtn1.getText(),etimebtn1.getText(),etplace1.getText(),year,month,day,strNow);
//                        dbhand.getReadableDatabase().execSQL(sql);   ,,,
                        String sql = String.format("UPDATE ex12 SET title = '%s'  WHERE nowtime='%s'",ettitle.getText(), mnowtime);
                        dbhand.getWritableDatabase().execSQL(sql);
                        String sql2 = String.format("UPDATE ex12 SET stime = '%s'  WHERE nowtime='%s'",stimebtn.getText(), mnowtime);
                        dbhand.getWritableDatabase().execSQL(sql2);
                        String sql3 = String.format("UPDATE ex12 SET endtime = '%s'  WHERE nowtime='%s'",etimebtn.getText(), mnowtime);
                        dbhand.getWritableDatabase().execSQL(sql3);
                        String sql4 = String.format("UPDATE ex12 SET place = '%s'  WHERE nowtime='%s'",etplace.getText(), mnowtime);
                        dbhand.getWritableDatabase().execSQL(sql4);
                        System.out.println(ettitle.getText()+","+mnowtime);
                    }catch (SQLException e){
                        Log.e("SQLite ","update error");
                    }
                    finish();
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
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

            stimebtn.setText(hourStr + ":" + minuteStr);
        }else if(requestCode==1) { // 종료시간 선택했을 때
            if(hour<10) hourStr= "0"+hour; // 0~9시 일경우
            else hourStr =""+hour;

            if(minute<10) minuteStr= "0"+minute;// 0~9분 일경우
            else minuteStr =""+minute;

            etimebtn.setText(hourStr + ":" + minuteStr);
        }
    }
}
