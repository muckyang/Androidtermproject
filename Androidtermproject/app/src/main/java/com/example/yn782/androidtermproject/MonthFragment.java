package com.example.yn782.androidtermproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-05.
 */

public class MonthFragment extends Fragment {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    //현재 년도, 월, 일
    int year = cal.get ( cal.YEAR );
    int month = cal.get ( cal.MONTH ) + 1 ;
    int day = cal.get ( cal.DATE ) ;

    //int year=2016,month=11,day=27; // 현재날짜 입력
    int lastDay[]={31,28,31,30,31,30,
                31,31,30,31,30,31};
    int dday=2;//월 앞의 공백
    TextView tx;
    Button previous,next;
    GridView calendar;
    ListView sche;
    DBHandler dbhand;
    CalendarAdapter ca;
    ArrayList<MyItem> data;
    ArrayList<MonthItem> griditem;
    View dialogView;
    //String dbtitle[];
    static ScheduleListAdapter scadapter;//리스트뷰 어댑터

    public MonthFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater , final ViewGroup container , Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_month,null);

        tx = (TextView)view.findViewById(R.id.Nowmonth);
        tx.setText(year+"년"+month+"월");
        previous = (Button)view.findViewById(R.id.previous);
        next = (Button)view.findViewById(R.id.next);
        calendar =(GridView)view.findViewById(R.id.gridView1); // 달력
        sche = (ListView) view.findViewById(R.id.listView1); //일정
        dbhand =new DBHandler(getContext());
        sche.setDivider(new ColorDrawable(Color.WHITE));
        sche.setDividerHeight(2);
        //dday 는 월 앞의 공백 ,lastDay[month-1]은 월의 일수
        griditem = new ArrayList<MonthItem>();
        ca= new CalendarAdapter(getActivity(),R.layout.gird_item_month, griditem,dbhand, year,month, dday,lastDay[month-1]);
        calendar.setAdapter(ca);


        //일정 리스트뷰
        data = new ArrayList<MyItem>();//날짜 제목 시간 장소
        scadapter = new ScheduleListAdapter(getContext(), R.layout.list_item_month, data ,year,month);//순서유의
        data = notifychangedListview(data,year,month);//현재 년 월에 대한 리스트 뷰
        sche.setAdapter(scadapter);


        //일정 이벤트 처리
        sche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                Intent intent = new Intent(getContext(),ScheActivity.class);
                intent.putExtra("Date",((MyItem)scadapter.getItem(position)).nDate);
                intent.putExtra("Title",((MyItem)scadapter.getItem(position)).nTitle);
                intent.putExtra("sTime",((MyItem)scadapter.getItem(position)).nsTime);
                intent.putExtra("eTime",((MyItem)scadapter.getItem(position)).neTime);
                intent.putExtra("Place",((MyItem)scadapter.getItem(position)).nPlace);
                intent.putExtra("NowTime",((MyItem)scadapter.getItem(position)).nnowTime);
                startActivity(intent);

            }
        });
        sche.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View vClicked, int position, long id) {
               dialogView = (View) View.inflate(getContext(),R.layout.delete_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                final String time1 =((MyItem)scadapter.getItem(position)).nnowTime;
                dlg.setTitle("일정삭제하기");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog ,int i){
                        String sql = String.format(
                   "DELETE FROM ex12 WHERE nowtime='%s'",time1);
                         dbhand.getWritableDatabase().execSQL(sql);
                        data = notifychangedListview(data,year,month,day);// 버튼클릭시 리스트 뷰 갱신
                        ca.notifyDataSetChanged();
                        calendar.setAdapter(ca);
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
                return true;
            }
        });



        //달력 아이템 이벤트처리
        calendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                day=position-dday+1;
                if(day >=1) {//달력앞 공백은 제외
                    Intent intent = new Intent(getContext(),addScheduleActivity.class);
                    intent.putExtra("year",year);
                    intent.putExtra("month",month);
                    intent.putExtra("day",day);
                    startActivity(intent);

                }
                return true;
            }

        });
