package com.jameydeorio.bionicflamingo.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.adapters.BookAdapter;
import com.jameydeorio.bionicflamingo.api.BookApi;
import com.jameydeorio.bionicflamingo.api.QueueApi;
import com.jameydeorio.bionicflamingo.api.ServiceGenerator;
import com.jameydeorio.bionicflamingo.database.BookDataSource;
import com.jameydeorio.bionicflamingo.models.Book;
import com.jameydeorio.bionicflamingo.models.QueueItem;
import com.jameydeorio.bionicflamingo.ui.activities.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YourQueueFragment extends Fragment {
    private static final String TAG = YourQueueFragment.class.getSimpleName();

    private RecyclerView mBookRecyclerView;
    private BookAdapter mBookAdapter;
    private BookDataSource mBookDataSource;
    private ArrayList<Book> mBooks = new ArrayList<>();

    private QueueApi mQueueApi;
    private BookApi mBookApi;

    private TextView mEmptyQueueLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.on_device_fragment, container, false);

        mQueueApi = ServiceGenerator.createService(QueueApi.class);
        mBookApi = ServiceGenerator.createService(BookApi.class);

        mEmptyQueueLabel = (TextView) view.findViewById(R.id.emptyQueueLabel);

        mBookRecyclerView = (RecyclerView) view.findViewById(R.id.onDeviceRecyclerView);
        mBookDataSource = new BookDataSource(getActivity());
        mBookAdapter = new BookAdapter(mBooks);
        mBookAdapter.setOnClickListener(mOnClickListener);
        mBookRecyclerView.setAdapter(mBookAdapter);
        mBookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getQueuedBooks();

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Book.BOOK_ID_KEY, Integer.parseInt(v.getTag().toString()));
            startActivity(intent);
        }
    };

    private void getQueuedBooks() {
        Call<List<QueueItem>> call = mQueueApi.getQueue();
        call.enqueue(new Callback<List<QueueItem>>() {
            @Override
            public void onResponse(Response<List<QueueItem>> response) {
                if (!response.isSuccess()) {
                    Log.e(TAG, response.errorBody().toString());
                    return;
                }

                List<QueueItem> queueItems = response.body();

                if (queueItems.size() > 0) {
                    mEmptyQueueLabel.setVisibility(View.GONE);
                } else {
                    mEmptyQueueLabel.setVisibility(View.VISIBLE);
                }

                for (QueueItem queueItem : response.body()) {
                    Call<Book> call = mBookApi.getBook(queueItem.getBook());
                    call.enqueue(new Callback<Book>() {
                        @Override
                        public void onResponse(Response<Book> response) {
                            if (!response.isSuccess()) {
                                Log.e(TAG, response.errorBody().toString());
                                return;
                            }
                            mBooks.add(response.body());
                            BookAdapter bookAdapter = new BookAdapter(mBooks);
                            bookAdapter.setOnClickListener(mOnClickListener);
                            mBookRecyclerView.setAdapter(bookAdapter);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
