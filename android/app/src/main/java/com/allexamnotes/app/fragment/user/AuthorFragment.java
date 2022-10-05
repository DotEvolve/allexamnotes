package com.allexamnotes.app.fragment.user;

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

import com.allexamnotes.app.R;
import com.allexamnotes.app.others.EndlessScrollListener;

public class AuthorFragment extends Fragment {

    private AuthorViewModel mViewModel;

    private int CURRENT_PAGE = 1;
    private String searchQuery = null;
    private int totalPages;
    private boolean loadFlag;

    private RecyclerView recyclerView;
    private AuthorAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static AuthorFragment newInstance() {
        return new AuthorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.author_fragment, container, false);

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CURRENT_PAGE = 1;
                getUsers();
            }
        });
        adapter = new AuthorAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (CURRENT_PAGE<=totalPages&&loadFlag){
                    getUsers();
                }else {
                    adapter.removeLoadingView();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AuthorViewModel.class);
        getUsers();
    }

    private void getUsers(){
        loadFlag = false;
        mViewModel.getUsers(CURRENT_PAGE,searchQuery).observe(this, usersResponse -> {
            if (usersResponse.getUsers()!=null){
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (CURRENT_PAGE==1)
                    adapter.clearItem();
                loadFlag=true;
                totalPages = usersResponse.getTotalPages();
                CURRENT_PAGE++;
                adapter.addItems(usersResponse.getUsers());

            }
        });
    }

}
