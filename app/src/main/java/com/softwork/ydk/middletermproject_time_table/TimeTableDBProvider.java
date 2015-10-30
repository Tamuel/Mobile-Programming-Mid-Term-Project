package com.softwork.ydk.middletermproject_time_table;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by DongKyu on 2015-10-15.
 */
public class TimeTableDBProvider extends ContentProvider {
    static final public String DB_LECTURE_TABLE = "lecture";
    static final public String DB_TIME_TABLE = "time";
    static final public String DB_NOTE_TABLE = "note";
    static final public String AUTHORITY = "TiMe.softwork.com";

    static final public Uri LECTURE_TABLE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DB_LECTURE_TABLE);
    static final public Uri LECTURE_TABLE_TIME_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DB_TIME_TABLE);

    static final int TIME_TABLE_GETALL = 1;
    static final int TIME_TABLE_GETONE = 2;
    static final int TIME_TABLE_TIME_GETALL = 3;
    static final int TIME_TABLE_TIME_GETONE = 4;
    static final int TIME_TABLE_NOTE_GETALL = 5;
    static final int TIME_TABLE_NOTE_GETONE = 6;

    static public boolean SHOULD_MAKE_TIMETABLE = false;

    // For Lecture Table
    static final public String LECTURE_ID = "_lID";
    static final public String LECTURE_MAJOR = "lectureMajor";
    static final public String LECTURE_NAME = "lectureName";
    static final public String LECTURE_ROOM = "lectureRoom";
    static final public String LECTURE_PROFESSOR = "professor";
    static final public String LECTURE_INFORMATION = "lectureInformation";
    static final public String LECTURE_TIME = "lectureTime";
    static final public String LECTURE_NOTES = "lectureNotes";

    // For Lecture Time Table
    static final public String LECTURE_TIME_ID = "_tID";
    static final public String LECTURE_TIME_DAY = "lectureDay";
    static final public String LECTURE_TIME_START_TIME = "lectureStartTime";
    static final public String LECTURE_TIME_END_TIME = "lectureEndTime";

    // For Lecture Note Table
    static final public String LECTURE_NOTE_ID = "_nID";
    static final public String LECTURE_NOTE_CONTENT = "lectureNoteContent";
    static final public String LECTURE_NOTE_TIME = "lectureNoteTime";

    static final UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, DB_LECTURE_TABLE, TIME_TABLE_GETALL);
        matcher.addURI(AUTHORITY, DB_LECTURE_TABLE + "/*", TIME_TABLE_GETONE);

        matcher.addURI(AUTHORITY, DB_TIME_TABLE, TIME_TABLE_TIME_GETALL);
        matcher.addURI(AUTHORITY, DB_TIME_TABLE + "/*", TIME_TABLE_TIME_GETONE);

        matcher.addURI(AUTHORITY, DB_NOTE_TABLE, TIME_TABLE_NOTE_GETALL);
        matcher.addURI(AUTHORITY, DB_NOTE_TABLE + "/*", TIME_TABLE_NOTE_GETONE);
    }

    SQLiteDatabase timeTableDB;
    class timeTableDBHelper extends SQLiteOpenHelper {
        public timeTableDBHelper(Context c) {
            super(c, TimeTableData.TIME_TABLE_NAME + ".tt", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_NOTE_TABLE
                    + " (" +  LECTURE_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LECTURE_NOTE_CONTENT+ " TEXT, "
                    + LECTURE_NOTE_TIME + " INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TIME_TABLE
                    + " (" +  LECTURE_TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LECTURE_TIME_DAY + " TEXT, "
                    + LECTURE_TIME_START_TIME + " INTEGER, "
                    + LECTURE_TIME_END_TIME + " INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_LECTURE_TABLE
                    + " (" + LECTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LECTURE_MAJOR + " TEXT, "
                    + LECTURE_NAME + " TEXT, "
                    + LECTURE_ROOM + " TEXT, "
                    + LECTURE_PROFESSOR + " TEXT, "
                    + LECTURE_INFORMATION + " TEXT, "
                    + LECTURE_TIME + " INTEGER, "
                    + LECTURE_NOTES + " INTEGER, "
                    + " FOREIGN KEY (" + LECTURE_TIME + ") REFERENCES "
                    + DB_TIME_TABLE + "(" + LECTURE_TIME_ID + ")"
                    + " FOREIGN KEY (" + LECTURE_NOTES+ ") REFERENCES "
                    + DB_NOTE_TABLE + "(" + LECTURE_NOTE_ID + "));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_LECTURE_TABLE + ";");
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        timeTableDBHelper timeTableHelper = new timeTableDBHelper(getContext());

        timeTableDB = timeTableHelper.getWritableDatabase();

        Log.e("DB", timeTableDB.toString());

        return (timeTableDB != null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        String where;

        switch (matcher.match(uri)) {
            case TIME_TABLE_GETALL:
                cnt = timeTableDB.delete(DB_LECTURE_TABLE, selection, selectionArgs);
                break;

            case TIME_TABLE_TIME_GETALL:
                cnt = timeTableDB.delete(DB_TIME_TABLE, selection, selectionArgs);
                break;

            case TIME_TABLE_GETONE:
                where = LECTURE_NAME + " = '" + uri.getPathSegments().get(1) + "'";
                if(TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = timeTableDB.delete(DB_LECTURE_TABLE, where, selectionArgs);
                break;

            case TIME_TABLE_TIME_GETONE:
                where = LECTURE_NAME + " = '" + uri.getPathSegments().get(1) + "'";
                if(TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = timeTableDB.delete(DB_TIME_TABLE, where, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String sql;
        sql = "SELECT * FROM " + selection;

        if(matcher.match(uri) == TIME_TABLE_GETONE) {
            sql += " WHERE " + LECTURE_NAME + " = '";
            sql += uri.getPathSegments().get(1);
            sql += "'";
        }
        sql += ";";

        Cursor cur = timeTableDB.rawQuery(sql, null);
        return cur;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case TIME_TABLE_GETALL:
                return "vnd.android.cursor.dir/vnd.ydksoftwork.lecture";
            case TIME_TABLE_GETONE:
                return "vnd.android.cursor.item/vnd.ydksoftwork.lecture";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = 0;
        switch (matcher.match(uri)) {
            case TIME_TABLE_GETALL:
                row = timeTableDB.insert(DB_LECTURE_TABLE, null, values);
                break;

            case TIME_TABLE_TIME_GETALL:
                row = timeTableDB.insert(DB_TIME_TABLE, null, values);
                break;
        }
        if(row > 0) {
            Uri notiuri = ContentUris.withAppendedId(LECTURE_TABLE_CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(notiuri, null);
            return notiuri;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cnt = 0;
        String where;

        switch (matcher.match(uri)) {
            case TIME_TABLE_GETALL:
                cnt = timeTableDB.update(DB_LECTURE_TABLE, values, selection, selectionArgs);
                break;

            case TIME_TABLE_TIME_GETALL:
                cnt = timeTableDB.update(DB_TIME_TABLE, values, selection, selectionArgs);
                break;

            case TIME_TABLE_GETONE:
                where = LECTURE_NAME + " = '" + uri.getPathSegments().get(1) + "'";
                if(TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = timeTableDB.update(DB_LECTURE_TABLE, values, where, selectionArgs);
                break;

            case TIME_TABLE_TIME_GETONE:
                where = LECTURE_TIME_ID + " = '" + uri.getPathSegments().get(1) + "'";
                if(TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = timeTableDB.update(DB_LECTURE_TABLE, values, where, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
