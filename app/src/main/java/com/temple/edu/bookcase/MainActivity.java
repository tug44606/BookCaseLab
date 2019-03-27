package com.temple.edu.bookcase;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    boolean isPortrait; // tell whether single pane or double
    BookDetailsFragment dFragment;
    ViewPagerFragment vpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BookListFragment listFragment = new BookListFragment();
        vpFragment = new ViewPagerFragment();
        dFragment = new BookDetailsFragment();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            // set single view fragment
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_main, vpFragment).
                    addToBackStack(null).
                    commit();
        }
        else {
            // set split view with fragments
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_left, listFragment).
                    addToBackStack(null).
                    commit();

            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_right, dFragment).
                    addToBackStack(null).
                    commit();
        }

    }

    @Override
    public void pickBook(String bTitle) {
        dFragment.setBook(bTitle);
    }
}