//달력 아이템 이벤트처리
        calendar.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day=position-dday+1;
                if(day >=1) {//달력앞 공백은 제외
                    data = notifychangedListview(data,year,month,day);
                }
            }

        });


        //버튼에 클릭 이벤트 처리
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year==2016 && month ==1) {
                    Toast.makeText(getContext(),"첫달 입니다.!", Toast.LENGTH_SHORT).show();
                    System.out.println("처음시작 입니다.!");
                }else if (month == 1) {
                    year--;
                    month = 12;
                    dday=7-(lastDay[month-1]-dday)%7;
                    if(dday==7) dday=0;
                } else if(year % 4 == 0 && month==3){//윤년
                    month--;
                    dday=7-(29-dday)%7;
                    if(dday==7) dday=0;
                }else {
                    month--;
                    dday=7-(lastDay[month-1]-dday)%7;
                    if(dday==7) dday=0;
                }
                tx.setText(year+"년"+month+"월");
                // girdview변동+ listView 변동
                if(year % 4 == 0 && month==2) {//윤년전용
                    calendar.setAdapter(new CalendarAdapter(getActivity(),R.layout.gird_item_month,griditem ,dbhand, year, month, dday, 29));
                }else{
                    calendar.setAdapter(new CalendarAdapter(getActivity(),R.layout.gird_item_month,griditem, dbhand, year, month, dday, lastDay[month - 1]));
                }
                data = notifychangedListview(data,year,month);// 버튼클릭시 리스트 뷰 갱신
                ca.notifyDataSetChanged();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year==2020 && month ==12) {
                    Toast.makeText(getContext(),"마지막달 입니다.!", Toast.LENGTH_SHORT).show();
                    System.out.println("마지막 입니다.!");
                }else if (month == 12) {
                    dday=(dday+lastDay[month-1])%7;
                    year++;
                    month = 1;
                }else if(year % 4 == 0 && month==2){//윤년
                    dday=(dday+29)%7;
                    month++;
                }else{
                    dday=(dday+lastDay[month-1])%7;
                    month++;
                }
                tx.setText(year+"년"+month+"월");
                // girdview변동
                if(year % 4 == 0 && month==2) {//윤년전용
                    calendar.setAdapter(new CalendarAdapter(getActivity(),R.layout.gird_item_month,griditem,dbhand, year, month, dday, 29));
                }else{
                    calendar.setAdapter(new CalendarAdapter(getActivity(),R.layout.gird_item_month,griditem, dbhand, year, month, dday, lastDay[month - 1]));
                }
                data = notifychangedListview(data,year,month); // 버튼클릭시 리스트 뷰 갱신
                ca.notifyDataSetChanged();

            }
        });
        return view;
    }

    //일정 추가,갱신 previous,next누를때마다 불러주어야 한다.
    public ArrayList<MyItem> notifychangedListview(ArrayList<MyItem> Cdata,int Cyear, int Cmonth){
        Cdata.clear(); //리스트를 빈 상태로 초기화
        int i =0;
        String sql2 = "Select * FROM ex12 WHERE year="+Cyear+" AND month="+Cmonth+" ORDER BY day ASC"; // AND앞 띄어쓰기 유의
        Cursor cursor = dbhand.getReadableDatabase().rawQuery(sql2,null); // dbhandler는 어차피 공유하므로 그대로 사용
        while (cursor.moveToNext()) { // db내용 가져옴/Item에 저장
            Cdata.add(new MyItem(cursor.getString(5)+"년"+cursor.getString(6)+"월"+cursor.getString(7)+"일",
                    cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(8)));
            i++;
        }
        if(i==0)scadapter.notifyDataSetInvalidated(); // 내용이 0 개일 경우 빈 어댑터 적용
        else scadapter.notifyDataSetChanged();// 내용 새로고침
        return Cdata;
    }
    public ArrayList<MyItem> notifychangedListview(ArrayList<MyItem> Cdata,int Cyear, int Cmonth,int Cday){
        Cdata.clear(); //리스트를 빈 상태로 초기화
        int i =0;
        String sql2 = "Select * FROM ex12 WHERE year="+Cyear+" AND month="+Cmonth+" AND day="+Cday; // AND앞 띄어쓰기 유의
        Cursor cursor = dbhand.getReadableDatabase().rawQuery(sql2,null); // dbhandler는 어차피 공유하므로 그대로 사용
        while (cursor.moveToNext()) { // db내용 가져옴/Item에 저장
            Cdata.add(new MyItem(cursor.getString(5)+"년"+cursor.getString(6)+"월"+cursor.getString(7)+"일",
                    cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(8)));
            i++;
        }
        if(i==0)scadapter.notifyDataSetInvalidated(); // 내용이 0 개일 경우 빈 어댑터 적용
        else scadapter.notifyDataSetChanged();// 내용 새로고침
        return Cdata;
    }

    public ArrayList<MonthItem> notifychangedgridview(ArrayList<MonthItem> Gdata, int Gyear,int Gmonth){
        Gdata.clear();
        ca.notifyDataSetChanged();
        return Gdata;
    }

    @Override // 리스트뷰 최신화
    public void onStart() {
        super.onStart();
        data = notifychangedListview(data,year,month,day);
        ca= new CalendarAdapter(getActivity(),R.layout.gird_item_month, griditem,dbhand, year,month, dday,lastDay[month-1]);
        calendar.setAdapter(ca);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
