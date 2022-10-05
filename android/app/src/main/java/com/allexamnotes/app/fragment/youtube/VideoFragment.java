package com.allexamnotes.app.fragment.youtube;

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

import com.allexamnotes.app.Config;
import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.allexamnotes.app.others.EndlessScrollListener;

public class VideoFragment extends Fragment implements OnItemClickListener {

    private VideoViewModel mViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VideoAdapter adapter;
    private String pageToken;
    private boolean loadFlag;
    private String PLAYLIST_ID;
    private boolean latestVideos;
    private View loadingView;

    public static VideoFragment newInstance(String playlist) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("playlist",playlist);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            PLAYLIST_ID = getArguments().getString("playlist");
            latestVideos = getArguments().getBoolean("latest");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_video_fragment, container, false);
        loadingView = view.findViewById(R.id.loadingView);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new VideoAdapter(getContext(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (pageToken!=null&&!pageToken.equals("")&&loadFlag){
                    loadVideos();
                }else {
                    adapter.removeLoadingItem();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageToken = null;
            loadVideos();
        });
        return view;
    }

    private void loadVideos(){
            loadFlag = false;
            mViewModel.getVideos(Config.YT_API_KEY, Config.chanelID,pageToken,PLAYLIST_ID).observe(this, playlist -> {
                loadingView.setVisibility(View.GONE);
                loadFlag = true;
                if (playlist!=null){
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    if (pageToken==null){
                        adapter.clear();
                    }
                    pageToken = playlist.getNextPageToken();
                    adapter.addObjects(playlist.getItems());

                }
            });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        loadVideos();
    }

    @Override
    public void onClick(int position) {

    }
}
