package com.temple.edu.bookcase;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    BookDetailsFragment dFragment;
    ViewPagerFragment vpFragment;
    BookListFragment lFragment;

    Button button;
    EditText searchBar;
    String userSearch;

    // to store the json objects
    JSONArray jsonList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lFragment = new BookListFragment();
        vpFragment = new ViewPagerFragment();
        dFragment = new BookDetailsFragment();

        searchBar = findViewById(R.id.searchText);
        button = findViewById(R.id.searchButton);


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
                    replace(R.id.container_left, lFragment).
                    addToBackStack(null).
                    commit();

            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_right, dFragment).
                    addToBackStack(null).
                    commit();
        }

        getBooks(userSearch);

        if(jsonList != null){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                vpFragment.updateViewPager(jsonList);
            }
            else {
                lFragment.setBookList(jsonList);
            }
        }


        // button listener that passes the input string as parameter and return new json array
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSearch = searchBar.getText().toString();
                getBooks(userSearch);
                if(jsonList != null){
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        vpFragment.updateViewPager(jsonList);
                    }
                    else {
                        lFragment.setBookList(jsonList);
                    }
                }

            }
        });
    }

    public void getBooks(final String search) {
        Thread IOThread;

        IOThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // will display all the books currently in the database
                String jsonURL = "https://kamorris.com/lab/audlib/booksearch.php?search=" + search;
                URL url;
                StringBuilder jsonBookString = new StringBuilder();

                // this will hold entire json from the given url
                BufferedReader reader = null;
                String buffer;

                try {
                    url = new URL(jsonURL);
                    reader = new BufferedReader(new InputStreamReader(url.openStream()));

                    while ((buffer = reader.readLine()) != null) {
                        jsonBookString.append(buffer);
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("GET JSON: ", jsonBookString.toString());

                // Convert StringBuilder to String to pass to jsonArray constructor

                try {
                    jsonList = new JSONArray(jsonBookString.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        IOThread.start();
    }



    @Override
    public void pickBook(Book jsonBook) {
        dFragment.updateBook(jsonBook);
    }
}
