package com.jameydeorio.bionicflamingo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Subathra Thanabalan on 3/9/16.
 */
public class DataSource {
    private Context mContext;
    private DatabaseHandler mDatabaseHandler;

    public DataSource(Context context) {
        mContext = context;
        mDatabaseHandler = new DatabaseHandler(context);
    }

    public SQLiteDatabase open() {
        return getDatabaseHandler().getWritableDatabase();
    }

    public void close(SQLiteDatabase database) {
        database.close();
    }

    public Context getContext() {
        return mContext;
    }

    public DatabaseHandler getDatabaseHandler() {
        return mDatabaseHandler;
    }

    public int getIntFromColumnName(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        return cursor.getInt(columnIndex);
    }

    public String getStringFromColumnName(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        return cursor.getString(columnIndex);
    }
}
