package com.softwork.ydk.middletermproject_time_table;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeTableActivity extends Activity {
    static public String[] weekDayStrings;
    static public String[] timeStepStrings;

    private Button[] weekDayButtons;

    private LinearLayout timeTableLayout;
    private LinearLayout weekDayLayout;
    private TimeTableTimeLinearLayout timeTableTimeLayout;
    private TimeTableLinearLayout[] timeTableLayouts; // Index 0 for show time step


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(Uri.parse("content://TiMe.softwork.com/lecture"), null, null, null, null);

        if(cur == null) {
            Intent getData = new Intent(TimeTableActivity.this, MakeTimeTableActivity.class);
            startActivityForResult(getData, TimeTableData.GET_TIME_TABLE_CODE);
        }

        weekDayLayout = (LinearLayout) findViewById(R.id.week_layout);
        timeTableLayout = (LinearLayout) findViewById(R.id.timeTableOuterLayout);

        weekDayStrings = getResources().getStringArray(R.array.week_day);
        timeStepStrings = getResources().getStringArray(R.array.time_table_hours);

        weekDayButtons = new Button[weekDayStrings.length];
        timeTableTimeLayout = new TimeTableTimeLinearLayout(this);
        timeTableLayouts = new TimeTableLinearLayout[weekDayStrings.length - 1];

        // Make week day buttons
        for(int i = 0; i < weekDayStrings.length; i++) {
            weekDayButtons[i] = new Button(this);
            weekDayButtons[i].setText(weekDayStrings[i]);
            weekDayButtons[i].setBackground(getResources().getDrawable(R.drawable.day_button));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0);
            layoutParams.weight = 1;
            weekDayButtons[i].setLayoutParams(layoutParams);
            weekDayLayout.addView(weekDayButtons[i]);
        }

        // Make Time table layouts
        timeTableLayout.addView(timeTableTimeLayout);
        for(int i = 0; i < weekDayStrings.length - 1; i++) {
            timeTableLayouts[i] = new TimeTableLinearLayout(this);
            timeTableLayouts[i].setDay(TimeTableData.Day.values()[i]);
            timeTableLayout.addView(timeTableLayouts[i]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TimeTableData.GET_TIME_TABLE_CODE) {
            if(resultCode == TimeTableData.RESULT_OK) {
                TextView tableName = (TextView) findViewById(R.id.time_table_name_text_view);
                TimeTableData.TIME_TABLE_NAME = data.getStringExtra(TimeTableData.GET_TIME_TABLE_NAME);
                tableName.setText(TimeTableData.TIME_TABLE_NAME);

                ContentResolver cr = getContentResolver();
                ContentValues content = new ContentValues();

                content.put(TimeTableDBProvider.LECTURE_NAME, "모바일앱프로그래밍");
                content.put(TimeTableDBProvider.LECTURE_ROOM, "공대 9호관 408호");

                cr.insert(TimeTableDBProvider.LECTURE_TABLE_CONTENT_URI, content);

            }
        }
    }
}
