package com.temple.edu.bookcase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    boolean isPortrait; // tell whether single pane or double
    BookDetailsFragment dFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.container_right) == null){
            isPortrait = true;
        }
        else {
            isPortrait = false;
        }


        BookListFragment listFragment = new BookListFragment();

        dFragment = new BookDetailsFragment();

        if(isPortrait){
            // set single view fragment
        }
        else {
            // set split view with fragments

        }

    }


    @Override
    public void pickBook(String bookTitle) {

    }
}
