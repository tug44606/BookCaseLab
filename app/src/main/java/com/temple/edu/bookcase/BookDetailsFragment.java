package com.temple.edu.bookcase;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {

    // variables
    TextView tv;
    ImageView iv;
    String bTitle;

    String title, author, published;
    Book bookObject;

    // constructor
    public BookDetailsFragment(){

    }

    // pass it to the next detail fragment for onCreate()
    public static BookDetailsFragment setDetailFragmentParams(Book bookList) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bookPick", bookList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            bookObject = getArguments().getParcelable("bookPick");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booktext_fragment, container, false);
        tv = view.findViewById(R.id.bookTitleTextView);
        iv = view.findViewById(R.id.bookImageView);
        // Add these to layout after
        //button = view.findViewById(R.id.button);
        //searchBar = view.findViewById(R.id.searchBar);
        if(getArguments() != null) {
            author = bookObject.getAuthor();
            title = bookObject.getTitle();
            published = bookObject.getPublished();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                tv.setText(title);
                tv.append(" by " + author);
                tv.append(", " + published);
            }
            else {
                tv.setText(title);
                tv.append(" by " + author);
                //tv.append(", " + published);
            }

            String imageURL = bookObject.getCoverURL();
            Picasso.get().load(imageURL).into(iv);
        }

        return view;
    }

    public void updateBook(Book bookObject){
        author = bookObject.getAuthor();
        title = bookObject.getTitle();
        published = bookObject.getPublished();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            tv.setText(title);
            tv.append(" by " + author);
            tv.append(", " + published);
        }
        else {
            tv.setText(title);
            tv.append(" by " + author);
            //tv.append(", " + published);
        }

        String imageURL = bookObject.getCoverURL();
        Picasso.get().load(imageURL).into(iv);
    }

}
