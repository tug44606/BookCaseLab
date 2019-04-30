package com.temple.edu.bookcase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface, BookDetailsFragment.AudioServiceInterface {

    BookDetailsFragment dFragment;
    ViewPagerFragment vpFragment;
    BookListFragment lFragment;

    Button button;
    ImageButton downloadButton;
    ImageButton deleteButton;
    EditText searchBar;
    String userSearch;

    // to store the json objects
    JSONArray jsonList = null;

    boolean portrait;

    ArrayList<Book> bookList = new ArrayList<>();

    // Service
    boolean connection;
    AudiobookService.MediaControlBinder mediaControlBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lFragment = new BookListFragment();
        vpFragment = new ViewPagerFragment();
        dFragment = new BookDetailsFragment();
        bookList = new ArrayList<>();

        searchBar = findViewById(R.id.searchText);
        searchBar.clearFocus();
        button = findViewById(R.id.searchButton);

        bindService(new Intent(this, AudiobookService.class), serviceConnection, BIND_AUTO_CREATE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            portrait = true;
            // set single view fragment
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_main, vpFragment).
                    addToBackStack(null).
                    commit();
        }
        else {
            portrait = false;
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


        Log.d("OnCreate: MainActivity: ", "Setup completed");


        // button listener that passes the input string as parameter and return new json array
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSearch = searchBar.getText().toString();
                getBooks(userSearch);
                if(bookList != null){
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        vpFragment.updateViewPager(bookList);
                    }
                    else {
                        lFragment.setBookList(bookList);
                    }
                }

            }
        });

    }

    public void getBooks(final String search) {
        Thread IOThread;


        Log.d("getBooks(): ", "Started");

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


                    Message msg = Message.obtain();
                    msg.obj = jsonBookString.toString();
                    jsonHandler.sendMessage(msg);

                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("getBooks() Return: ", jsonBookString.toString());
            }
        });

        IOThread.start();
    }


    Handler jsonHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                jsonList = new JSONArray((String) msg.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bookList.clear();
            for(int i = 0; i < jsonList.length(); i++){
                try {
                    bookList.add(new Book(jsonList.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(portrait) {
                vpFragment.updateViewPager(bookList);
            } else {
                lFragment.setBookList(bookList);
            }
            return false;
        }
    });




    @Override
    public void pickBook(Book bookObject) {
        dFragment.updateBook(bookObject);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = ((AudiobookService.MediaControlBinder) service);
            connection = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connection = false;
            mediaControlBinder = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection) {
            unbindService(serviceConnection);
            connection = false;
        }
    }

    @Override
    public void playBook(int id) {
        mediaControlBinder.play(id);
    }

    @Override
    public void pauseBook() {
        mediaControlBinder.pause();
    }

    @Override
    public void stopBook() {
        mediaControlBinder.stop();
    }

    @Override
    public void seekBook(int position) {
        mediaControlBinder.seekTo(position);
    }

    @Override
    public void setProgress(Handler progressHandler) {
        mediaControlBinder.setProgressHandler(progressHandler);
    }
}
