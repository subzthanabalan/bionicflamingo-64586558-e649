package com.jameydeorio.bionicflamingo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jameydeorio.bionicflamingo.R;
import com.jameydeorio.bionicflamingo.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final Book[] mBooks;
    private View.OnClickListener mOnClickListener;

    public BookAdapter(List<Book> books) {
        mBooks = books.toArray(new Book[books.size()]);
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bindBook(mBooks[position]);
    }

    @Override
    public int getItemCount() {
        return mBooks.length;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitleLabel;

        public BookViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            bookTitleLabel = (TextView) itemView.findViewById(R.id.bookTitleLabel);
            if (onClickListener != null) {
                itemView.setOnClickListener(onClickListener);
            }
        }

        public void bindBook(Book book) {
            bookTitleLabel.setText(book.getTitle());
            itemView.setTag(book.getIdentifier());
        }
    }
}
