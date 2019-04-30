package com.temple.edu.bookcase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;


public class BookListFragment extends Fragment {

    // interface to be implemented
    public BookListInterface chooseBook;

    AudiobookService.MediaControlBinder mediaControlBinder;

    // variables
    Context c;
    ListView lv;

    Book book;

    ArrayList<Book> bookList;
    BookAdapter bAdapter;

    // constructor
    public BookListFragment(){
    }

    // interface
    public interface BookListInterface{
        void pickBook(Book bookObject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // on creation
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ArrayList<String> bookList = new ArrayList<>();
        // populate list
        //bookList.addAll(Arrays.asList("com.temple.edu.bookcase.Book 1", "com.temple.edu.bookcase.Book 2", "com.temple.edu.bookcase.Book 3", "com.temple.edu.bookcase.Book 4", "com.temple.edu.bookcase.Book 5", "com.temple.edu.bookcase.Book 6", "com.temple.edu.bookcase.Book 7", "com.temple.edu.bookcase.Book 8",
        //        "com.temple.edu.bookcase.Book 9", "com.temple.edu.bookcase.Book 10"));


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.booklist_fragment, container, false);
        lv = v.findViewById(R.id.bookListView);

        // get the book names and set adapter
        /*
        Resources res = this.getResources();
        final String[] books = res.getStringArray(R.array.bookList);
        lv.setAdapter(new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, books));

        // when a book is selected from list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // send to the interface once book title is retrieved
                String bTitle = (String) parent.getItemAtPosition(position);
                ((BookListInterface) c).pickBook(bTitle);
            }
        });
        */

        // create new array of books
        bookList = new ArrayList<>();


        return v;
    }

    public void setBookList(final ArrayList<Book> bookArray){
        bAdapter = new BookAdapter(c, bookArray);
        bAdapter.notifyDataSetChanged();
        lv.setAdapter(bAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 book = bookArray.get(position);
                ((BookListInterface) c).pickBook(book);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListInterface) {
            chooseBook = (BookListInterface) context;
        } else {
            throw new RuntimeException(context.toString());
        }
        this.c = context;
    }


}
