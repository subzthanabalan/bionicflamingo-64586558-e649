package com.jameydeorio.bionicflamingo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.jameydeorio.bionicflamingo.models.QueueItem;

import java.util.ArrayList;

/**
 * Created by Subathra Thanabalan on 3/9/16.
 */
public class QueueItemDataSource extends DataSource {

    public QueueItemDataSource(Context context) {
        super(context);
    }

    public QueueItem getQueueItem(int itemId) {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                DatabaseHandler.QUEUE_ITEMS_TABLE,
                new String[]{BaseColumns._ID, DatabaseHandler.COLUMN_USER,
                        DatabaseHandler.COLUMN_IDENTIFIER,
                        DatabaseHandler.COLUMN_BOOK},
                String.format("%s=%s", DatabaseHandler.COLUMN_IDENTIFIER, itemId),
                null, null, null, null
        );

        QueueItem item = new QueueItem();

        if (cursor.getCount() != 1) {
            cursor.close();
            close(database);
            return null;
        }

        if (cursor.moveToFirst()) {
            item.setId(getIntFromColumnName(cursor, BaseColumns._ID));
            item.setIdentifier(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_IDENTIFIER));
            item.setUser(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_USER));
            item.setBook(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_BOOK));
        }

        cursor.close();
        close(database);

        return item;
    }

    public ArrayList<QueueItem> getQueueItems() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                DatabaseHandler.QUEUE_ITEMS_TABLE,
                new String[]{BaseColumns._ID, DatabaseHandler.COLUMN_USER,
                        DatabaseHandler.COLUMN_IDENTIFIER,
                        DatabaseHandler.COLUMN_BOOK},
                null, null, null, null, null
        );

        ArrayList<QueueItem> items = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                QueueItem item = new QueueItem();
                item.setId(getIntFromColumnName(cursor, BaseColumns._ID));
                item.setIdentifier(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_IDENTIFIER));
                item.setUser(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_USER));
                item.setBook(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_BOOK));
                items.add(item);
            } while (cursor.moveToNext());
        }

        Log.i(QueueItemDataSource.class.getSimpleName(),"number of items: "+items.size());

        cursor.close();
        close(database);

        return items;
    }

    public void create(QueueItem item) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.COLUMN_IDENTIFIER, item.getIdentifier());
        contentValues.put(DatabaseHandler.COLUMN_USER, item.getUser());
        contentValues.put(DatabaseHandler.COLUMN_BOOK, item.getBook());
        database.insert(DatabaseHandler.QUEUE_ITEMS_TABLE, null, contentValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
