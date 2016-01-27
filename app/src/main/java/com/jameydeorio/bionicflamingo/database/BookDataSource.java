package com.jameydeorio.bionicflamingo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.jameydeorio.bionicflamingo.models.Book;

import java.util.ArrayList;

public class BookDataSource {
    private Context mContext;
    private BookSQLiteHelper mBookSQLiteHelper;

    public BookDataSource(Context context) {
        mContext = context;
        mBookSQLiteHelper = new BookSQLiteHelper(mContext);
    }

    private SQLiteDatabase open() {
        return mBookSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public Book getBook(int bookId) {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                BookSQLiteHelper.BOOKS_TABLE,
                new String[]{BaseColumns._ID, BookSQLiteHelper.COLUMN_TITLE,
                        BookSQLiteHelper.COLUMN_IDENTIFIER, BookSQLiteHelper.COLUMN_ISBN,
                        BookSQLiteHelper.COLUMN_COVER_URL},
                String.format("%s=%s", BookSQLiteHelper.COLUMN_IDENTIFIER, bookId),
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
            book.setIdentifier(getIntFromColumnName(cursor, BookSQLiteHelper.COLUMN_IDENTIFIER));
            book.setIsbn(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_ISBN));
            book.setTitle(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_TITLE));
            book.setCoverUrl(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_TITLE));
        }

        cursor.close();
        close(database);

        return book;
    }

    public ArrayList<Book> getBooks() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                BookSQLiteHelper.BOOKS_TABLE,
                new String[]{BaseColumns._ID, BookSQLiteHelper.COLUMN_TITLE,
                        BookSQLiteHelper.COLUMN_IDENTIFIER, BookSQLiteHelper.COLUMN_ISBN,
                        BookSQLiteHelper.COLUMN_COVER_URL},
                null, null, null, null, null
        );

        ArrayList<Book> books = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(getIntFromColumnName(cursor, BaseColumns._ID));
                book.setIdentifier(getIntFromColumnName(cursor, BookSQLiteHelper.COLUMN_IDENTIFIER));
                book.setIsbn(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_ISBN));
                book.setTitle(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_TITLE));
                book.setCoverUrl(getStringFromColumnName(cursor, BookSQLiteHelper.COLUMN_COVER_URL));
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close(database);

        return books;
    }

    private int getIntFromColumnName(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        return cursor.getString(columnIndex);
    }

    public void create(Book book) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookSQLiteHelper.COLUMN_IDENTIFIER, book.getIdentifier());
        contentValues.put(BookSQLiteHelper.COLUMN_ISBN, book.getIsbn());
        contentValues.put(BookSQLiteHelper.COLUMN_TITLE, book.getTitle());
        contentValues.put(BookSQLiteHelper.COLUMN_COVER_URL, book.getCoverUrl());
        database.insert(BookSQLiteHelper.BOOKS_TABLE, null, contentValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
