package com.jameydeorio.bionicflamingo.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.api.BookApi;
import com.jameydeorio.bionicflamingo.api.ServiceGenerator;
import com.jameydeorio.bionicflamingo.database.BookDataSource;
import com.jameydeorio.bionicflamingo.models.Book;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleLabel;
    private ImageView mImageView;

    private Book mBook;
    private BookDataSource bookDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleLabel = (TextView) findViewById(R.id.titleLabel);
        mImageView = (ImageView) findViewById(R.id.detailCoverImage);

        bookDataSource = new BookDataSource(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int bookId = extras.getInt(Book.BOOK_ID_KEY);
        mTitleLabel.setText(String.format("Book %s", bookId));

        mBook = bookDataSource.getBook(bookId);
        if (mBook == null) {
            downloadBook(bookId);
        } else {
            populateLabels();
        }
    }

    private void populateLabels() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitleLabel.setText(mBook.getTitle());

                Picasso.with(getApplicationContext())
                        .load(mBook.getCoverUrl())
                        .into(mImageView);

            }
        });
    }

    private void downloadBook(int bookId) {
        BookApi bookApi = ServiceGenerator.createService(BookApi.class);
        Call<Book> call = bookApi.getBook(bookId);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Response<Book> response) {
                if (!response.isSuccess()) {
                    Log.e(TAG, response.errorBody().toString());
                    return;
                }

                mBook = response.body();
                bookDataSource.create(mBook);
                populateLabels();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
