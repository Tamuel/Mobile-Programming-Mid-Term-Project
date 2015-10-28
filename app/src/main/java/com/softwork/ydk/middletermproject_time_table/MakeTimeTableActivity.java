package com.softwork.ydk.middletermproject_time_table;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MakeTimeTableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_time_table);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.make_time_table_button:
                EditText editText = (EditText) findViewById(R.id.time_table_name_edit_text);
                Intent data = new Intent();
                data.putExtra(TimeTableData.TIME_TABLE_NAME, editText.getText().toString());
                setResult(TimeTableData.RESULT_OK, data);
                finish();
                break;
        }
    }
}
