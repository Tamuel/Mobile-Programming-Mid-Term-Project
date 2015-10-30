package com.softwork.ydk.middletermproject_time_table;

/**
 * Created by DongKyu on 2015-10-29.
 */
public class TimeTableData {
    static public String TIME_TABLE_NAME; // Name of time table

    static public String GET_TIME_TABLE_NAME = "TIME_TABLE_NAME";
    static public int GET_TIME_TABLE_CODE = 1024;
    static public int RESULT_OK = 1;
    static public int RESULT_FAIL = 0;

    public enum Day {
        MON(0),
        TUE(1),
        WED(2),
        THR(3),
        FRI(4);

        private int value;

        private Day() {

        }

        private Day(int value) {
            this.value = value;
        }

        public int getInt() {
            return value;
        }
    }
}
