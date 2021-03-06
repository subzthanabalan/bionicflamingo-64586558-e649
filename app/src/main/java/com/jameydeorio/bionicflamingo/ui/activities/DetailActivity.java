package com.jameydeorio.bionicflamingo.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.api.BookApi;
import com.jameydeorio.bionicflamingo.api.QueueApi;
import com.jameydeorio.bionicflamingo.api.ServiceGenerator;
import com.jameydeorio.bionicflamingo.database.BookDataSource;
import com.jameydeorio.bionicflamingo.database.QueueItemDataSource;
import com.jameydeorio.bionicflamingo.models.Book;
import com.jameydeorio.bionicflamingo.models.QueueItem;
import com.squareup.picasso.Picasso;

import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleLabel;
    private ImageView mImageView;
    private Button mDeleteButton;

    private Book mBook;
    private int queueId;

    private BookDataSource bookDataSource;
    private QueueItemDataSource queueItemDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleLabel = (TextView) findViewById(R.id.titleLabel);
        mImageView = (ImageView) findViewById(R.id.detailCoverImage);
        mDeleteButton = (Button)findViewById(R.id.removeFromQueueButton);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBookFromQueue();
            }
        });

        queueItemDataSource = new QueueItemDataSource(this);
        bookDataSource = new BookDataSource(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        queueId = extras.getInt(QueueItem.QUEUE_ITEM_ID_KEY);
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

                showToastMessage(true, mBook.getIdentifier());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

                showToastMessage(false, mBook.getIdentifier());
            }
        });
    }

    private void showToastMessage(boolean wasSuccessful, int bookId) {
        int messageId = (wasSuccessful) ? R.string.book_downloaded_success : R.string.book_downloaded_failure;
        String message = String.format(getResources().getString(messageId), bookId);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void removeBookFromQueue() {
        Log.i(DetailActivity.class.getSimpleName(), "Remove book "+mBook.getTitle()+" from queue "+queueId);
        QueueApi queueApi = ServiceGenerator.createService(QueueApi.class);
        Call<QueueItem> call = queueApi.deleteFromQueue(queueId);
        call.enqueue(new Callback<QueueItem>() {
            @Override
            public void onResponse(Response<QueueItem> response) {
                if (!response.isSuccess()) {
                    Log.e(TAG, response.errorBody().toString());
                    return;
                }

                QueueItem queueItem = response.body();

                // Delete book from database
                deleteBook();
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void deleteBook() {
        bookDataSource.delete(mBook);
    }
}
