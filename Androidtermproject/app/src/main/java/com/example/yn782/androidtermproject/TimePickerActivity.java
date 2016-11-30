package com.example.yn782.androidtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by yn782 on 2016-11-18.
 */

public class TimePickerActivity extends AppCompatActivity {
    TimePicker tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settime_activity);
        Intent intent = getIntent();
        Button okbtn =(Button)findViewById(R.id.timeok);
        Button endbtn =(Button)findViewById(R.id.timecancel);
        tp=(TimePicker)findViewById(R.id.timepicker);
        tp.setCurrentHour(23);
        setTitle(intent.getStringExtra("title")); // 시작시간 or 종료시간

        //시간지정 완료
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentre = new Intent();
                intentre.putExtra("rehour",tp.getCurrentHour());
                intentre.putExtra("reminute",tp.getCurrentMinute());
                setResult(RESULT_OK,intentre);
                finish();
            }
        });
        //타임피커 종료
        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}
