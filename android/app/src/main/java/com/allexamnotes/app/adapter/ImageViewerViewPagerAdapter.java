package com.allexamnotes.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<>();

    public ImageViewerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImageViewerViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
