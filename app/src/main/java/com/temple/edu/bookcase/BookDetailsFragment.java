package com.temple.edu.bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookDetailsFragment extends Fragment {

    // variables
    TextView tv;
    String bTitle;

    // constructor
    public BookDetailsFragment(){

    }

    // create a factory that makes new instance every time
    public static BookDetailsFragment detailFragmentFactory(String book) {
        BookDetailsFragment fragment = new BookDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("bookPick", book);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            // get book title when fragment create
            bTitle = getArguments().getString("bookPick");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.booktext_fragment, container, false);
        tv = v.findViewById(R.id.bookTitleTextView);

        // Set text of view to book title
        tv.setText(bTitle);

        return v;
    }
}
