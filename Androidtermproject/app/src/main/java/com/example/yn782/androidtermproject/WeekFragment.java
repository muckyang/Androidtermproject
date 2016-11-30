package com.example.yn782.androidtermproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-05.
 */

public class WeekFragment extends Fragment {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    //현재 년도, 월, 일
    int year = cal.get ( cal.YEAR );
    int month = cal.get ( cal.MONTH ) + 1 ;
    int day = cal.get ( cal.DATE ) ;
    int nWeek = cal.get(cal.DAY_OF_WEEK);
    int sday;//이번주 시작일

    int lastDay[]={31,28,31,30,31,30,
            31,31,30,31,30,31};
    int prevyear , prevmonth,prevsday;

    ListView weeklist,sche;
    TextView tx;
    Button previous,next;
    ArrayList<WeekItem> wi;
    DBHandler dbhand;
    WeekAdapter wa;
    public WeekFragment(){

    }

    public View onCreateView(LayoutInflater inflater , ViewGroup container ,Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_week,null);

        if(nWeek==1){
            sday=day;
        }else if(nWeek==2){
            sday=day-1;
        }else if(nWeek==3){
            sday=day-2;
        }else if(nWeek==4){
            sday=day-3;
        }else if(nWeek==5){
            sday=day-4;
        }else if(nWeek==6){
            sday=day-5;
        }else if(nWeek==7){
            sday=day-6;
        }
        tx = (TextView)view.findViewById(R.id.Nowweek);
        tx.setText(year+"년"+month+"월");
        previous = (Button)view.findViewById(R.id.wprevious);
        next = (Button)view.findViewById(R.id.wnext);
        dbhand =new DBHandler(getContext());
        weeklist =(ListView)view.findViewById(R.id.weeklist); // 주별리스트


        wi=new ArrayList<WeekItem>();
        wa= new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand, year , month , sday , lastDay[month-1]);
        weeklist.setAdapter(wa);

        //버튼에 클릭 이벤트 처리
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year==2016 && month ==1 && sday<=7) {
                    Toast.makeText(getContext(),"첫달 입니다.!", Toast.LENGTH_SHORT).show();
                    System.out.println("처음시작 입니다.!");
                }else if (month == 1 && sday<=7) {//년도변경시
                    month=12;
                    year--;
                    sday = lastDay[month-1]-(7-sday);//변경후 월의 시작일
                    tx.setText(year+"년"+month+"월");
                } else if(year % 4 == 0 && month==3 && sday<=7){//윤년
                    month--;
                    sday = 29-(7-sday);
                    tx.setText(year+"년"+month+"월");
                }else if(sday<=7) {//월만 변경
                    month--;
                    sday = lastDay[month-1] - (7 - sday);
                    tx.setText(year+"년"+month+"월");
                }else{//미변경
                    sday=sday-7;
                    tx.setText(year+"년"+month+"월");
                    System.out.println("prev버튼클릭"+sday);
                }

                // girdview변동+ listView 변동
                if(year % 4 == 0 && month==2) {//윤년전용
                    weeklist.setAdapter(new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand,  year , month , sday , 29));
                }else{
                    weeklist.setAdapter(new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand,  year , month , sday , lastDay[month-1]));
                }
                //data = notifychangedListview(data,year,month);// 버튼클릭시 리스트 뷰 갱신
               // ca.notifyDataSetChanged();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year==2020 && month ==12 && sday+7>31) {
                    Toast.makeText(getContext(),"마지막달 입니다.!", Toast.LENGTH_SHORT).show();
                }else if (month == 12 && sday+7>31) {
                    year++;
                    sday=(sday+7)-lastDay[month-1];
                    tx.setText(year+"년"+month+"월");//월은 아직 미변경
                    month=1;

                }else if(year % 4 == 0 && month==2 && sday+7>29){//윤년
                    sday=(sday+7)-29;
                    month++;
                }else if(sday+7>lastDay[month-1]){
                    sday=(sday+7)-lastDay[month-1];
                    month++;
                }else{
                    sday=sday+7;
                }
                tx.setText(year+"년"+month+"월");
                // girdview변동
                if(year % 4 == 0 && month==2) {//윤년전용
                    weeklist.setAdapter(new WeekAdapter(getContext(),R.layout.list_item_week,wi, dbhand, year , month , sday , 29));
                }else{
                    weeklist.setAdapter(new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand,  year , month , sday , lastDay[month-1]));
                }
               // data = notifychangedListview(data,year,month); // 버튼클릭시 리스트 뷰 갱신
               // ca.notifyDataSetChanged();
            }
        });
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        wa= new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand, year , month , sday , lastDay[month-1]);
        weeklist.setAdapter(wa);
    }

    public void noti() {
        wa= new WeekAdapter(getContext(),R.layout.list_item_week,wi,dbhand, year , month , sday , lastDay[month-1]);
        weeklist.setAdapter(wa);
    }
}
