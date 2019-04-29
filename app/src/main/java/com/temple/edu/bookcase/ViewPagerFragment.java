package com.temple.edu.bookcase;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    // variables
    ViewPager vp;
    BookDetailsFragment dFragment;
    PagerAdapter pAdapter;

    Book bookObject;
    ArrayList<Book> bookList;
    ArrayList<BookDetailsFragment> bdFragment;
    // constructor
    public ViewPagerFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate layout
        View v = inflater.inflate(R.layout.viewpager_fragment, container, false);

        // Set adapter and view pager
        pAdapter = new PagerAdapter(getFragmentManager());
        bookList = new ArrayList<>();
        vp = v.findViewById(R.id.viewPager);
        bdFragment = new ArrayList<>();

        vp.setAdapter(pAdapter);


        Log.d("ViewPagerFragment.onCreateView(): ", "Successful");
        return v;
    }


    public void updateViewPager(final ArrayList<Book> bookArray){
        bdFragment.clear();
        for(int i = 0; i < bookArray.size(); i++) {
            bookObject = bookArray.get(i);
            dFragment = BookDetailsFragment.setDetailFragmentParams(bookObject);
            bdFragment.add(dFragment);
        }
        pAdapter.addBooks(bdFragment);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<BookDetailsFragment> pFragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pFragments = new ArrayList<>();
        }

        public void addBooks(ArrayList<BookDetailsFragment> books) {
            pFragments.clear();
            pFragments.addAll(books);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i){
            return pFragments.get(i);
        }

        @Override
        public int getCount() {
            return pFragments.size();
        }
    }
}
