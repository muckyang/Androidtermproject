package com.example.yn782.androidtermproject;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    static ScheduleListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //기본은 월별로 지정
        setTitle("MONTH");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentTransaction tsction = getSupportFragmentManager().beginTransaction();
        tsction.replace(R.id.fragment,new MonthFragment());//기본 월별로 사용
        tsction.commit();

    }
    //메뉴추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴아이템 클릭시 이벤트(프레그먼트 교체)+ 리스트뷰 교체(해야함)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final FragmentTransaction tsction = getSupportFragmentManager().beginTransaction();
        switch(item.getItemId()){
            case R.id.formonth:
                tsction.replace(R.id.fragment,new MonthFragment());
                tsction.commit();
                setTitle("MONTH");
                return true;
            case R.id.forweek:
                tsction.replace(R.id.fragment,new WeekFragment());
                tsction.commit();
                setTitle("WEEK");
                return true;
            case R.id.forday:
                tsction.replace(R.id.fragment,new DayFragment());
                tsction.commit();
                setTitle("DAY");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
