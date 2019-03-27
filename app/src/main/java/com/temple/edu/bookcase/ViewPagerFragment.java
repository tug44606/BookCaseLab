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

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    // variables
    ViewPager vp;
    BookDetailsFragment dFragment;
    PagerAdapter pAdapter;

    // constructor
    public ViewPagerFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  Inflate layout
        View v = inflater.inflate(R.layout.viewpager_fragment, container, false);
        vp = v.findViewById(R.id.viewPager);

        // Get books
        Resources res = this.getResources();
        final String[] bookList = res.getStringArray(R.array.bookList);

        dFragment = new BookDetailsFragment();
        vp = v.findViewById(R.id.viewPager);

        // new page adapter
        // is abstract so cant be instantiated, need to implement methods
        pAdapter = new PagerAdapter(getChildFragmentManager());

        // Loop through list and create detail fragments instance and add to pager
        for(int i = 0; i < bookList.length; i++){
            dFragment = BookDetailsFragment.detailFragmentFactory(bookList[i]);
            pAdapter.add(dFragment);
        }

        // set view pager
        vp.setAdapter(pAdapter);

        return v;
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
