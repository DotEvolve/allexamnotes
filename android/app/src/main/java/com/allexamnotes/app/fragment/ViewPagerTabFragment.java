package com.allexamnotes.app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allexamnotes.app.Config;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.postlist.PostListFragment;
import com.allexamnotes.app.fragment.youtube.PlaylistFragment;
import com.allexamnotes.app.fragment.youtube.VideoFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerTabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    private String screen,data;
    private String passedData[];

    private ArrayList<Integer> postIds = new ArrayList<>();
    private ArrayList<String> postTitles = new ArrayList<>();

    public ViewPagerTabFragment() {
        // Required empty public constructor
    }

    public static ViewPagerTabFragment newInstance(String screen,String data) {
        ViewPagerTabFragment fragment = new ViewPagerTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("screen",screen);
        bundle.putString("data",data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            screen = getArguments().getString("screen");
            data = getArguments().getString("data");
        }
    }

    private void processData(String screen,String data){
        postIds.clear();
        postTitles.clear();
        try {
            //for only one data
            if (screen.equals("posts")){
                if (data.contains(",")){
                    //multiple items in posts
                    String[] items = data.split(",");
                    //items is in the format Title:ID
                    for (String item : items) {
                        if (item.contains(":")) {
                            String[] temp = item.split(":");
                            if (temp.length == 2) {
                                postTitles.add(temp[0]);
                                postIds.add(Integer.parseInt(temp[1]));

                            }
                        }
                    }
                }else {
                    tabLayout.setVisibility(View.GONE);
                    String[] temp = data.split(":");
                    if (temp.length==2){
                        postTitles.add(temp[0]);
                        postIds.add(Integer.parseInt(temp[1]));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager_tab, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new TabPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //add fragments to viewpager

        processData(screen,data);

        if (screen.equals("youtube")) {
            if (data==null){
                data = "latest,playlist";
            }
            passedData = data.split(",");
            if (passedData.length == 1) {
                tabLayout.setVisibility(View.GONE);
            }

            for (String passedDatum : passedData) {
                if (passedDatum.equals("latest")) {
                    adapter.addFragment(VideoFragment.newInstance(Config.YT_LATEST_CHANEL_ID), getResources().getString(R.string.youtube_latest_playlist_name));
                }else if (passedDatum.equals("playlist")) {
                    adapter.addFragment(PlaylistFragment.newInstance(), getResources().getString(R.string.youtube_playlist_string));
                }
            }
        } else if (screen.equals("posts")) {
            for (int i = 0; i < postIds.size(); i++) {
                fragments.add(PostListFragment.newInstance(String.valueOf(postIds.get(i)), null, null, null));
                titles.add(postTitles.get(i));
                adapter.addFragment(fragments.get(i), titles.get(i));
            }
        }

        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
}
