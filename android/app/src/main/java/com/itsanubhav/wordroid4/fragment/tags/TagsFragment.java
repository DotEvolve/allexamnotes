package com.itsanubhav.wordroid4.fragment.tags;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.MainActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.fragment.postlist.PostListFragment;
import com.itsanubhav.wordroid4.listeners.OnItemClickListener;
import com.itsanubhav.wordroid4.others.EndlessScrollListener;
import com.itsanubhav.wordroid4.others.SpaceItemDecoration;
import com.itsanubhav.libdroid.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsFragment extends Fragment implements OnItemClickListener {

    private TagsViewModel mViewModel;

    private int currentPage = 1;
    private int totalPages;
    boolean loadFlag;
    private String searchQuery;

    View loadingLayout;
    View noContentLayout;

    private List<Tag> tagList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TagsAdapter adapter;

    public static TagsFragment newInstance(String searchQuery) {
        TagsFragment tagsFragment = new TagsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query",searchQuery);
        tagsFragment.setArguments(bundle);
        return tagsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null){
            searchQuery = getArguments().getString("query");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tags_fragment, container, false);

        noContentLayout = rootView.findViewById(R.id.no_contents_error_layout);
        loadingLayout = rootView.findViewById(R.id.loadingView);

        adapter = new TagsAdapter(getContext(),this);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            fetchTags();
        });
        recyclerView = rootView.findViewById(R.id.tagsRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)){
                    case TagsAdapter.TAG_ITEM:
                        return 1;
                    default:
                        return 2;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(animationController);
        recyclerView.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (currentPage<=totalPages&&loadFlag){
                    fetchTags();
                }else {
                    adapter.removeLoaidngView();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TagsViewModel.class);
        fetchTags();
    }

    private void fetchTags(){
        loadFlag = false;
        mViewModel.getTags(currentPage,searchQuery).observe(this, tagResponse -> {
            if (tagResponse!=null){
                loadingLayout.setVisibility(View.GONE);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                loadFlag = true;
                totalPages = tagResponse.getTotalPages();
                if (currentPage==1){
                    tagList.clear();
                    adapter.clearItems();
                }
                if (tagResponse.getTotalPages()>0) {
                    tagList.addAll(tagResponse.getTags());
                    currentPage++;
                    adapter.addItems(tagResponse.getTags());
                }else {
                    noContentLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(int position) {
        if (getActivity() instanceof ContainerActivity){
            try {
                ContainerActivity a = (ContainerActivity)getActivity();
                assert a != null;
                String title = tagList.get(position).getName();
                a.addFragment(PostListFragment.newInstance(null,null,String.valueOf(tagList.get(position).getId()),null),title);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (getActivity() instanceof MainActivity){
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("screen","posts");
            intent.putExtra("title",tagList.get(position).getName());
            intent.putExtra("tags",String.valueOf(tagList.get(position).getId()));
            startActivity(intent);
        }
    }
}
