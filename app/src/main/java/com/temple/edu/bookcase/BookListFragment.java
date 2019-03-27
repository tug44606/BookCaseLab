package com.temple.edu.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class BookListFragment extends Fragment {

    // interface to be implemented by main activity
    public BookListInterface chooseBook;

    // variables
    ListView lv;
    Context c;


    // constructor
    public BookListFragment(){
    }

    // interface
    public interface BookListInterface{
        void pickBook(String bTitle);
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

    @Override
    public void onDetach() {
        super.onDetach();
        chooseBook = null;
    }


    // on creation
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ArrayList<String> bookList = new ArrayList<>();
        // populate list
        //bookList.addAll(Arrays.asList("Book 1", "Book 2", "Book 3", "Book 4", "Book 5", "Book 6", "Book 7", "Book 8",
        //        "Book 9", "Book 10"));


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.booklist_fragment, container, false);
        lv = v.findViewById(R.id.bookListView);

        // get the book names and set adapter
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


        return v;
    }
}
