package com.temple.edu.bookcase;

import android.content.res.Configuration;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    BookDetailsFragment dFragment;
    ViewPagerFragment vpFragment;
    JSONArray jsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BookListFragment listFragment = new BookListFragment();
        vpFragment = new ViewPagerFragment();
        dFragment = new BookDetailsFragment();


        // Get books
        URL url;
        BufferedReader reader;

        // this will hold entire json from the given url
        StringBuilder jsonBookString = new StringBuilder();
        String buffer;

        // will display all the books currently in the database
        String urlString = "https://kamorris.com/lab/audlib/booksearch.php?search=";

        try {
            url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((buffer = reader.readLine()) != null) {
                jsonBookString.append(buffer);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert StringBuilder to String to pass to jsonArray constructor

        try {
            jsonList = new JSONArray(jsonBookString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            vpFragment.updateViewPager(jsonList);
        }
        else {
            listFragment.setBookList(jsonList);
        }

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

        // button listener that passes the input string as parameter and return new json array

    }

    @Override
    public void pickBook(Book jsonBook) {
        dFragment.updateBook(jsonBook);
    }
}
