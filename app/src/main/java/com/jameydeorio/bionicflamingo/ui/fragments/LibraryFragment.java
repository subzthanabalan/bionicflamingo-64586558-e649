package com.jameydeorio.bionicflamingo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.adapters.BookAdapter;
import com.jameydeorio.bionicflamingo.api.BookApi;
import com.jameydeorio.bionicflamingo.api.QueueApi;
import com.jameydeorio.bionicflamingo.api.ServiceGenerator;
import com.jameydeorio.bionicflamingo.database.QueueItemDataSource;
import com.jameydeorio.bionicflamingo.models.Book;
import com.jameydeorio.bionicflamingo.models.QueueItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFragment extends Fragment {
    private static final String TAG = LibraryFragment.class.getSimpleName();

    private RecyclerView bookRecyclerView;
    private ProgressBar progressBar;
    private BookApi mBookApi;
    private QueueItemDataSource queueItemDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);

        mBookApi = ServiceGenerator.createService(BookApi.class);

        queueItemDataSource = new QueueItemDataSource(getActivity());

        bookRecyclerView = (RecyclerView) view.findViewById(R.id.libraryRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BookAdapter bookAdapter = new BookAdapter(new ArrayList<Book>());
        bookAdapter.setOnClickListener(mOnClickListener);
        bookRecyclerView.setAdapter(bookAdapter);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        getBookList();

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int bookId = Integer.parseInt(v.getTag().toString());
            QueueApi queueApi = ServiceGenerator.createService(QueueApi.class);
            QueueItem queueItemPost = new QueueItem();
            queueItemPost.setBook(bookId);
            queueItemPost.setUser(5); // To avoid auth for simplicity's sake, we always use user 5
            Call<QueueItem> call = queueApi.addToQueue(queueItemPost);
            call.enqueue(new Callback<QueueItem>() {
                @Override
                public void onResponse(Response<QueueItem> response) {
                    if (!response.isSuccess()) {
                        Log.e(TAG, response.errorBody().toString());
                        return;
                    }

                    QueueItem queueItem = response.body();
                    queueItemDataSource.create(queueItem);
                    Log.d(TAG, String.format("Downloaded queue item %s", queueItem.getId()));

                    // Notify user of success via a toast message
                    showToastMessage(true, bookId);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.toString());

                    // Notify user of failure via a toast message
                    showToastMessage(false, bookId);
                }
            });
        }
    };

    private void getBookList() {
        Call<List<Book>> call = mBookApi.listBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Response<List<Book>> response) {
                if (response.isSuccess()) {
                    List<Book> books = response.body();
                    final BookAdapter bookAdapter = new BookAdapter(books);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookAdapter.setOnClickListener(mOnClickListener);
                            bookRecyclerView.setAdapter(bookAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showToastMessage(boolean wasSuccessful, int bookId) {
        int messageId = (wasSuccessful) ? R.string.book_added_success : R.string.book_added_failure;
        String message = String.format(getResources().getString(messageId), bookId);
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
