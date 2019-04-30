package com.temple.edu.bookcase;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import edu.temple.audiobookplayer.AudiobookService;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class BookDetailsFragment extends Fragment {

    // variables
    TextView tv;
    ImageView iv;
    String bTitle;

    String title, author, published;
    Book bookObject;

    private AudioServiceInterface asInterface;
    Context c;

    ImageButton playButton;
    ImageButton stopButton;
    ImageButton pauseButton;
    ImageButton downloadButton;
    ImageButton deleteButton;
    SeekBar seekBar;
    TextView progressText;

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

        playButton = view.findViewById(R.id.playButton);
        stopButton = view.findViewById(R.id.stopButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        seekBar = view.findViewById(R.id.seekBar);
        progressText = view.findViewById(R.id.progressText);
        deleteButton = view.findViewById(R.id.deleteButton);
        downloadButton = view.findViewById(R.id.downloadButton);

        // Add these to layout after
        //button = view.findViewById(R.id.button);
        //searchBar = view.findViewById(R.id.searchBar);
        if(getArguments() != null) {
            updateBook(bookObject);
        }

        return view;
    }

    public void updateBook(final Book bookObject){
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

        // seek bar is same length as book duration rather than 0 - 100
        seekBar.setMax(bookObject.getDuration());


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AudioServiceInterface) c).playBook(bookObject.getId());
                ((AudioServiceInterface) c).setProgress(pHandler);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AudioServiceInterface) c).pauseBook();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                progressText.setText("0s");
                ((AudioServiceInterface) c).stopBook();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    progressText.setText("" + progress + "s");
                    ((AudioServiceInterface) c).seekBook(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BookDetailsFragment.downloadButton.onClick: ", "Book ID = " + bookObject.getId());
                downloadBook(bookObject.getId());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BookDetailsFragment.downloadButton.onClick: ", "Book ID = " + bookObject.getId());
                String filename = "Book" + bookObject.getId() + ".mp3";
                File file = new File(c.getFilesDir(), filename);

                if(file.exists()){
                    if(file.delete()){
                        Log.d("deleteButton.onClick(): ", "Book deleted!");
                    }
                    else {
                        Log.d("deleteButton.onClick(): ", "Error deleting book!");
                    }
                } else {
                    Log.d("deleteButton.onClick(): ", "Book is not downloaded!");
                }


            }
        });
    }



    private void downloadBook(int bookId) {
        downloadAsync task = new downloadAsync(bookId, getContext());
        task.execute();
    }

    private class downloadAsync extends AsyncTask<String, String, String> {
        private int bookId;
        private Context context;

        downloadAsync(int bookId, Context context) {
            this.bookId = bookId;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String filename = "Book" + this.bookId + ".mp3";
            int count;
            try {
                URL url = new URL("https://kamorris.com/lab/audlib/download.php?id=" + bookId);

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File file = new File(context.getFilesDir(), filename);

                if(file.exists()){
                    Log.d("downloadAsync(): ", "File already downloaded!");
                } else {
                    file.createNewFile();

                    Log.d("downloadAsync(): context.getFilesDir - " , context.getFilesDir().toString());
                    OutputStream output = new FileOutputStream(file.getPath());
                    Log.d("DownloadAsync(): Filename - ", file.getPath());


                    byte data[] = new byte[1024];

                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("DownloadAsync().onPostExecute: ", "Success!");
            Toast.makeText(context, "Download finished", Toast.LENGTH_LONG).show();
        }
    }

    Handler pHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            updateSeekbar(msg.what);
            return false;
        }
    });

    public void updateSeekbar(int time){
        seekBar.setProgress(time);
        progressText.setText("" + time + "s");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListFragment.BookListInterface) {
            asInterface = (AudioServiceInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AudioServiceInterface");
        }
        this.c = context;
    }

    public interface AudioServiceInterface{
        void playBook(int id);
        void pauseBook();
        void stopBook();
        void seekBook(int position);
        void setProgress(Handler progressHandler);
    }

}
