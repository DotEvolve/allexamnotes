package com.allexamnotes.app.fragment.homepage;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allexamnotes.app.R;
import com.allexamnotes.app.adapter.ParentAdapter;
import com.allexamnotes.app.listeners.OnHomePageItemClickListener;
import com.allexamnotes.libdroid.model.WorDroidSection;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private HomePageViewModel mViewModel;
    private RecyclerView recyclerView;
    private LinearLayout loadingView;
    private ParentAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnHomePageItemClickListener onHomePageItemClickListener;
    private boolean shuffle = false;
    private List<WorDroidSection> sectionList = new ArrayList<>();

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_fragment, container, false);
        loadingView = view.findViewById(R.id.loadingView);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.postListRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this::loadHomePage);
        onHomePageItemClickListener = new OnHomePageItemClickListener() {
            @Override
            public void onClick(int position,String type) {


            }

            @Override
            public void onLongClick(int position,String screen) {

            }
        };

        mAdapter = new ParentAdapter(getContext(),onHomePageItemClickListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        loadHomePage();
    }

    private void loadHomePage(){
        mViewModel.getHomePageSections().observe(this, worDroidSections -> {
            loadingView.setVisibility(View.GONE);
            if (worDroidSections!=null){
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                mAdapter.clearItems();
                mAdapter.addObject(worDroidSections);
            }else {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

        });
    }

}
