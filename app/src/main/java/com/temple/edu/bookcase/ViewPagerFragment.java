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
    // constructor
    public ViewPagerFragment(){

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
        vp.setAdapter(pAdapter);

        return v;
    }


    public void updateViewPager(final ArrayList bookArray){
        bookList.clear();
        bookList.addAll(bookArray);

        for(int i = 0; i < bookList.size(); i++) {
            bookObject = bookList.get(i);
            dFragment = BookDetailsFragment.setDetailFragmentParams(bookObject);
            pAdapter.add(dFragment);
            pAdapter.notifyDataSetChanged();
        }

        pAdapter.getItemPosition(bookObject);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<BookDetailsFragment> pFragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pFragments = new ArrayList<>();
        }

        public void add(BookDetailsFragment fragment){
            pFragments.add(fragment);
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
