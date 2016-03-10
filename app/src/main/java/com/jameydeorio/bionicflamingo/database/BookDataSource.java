package com.jameydeorio.bionicflamingo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.jameydeorio.bionicflamingo.models.Book;

import java.util.ArrayList;

public class BookDataSource extends DataSource {

    public BookDataSource(Context context) {
        super(context);
    }

    public Book getBook(int bookId) {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                DatabaseHandler.BOOKS_TABLE,
                new String[]{BaseColumns._ID, DatabaseHandler.COLUMN_TITLE,
                        DatabaseHandler.COLUMN_IDENTIFIER, DatabaseHandler.COLUMN_ISBN,
                        DatabaseHandler.COLUMN_COVER_URL},
                String.format("%s=%s", DatabaseHandler.COLUMN_IDENTIFIER, bookId),
                null, null, null, null
        );

        Book book = new Book();

        if (cursor.getCount() != 1) {
            cursor.close();
            close(database);
            return null;
        }

        if (cursor.moveToFirst()) {
            book.setId(getIntFromColumnName(cursor, BaseColumns._ID));
            book.setIdentifier(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_IDENTIFIER));
            book.setIsbn(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_ISBN));
            book.setTitle(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_TITLE));
            book.setCoverUrl(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_COVER_URL));
        }

        cursor.close();
        close(database);

        return book;
    }

    public ArrayList<Book> getBooks() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                DatabaseHandler.BOOKS_TABLE,
                new String[]{BaseColumns._ID, DatabaseHandler.COLUMN_TITLE,
                        DatabaseHandler.COLUMN_IDENTIFIER, DatabaseHandler.COLUMN_ISBN,
                        DatabaseHandler.COLUMN_COVER_URL},
                null, null, null, null, null
        );

        ArrayList<Book> books = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(getIntFromColumnName(cursor, BaseColumns._ID));
                book.setIdentifier(getIntFromColumnName(cursor, DatabaseHandler.COLUMN_IDENTIFIER));
                book.setIsbn(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_ISBN));
                book.setTitle(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_TITLE));
                book.setCoverUrl(getStringFromColumnName(cursor, DatabaseHandler.COLUMN_COVER_URL));
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close(database);

        return books;
    }

    public void create(Book book) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.COLUMN_IDENTIFIER, book.getIdentifier());
        contentValues.put(DatabaseHandler.COLUMN_ISBN, book.getIsbn());
        contentValues.put(DatabaseHandler.COLUMN_TITLE, book.getTitle());
        contentValues.put(DatabaseHandler.COLUMN_COVER_URL, book.getCoverUrl());
        database.insert(DatabaseHandler.BOOKS_TABLE, null, contentValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
