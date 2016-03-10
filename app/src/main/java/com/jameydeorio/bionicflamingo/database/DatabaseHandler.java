package com.jameydeorio.bionicflamingo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Subathra Thanabalan on 3/9/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DB_NAME = "bionicflamingo.db";
    public static final int DB_VERSION = 2;

    // General properties
    public static final String COLUMN_IDENTIFIER = "IDENTIFIER";

    // Properties for Books
    public static final String BOOKS_TABLE = "BOOKS";
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

    // Properties for QueueItems
    public static final String QUEUE_ITEMS_TABLE = "QUEUE_ITEMS";
    public static final String COLUMN_USER = "USER";
    public static final String COLUMN_BOOK = "BOOK";
    private static final String CREATE_QUEUE_ITEMS_TABLE =
            "CREATE TABLE " + QUEUE_ITEMS_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_IDENTIFIER + " INTEGER," +
                    COLUMN_USER + " INTEGER," +
                    COLUMN_BOOK + " INTEGER)";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKS_TABLE);
        db.execSQL(CREATE_QUEUE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
