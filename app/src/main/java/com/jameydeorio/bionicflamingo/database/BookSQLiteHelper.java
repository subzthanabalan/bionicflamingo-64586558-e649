package com.jameydeorio.bionicflamingo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BookSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "bionicflamingo.db";
    private static final int DB_VERSION = 1;

    public static final String BOOKS_TABLE = "BOOKS";
    public static final String COLUMN_IDENTIFIER = "IDENTIFIER";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_ISBN = "ISBN";
    public static final String COLUMN_COVER_URL = "COVER_URL";
    private static final String CREATE_BOOKS_TABLE =
            "CREATE TABLE " + BOOKS_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_IDENTIFIER + " INTEGER," +
                    COLUMN_ISBN + " INTEGER," +
                    COLUMN_COVER_URL + " TEXT," +
                    COLUMN_TITLE + " TEXT)";

    public BookSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
