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

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.adapters.BookAdapter;
import com.jameydeorio.bionicflamingo.api.BookApi;
import com.jameydeorio.bionicflamingo.api.QueueApi;
import com.jameydeorio.bionicflamingo.api.ServiceGenerator;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);

        mBookApi = ServiceGenerator.createService(BookApi.class);

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
            int bookId = Integer.parseInt(v.getTag().toString());
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
                    Log.d(TAG, String.format("Downloaded queue item %s", queueItem.getId()));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.toString());
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
}
