package com.itsanubhav.wordroid4.fragment.youtube;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import android.widget.Toast;

import com.itsanubhav.wordroid4.Config;
import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.MainActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.listeners.OnItemClickListener;
import com.itsanubhav.wordroid4.others.EndlessScrollListener;
import com.itsanubhav.libdroid.model.playlist.ItemsItem;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements OnItemClickListener {

    private PlaylistViewModel mViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PlaylistAdapter adapter;
    private List<ItemsItem> itemsItems = new ArrayList<>();
    private View loadingView;

    private String pageToken;
    private boolean loadFlag;


    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment, container, false);
        loadingView = view.findViewById(R.id.loadingView);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PlaylistAdapter(getContext(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (pageToken!=null&&!pageToken.equals("")&&loadFlag){
                    loadPlaylist();
                }else {
                    adapter.removeLoadingItem();
                }
            }
        });

        //
        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageToken = null;
            loadPlaylist();
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        loadPlaylist();
    }

    private void loadPlaylist(){
        loadFlag = false;
        mViewModel.getPlaylist(Config.YT_API_KEY,Config.chanelID,pageToken).observe(this, playlist -> {
            loadFlag = true;
            loadingView.setVisibility(View.GONE);
            if (playlist!=null){
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (pageToken==null){
                    itemsItems.clear();
                    adapter.clear();
                }
                itemsItems.addAll(playlist.getItems());
                pageToken = playlist.getNextPageToken();
                adapter.addObjects(playlist.getItems());
            }
        });
    }

    @Override
    public void onClick(int position) {
        if (getActivity() instanceof ContainerActivity){
            try {
                ContainerActivity a = (ContainerActivity)getActivity();
                assert a != null;
                String title = itemsItems.get(position).getSnippet().getTitle();
                a.addFragment(VideoFragment.newInstance(itemsItems.get(position).getId()),title);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (getActivity() instanceof MainActivity){
            Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("title",itemsItems.get(position).getSnippet().getTitle());
            intent.putExtra("screen","videos");
            intent.putExtra("playlist",itemsItems.get(position).getId());
            startActivity(intent);
        }

    }
}
