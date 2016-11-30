package com.example.yn782.androidtermproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yn782 on 2016-11-05.
 */

public class DayFragment extends Fragment {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    //현재 년도, 월, 일
    int dyear = cal.get(cal.YEAR);
    int dmonth = cal.get(cal.MONTH) + 1;
    int dday = cal.get(cal.DATE);
    int lastDay[] = {31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31};
    Button selectday, addbtn;
    View dialogView;
    ArrayList<DaylistItem> data;
    DBHandler dbhand;
    CalendarView cv;
    static ListView datelist;
    TextView nodatetv, dNowdaytv;
    int selectmode = 0;
    int list = 0;
    static DayAdapter dayadapter;//리스트뷰 어댑터

    public DayFragment() {

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_day, null);

        cv = (CalendarView) view.findViewById(R.id.calView);//캘린더뷰
        cv.setVisibility(View.GONE);

        selectday = (Button) view.findViewById(R.id.dayselectbtn);
        addbtn = (Button) view.findViewById(R.id.addschedulebtn);
        dNowdaytv = (TextView) view.findViewById(R.id.dNowday);
        nodatetv = (TextView) view.findViewById(R.id.nodate);//일정이 없습니다. 메세지

        datelist = (ListView) view.findViewById(R.id.daylist1);
        dNowdaytv.setText(dyear + "년" + dmonth + "월" + dday + "일");

        dbhand = new DBHandler(getContext());
        data = new ArrayList<DaylistItem>();//날짜 제목 시간 장소
        dayadapter = new DayAdapter(getContext(), R.layout.list_item_day, data, dbhand, dyear, dmonth, dday);//순서유의
        data = notifychangedListview(data, dyear, dmonth, dday);//현재 년 월에 대한 리스트 뷰
        datelist.setAdapter(dayadapter);

//        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("클릭");
//            }
//        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addScheduleActivity.class);
                intent.putExtra("year", dyear);
                intent.putExtra("month", dmonth);
                intent.putExtra("day", dday);
                startActivity(intent);
            }
        });
        //일정 이벤트 처리
        datelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                final String nowtime =((DaylistItem) dayadapter.getItem(position)).dnowtime;
                String sql = String.format(
                        "Select * FROM ex12 WHERE nowtime='%s'",nowtime);
                Cursor cursor=dbhand.getReadableDatabase().rawQuery(sql,null);

                Intent intent = new Intent(getContext(),ScheActivity.class);
                while (cursor.moveToNext()) {
                    intent.putExtra("Date",cursor.getString(5)+"년"+cursor.getString(6)+"월"+cursor.getString(7)+"일");
                    intent.putExtra("Title",cursor.getString(1));
                    intent.putExtra("sTime",cursor.getString(2));
                    intent.putExtra("eTime",cursor.getString(3));
                    intent.putExtra("Place",cursor.getString(4));
                    intent.putExtra("NowTime", cursor.getString(8));
                }
                startActivity(intent);

            }
        });

        //삭제하기
        datelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View vClicked, int position, long id) {
                System.out.println("롱클릭");
                dialogView = (View) View.inflate(getContext(), R.layout.delete_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                final String time1 = ((DaylistItem) dayadapter.getItem(position)).dnowtime;
                dlg.setTitle("일정삭제하기");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        String sql = String.format(
                                "DELETE FROM ex12 WHERE nowtime='%s'", time1);
                        dbhand.getWritableDatabase().execSQL(sql);
                        data = notifychangedListview(data, dyear, dmonth, dday);
                        // data = notifychangedListview(data,year,month,day);// 버튼클릭시 리스트 뷰 갱신
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                return true;
            }
        });

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                dyear = year;
                dmonth = month + 1;
                dday = dayOfMonth;
                data = notifychangedListview(data, dyear, dmonth, dday);
                Log.i("DayFragment23", dyear + "" + dmonth + "" + dday);
                dNowdaytv.setText(dyear + "년" + dmonth + "월" + dday + "일");
            }
        });

        selectday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectmode == 0) {
                    cv.setVisibility(View.VISIBLE);
                    nodatetv.setVisibility(View.GONE);
                    selectmode = 1;
                } else if (selectmode == 1) {
                    selectmode = 0;
                    cv.setVisibility(View.GONE);
                    if (list == 0)
                        nodatetv.setVisibility(View.VISIBLE);
                    if (list == 1)
                        nodatetv.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }


    public ArrayList<DaylistItem> notifychangedListview(ArrayList<DaylistItem> Cdata, int Cyear, int Cmonth, int Cday) {
        Cdata.clear(); //리스트를 빈 상태로 초기화
        int i = 0;
        String sql2 = "Select * FROM ex12 WHERE year=" + Cyear + " AND month=" + Cmonth + " AND day=" + Cday; // AND앞 띄어쓰기 유의
        Cursor cursor = dbhand.getReadableDatabase().rawQuery(sql2, null); // dbhandler는 어차피 공유하므로 그대로 사용
        while (cursor.moveToNext()) { // db내용 가져옴/Item에 저장
            Cdata.add(new DaylistItem(cursor.getString(1), cursor.getString(2) + "~" + cursor.getString(3), cursor.getString(4), cursor.getString(8)));
            i++;
        }

        if (i == 0) {
            list = 0;
            nodatetv.setVisibility(View.VISIBLE);
            dayadapter.notifyDataSetInvalidated(); // 내용이 0 개일 경우 빈 어댑터 적용
        } else {
            list = 1;
            nodatetv.setVisibility(View.GONE);
            dayadapter.notifyDataSetChanged();// 내용 새로고침
        }
        return Cdata;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override // 리스트뷰 최신화
    public void onStart() {
        super.onStart();
        data = notifychangedListview(data, dyear, dmonth, dday);
        selectmode = 0;
        cv.setVisibility(View.GONE);
    }


}

