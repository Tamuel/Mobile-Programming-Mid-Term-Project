package com.softwork.ydk.middletermproject_time_table;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by DongKyu on 2015-10-29.
 */
public class TimeTableLinearLayout extends LinearLayout {
    private TimeTableData.Day day;

    private Button[] timeTableButtons;

    public TimeTableLinearLayout(Context context) {
        super(context);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0);
        layoutParams.weight = 1;
        this.setLayoutParams(layoutParams);
        this.setOrientation(LinearLayout.VERTICAL);

        timeTableButtons = new Button[TimeTableActivity.timeStepStrings.length];

        LinearLayout.LayoutParams timeTableButtonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                (int) getResources().getDimension(R.dimen.time_table_button_height),
                0);

        // Make Time Table buttons
        for(int i = 0; i < TimeTableActivity.timeStepStrings.length; i++) {
            if(i == 0) {
                TextView blankTextView = new TextView(context);
                blankTextView.setHeight((int) (getResources().getDimension(R.dimen.time_table_button_height)));
                blankTextView.setBackgroundColor(Color.WHITE);
            }

            timeTableButtons[i] = new Button(context);
            timeTableButtons[i].setLayoutParams(timeTableButtonLayoutParams);
            timeTableButtons[i].setBackground(getResources().getDrawable(R.drawable.time_table_button));
            this.addView(timeTableButtons[i]);
        }
    }

    public TimeTableData.Day getDay() {
        return day;
    }

    public void setDay(TimeTableData.Day day) {
        this.day = day;
    }
}
