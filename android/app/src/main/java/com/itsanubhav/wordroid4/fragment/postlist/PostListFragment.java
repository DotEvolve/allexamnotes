package com.itsanubhav.wordroid4.fragment.postlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsanubhav.wordroid4.MainApplication;
import com.itsanubhav.wordroid4.PostContainerActivity;
import com.itsanubhav.wordroid4.listeners.OnHomePageItemClickListener;
import com.google.android.material.appbar.AppBarLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.adapter.PostListAdapter;
import com.itsanubhav.wordroid4.others.EndlessScrollListener;
import com.itsanubhav.wordroid4.others.SpaceItemDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.itsanubhav.libdroid.model.category.Category;
import com.itsanubhav.libdroid.model.posts.Posts;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class PostListFragment extends Fragment implements OnHomePageItemClickListener {

    private static boolean SLIDER_ENABLED = true;
    private static final int SLIDER_SIZE = 5;
    private static final boolean postListMode = true; // true = list & false = grid
    private int firstItemPosition = 5;

    private int CURRENT_PAGE = 1;
    private String TAGS;
    private String AUTHOR;
    private String SEARCH_QUERY;
    private String CATEGORIES;
    private int totalPages;
    private boolean loadFlag;

    private PostListViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private View loadingLayout;
    private View noContentLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Posts> masterList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    //private View noInternetView;
    private OnHomePageItemClickListener onHomePageItemClickListener;

    private ImageView transitionImageView;

    public static PostListFragment newInstance() {
        return new PostListFragment();
    }

    public static PostListFragment newInstance(String categories,String author,String tags,String search){
        Bundle bundle = new Bundle();
        bundle.putString("categories",categories);
        bundle.putString("author",author);
        bundle.putString("tags",tags);
        bundle.putString("search",search);
        PostListFragment postListFragment = new PostListFragment();
        postListFragment.setArguments(bundle);
        return postListFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void readBundle(Bundle bundle){
        if (bundle!=null){
            CATEGORIES = bundle.getString("categories");
            SEARCH_QUERY = bundle.getString("search");
            AUTHOR = bundle.getString("author");
            TAGS = bundle.getString("tags");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_list_fragment, container, false);
        loadingLayout = view.findViewById(R.id.loadingView);
        noContentLayout = view.findViewById(R.id.no_contents_error_layout);
        //noInternetView = view.findViewById(R.id.no_internet_error);
        mRecyclerView = view.findViewById(R.id.simpleHomeRecyclerView);
        mAdapter = new PostListAdapter(getContext(),this);
        swipeRefreshLayout = view.findViewById(R.id.postListRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        transitionImageView = view.findViewById(R.id.featuredImage);

        readBundle(getArguments());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            CURRENT_PAGE = 1;
            fetchPosts();
        });

        //Toolbar
        AppBarLayout appBarLayout = view.findViewById(R.id.mainActionBar);
        appBarLayout.setVisibility(GONE);

        mAdapter.setLayoutManager(postListMode);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        mRecyclerView.setLayoutAnimation(animationController);

        if (postListMode){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (CURRENT_PAGE<=totalPages&&loadFlag){
                        fetchPosts();
                    }else {
                        mAdapter.endOfList();
                    }
                }
            });
        }else {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));
            mRecyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (CURRENT_PAGE<=totalPages &&loadFlag){
                        fetchPosts();
                    }else {
                        mAdapter.endOfList();
                    }
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scheduleLayoutAnimation();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PostListViewModel.class);
        fetchPosts();
        //Applying settings
        if (SEARCH_QUERY!=null)
            SLIDER_ENABLED = false;
    }



    private void fetchPosts(){
        loadFlag = false;
        mViewModel.getNewsRepository(CURRENT_PAGE,CATEGORIES,SEARCH_QUERY,TAGS,AUTHOR).observe(this, postResponse -> {
            if (postResponse==null) {
                //Toast.makeText(getContext(),"Error loading posts",Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                loadingLayout.setVisibility(GONE);
                mAdapter.endOfList();
            }else {
                if (MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().isNativeAdsEnabled()) {
                    loadAdmobNativeAds();
                }
                loadFlag = true;
                //fetchCategories();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (CURRENT_PAGE==1&&CATEGORIES!=null){
                    fetchCategories();
                    mAdapter.removeAllViews();
                }
                CURRENT_PAGE++;
                loadingLayout.setVisibility(GONE);
                totalPages = postResponse.getTotalPages();
                if (postResponse.getTotalPages()>0) {
                    masterList.addAll(postResponse.getPosts());
                    mAdapter.addItems(postResponse.getPosts());
                }else {
                    noContentLayout.setVisibility(View.VISIBLE);
                    mAdapter.endOfList();
                }
            }
        });

    }

    private void fetchCategories(){
        if (CATEGORIES!=null) {
            Integer parentCategory = Integer.parseInt(CATEGORIES.split(",")[0]);
            mViewModel.getSubCategories(1, parentCategory).observe(this, categoryResponse -> {
                List<Category> categories = categoryResponse.getCategories();
                Log.e("Category Size", "Size: " + categories.size());
                if (categories.size() > 0) {
                    categoryList.addAll(categories);
                    mAdapter.addSubCategories(categories, parentCategory);
                }
            });
        }
    }

    public void loadAdmobNativeAds(){
        AdLoader adLoader = new AdLoader.Builder(getContext(), getResources().getString(R.string.admob_native_ad_unit_id))
                .forUnifiedNativeAd(unifiedNativeAd -> {

                    insertAd(unifiedNativeAd, MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().getNativeAdsFrequency());
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Toast.makeText(getContext(),"Ad Failed",Toast.LENGTH_SHORT).show();
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAds(new AdRequest.Builder().build(),2);
    }

    private void insertAd(UnifiedNativeAd unifiedNativeAd,int frequency){
        mAdapter.insertAdItem(unifiedNativeAd,firstItemPosition);
        firstItemPosition = firstItemPosition + frequency;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CURRENT_PAGE = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("totalPages",totalPages);
    }

    @Override
    public void onClick(int position, String type) {
        switch (type) {
            case "post": {
                /*Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("postId", masterList.get(position).getId());
                intent.putExtra("img", masterList.get(position).getFeaturedImg());
                intent.putExtra("title", masterList.get(position).getTitle());
                startActivity(intent);*/
                Intent intent = new Intent(getContext(), PostContainerActivity.class);
                intent.putExtra("postId", masterList.get(position).getId());
                intent.putExtra("img", masterList.get(position).getFeaturedImg());
                intent.putExtra("title", masterList.get(position).getTitle());
                startActivity(intent);
                break;
            }
            case "posts": {
                /*Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra("title", categoryList.get(position).getName());
                intent.putExtra("screen", "posts");
                intent.putExtra("category", String.valueOf(categoryList.get(position).getId()));
                startActivity(intent);*/
                Toast.makeText(getContext(),"Posts Clicked",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onLongClick(int position, String type) {


    }
}