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
        vp = v.findViewById(R.id.viewPager);

        return v;
    }


    public void updateViewPager(JSONArray bookArray){
        for(int i = 0; i < bookArray.length(); i++){
            try {
                // update
                pAdapter.getItemPosition(i);
                pAdapter.notifyDataSetChanged();

                // get book object
                JSONObject jsonBook = bookArray.getJSONObject(i);
                bookObject = new Book(jsonBook);

                // get fragment from the static method
                dFragment = BookDetailsFragment.setDetailFragmentParams(bookObject);
                pAdapter.add(dFragment);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // set view pager
        vp.setAdapter(pAdapter);
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
