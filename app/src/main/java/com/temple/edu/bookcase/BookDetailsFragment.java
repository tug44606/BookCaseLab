package com.temple.edu.bookcase;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;
import edu.temple.audiobookplayer.AudiobookService;

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
