package com.temple.edu.bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    Context c;
    ArrayList<Book> books;

    public BookAdapter(Context c, ArrayList<Book> books){
        this.c = c;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(c);
        textView.setText(books.get(position).getTitle());

        return textView;
    }
}
