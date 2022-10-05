package com.allexamnotes.app.fragment.postlist;

import android.annotation.SuppressLint;
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

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.LoadAdError;
import com.allexamnotes.app.Config;
import com.allexamnotes.app.MainApplication;
import com.allexamnotes.app.PostContainerActivity;
import com.allexamnotes.app.listeners.OnHomePageItemClickListener;
import com.google.android.material.appbar.AppBarLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allexamnotes.app.R;
import com.allexamnotes.app.adapter.PostListAdapter;
import com.allexamnotes.app.others.EndlessScrollListener;
import com.allexamnotes.app.others.SpaceItemDecoration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.allexamnotes.libdroid.model.category.Category;
import com.allexamnotes.libdroid.model.posts.Posts;
import com.allexamnotes.app.others.Utils;

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
        firstItemPosition = MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().getNativeAdsFrequency();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            CURRENT_PAGE = 1;
            firstItemPosition = MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().getNativeAdsFrequency();
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
        mViewModel.getNewsRepository(CURRENT_PAGE,CATEGORIES,SEARCH_QUERY,TAGS,AUTHOR).observe(getViewLifecycleOwner(), postResponse -> {
            if (postResponse==null) {
                //Toast.makeText(getContext(),"Error loading posts",Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                loadingLayout.setVisibility(GONE);
                mAdapter.endOfList();
            }else {
                if (MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().isNativeAdsEnabled()&& Config.ADMOB_ADS) {
                    loadAdmobNativeAds();
                } else if (MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().isNativeAdsEnabled()&& Config.FB_ADS) {
                    loadFBNativeAd();
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
                if (postResponse.getTotalPages()>0) {
//                    if (CURRENT_PAGE==1){
//                        mAdapter.addSliderItems(postResponse.getPosts().subList(0,SLIDER_SIZE));
//                        masterList.addAll(postResponse.getPosts().subList(SLIDER_SIZE-1,postResponse.getPosts().size()));
//                        mAdapter.addItems(postResponse.getPosts().subList(SLIDER_SIZE-1,postResponse.getPosts().size()));
//                    }else {
//                        masterList.addAll(postResponse.getPosts());
//                        mAdapter.addItems(postResponse.getPosts());
//                    }
                    masterList.addAll(postResponse.getPosts());
                    mAdapter.addItems(postResponse.getPosts());
                }else {
                    //noContentLayout.setVisibility(View.VISIBLE);
                    mAdapter.endOfList();
                }
                CURRENT_PAGE++;
                loadingLayout.setVisibility(GONE);
                totalPages = postResponse.getTotalPages();
            }
        });

    }

    private void fetchCategories(){
        if (CATEGORIES!=null) {
            Integer parentCategory;
            try{
                parentCategory = Integer.parseInt(CATEGORIES.split(",")[0]);
                mViewModel.getSubCategories(1, parentCategory).observe(getViewLifecycleOwner(), categoryResponse -> {
                    List<Category> categories = categoryResponse.getCategories();
                    Log.e("Category Size", "Size: " + categories.size());
                    if (categories.size() > 0) {
                        categoryList.addAll(categories);
                        mAdapter.addSubCategories(categories, parentCategory);
                    }
                });
            }catch (Exception e){
                Log.e("PostListFragment","Error loading Categories");
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void loadAdmobNativeAds(){
        new AdLoader.Builder(getContext(), getResources().getString(R.string.admob_native_ad_unit_id))
                .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                        insertAd(nativeAd,MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().getNativeAdsFrequency());
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Utils.showDebugToast("Ad failed to load "+adError.getMessage(),getContext());
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build().loadAds(new AdRequest.Builder().build(),2);
    }

    private NativeAd nativeAd;

    private void loadFBNativeAd() {
        nativeAd = new NativeAd(getActivity(), getResources().getString(R.string.fb_native_ad_placement_id));
        String TAG = "PostList";
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                insertFbAds(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private void insertAd(com.google.android.gms.ads.nativead.NativeAd nativeAd, int frequency){
        mAdapter.insertAdItem(nativeAd,firstItemPosition);
        firstItemPosition = firstItemPosition + frequency;
    }

    private void insertFbAds(NativeAd ad){
        int frequency = MainApplication.getAppSettings(getContext()).getSettings().getPostListSettings().getNativeAdsFrequency();
        mAdapter.insertFBAdItem(ad,firstItemPosition);
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