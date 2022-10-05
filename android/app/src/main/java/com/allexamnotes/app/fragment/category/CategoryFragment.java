package com.allexamnotes.app.fragment.category;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.allexamnotes.app.Config;
import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.MainActivity;
import com.allexamnotes.app.MainApplication;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.postlist.PostListFragment;
import com.allexamnotes.app.others.EndlessScrollListener;
import com.allexamnotes.app.others.ItemOffsetDecoration;
import com.allexamnotes.libdroid.database.CategoryDatabase;
import com.allexamnotes.libdroid.model.category.Category;
import com.allexamnotes.app.others.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryAdapter.onCategoryListener {

    private boolean LAYOUT_LIST_MODE = true; //true means list and false means grid

    private CategoryViewModel mViewModel;
    private int firstItemPosition = 5;
    //PAGE VARIABLES
    private int PAGE = 1;
    private String SEARCH;
    private Integer PARENT;

    //Flags
    private boolean loadingFlag;
    private int totalPages;
    private boolean hasLoadedOnce;

    View loadingView;
    View noContentsLayout;

    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CategoryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    public static CategoryFragment newInstance(String search,Integer parent) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search",search);
        bundle.putInt("parent",parent);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){

        }

        if (getArguments()!=null){
            PARENT = getArguments().getInt("parent");
            if (PARENT<0){
                PARENT=null;
            }
            SEARCH = getArguments().getString("search");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        noContentsLayout = view.findViewById(R.id.no_contents_error_layout);
        loadingView = view.findViewById(R.id.loadingView);
        mRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.categorySwipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            PAGE = 1;
            categoryList.clear();
            adapter.clearAdapter();
            fetchCategories();
        });
        if (Config.LOAD_CATEGORIES_WITH_BACKGROUND_IMGS)
            LAYOUT_LIST_MODE = false;
        adapter = new CategoryAdapter(getContext(),this, LAYOUT_LIST_MODE);
        if (LAYOUT_LIST_MODE){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (PAGE<totalPages && !loadingFlag){
                        PAGE++;
                        fetchCategories();
                    }else {
                        adapter.removeLoadMoreView();
                    }
                }
            });
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.category_item_offset);
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (PAGE<totalPages && !loadingFlag){
                        PAGE++;
                        fetchCategories();

                    }else {
                        adapter.removeLoadMoreView();
                    }
                }
            });
        }
        mRecyclerView.setAdapter(adapter);
        if (!hasLoadedOnce)
            mRecyclerView.scheduleLayoutAnimation();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        if (savedInstanceState == null) {
            fetchCategories();
        }
    }

    private void fetchCategories(){
        loadingFlag = true;
        mViewModel.getCategory(PAGE,SEARCH,PARENT,MainApplication.getAppSettings(getContext()).getHiddenCats()).observe(getViewLifecycleOwner(), categoryResponse -> {
            loadingFlag = false;
            hasLoadedOnce = true;
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            if (PAGE==1){
                categoryList.clear();
                adapter.clearAdapter();
            }
            if (MainApplication.getAppSettings(getActivity()).getSettings().getPostListSettings().isNativeAdsEnabled()&&LAYOUT_LIST_MODE) {
                loadAdmobNativeAds();
            }
            if (categoryResponse.getTotalPages()>0) {
                loadingView.setVisibility(View.GONE);
                totalPages = categoryResponse.getTotalPages();
                categoryList.addAll(categoryResponse.getCategories());
                adapter.addItems(categoryResponse.getCategories());
                if (categoryResponse.getCategories().size() < 10)
                    adapter.removeLoadMoreView();
                mRecyclerView.scheduleLayoutAnimation();
            }else {
                noContentsLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveToDatabase(List<Category> categories){
        CategoryDatabase categoryDatabase = CategoryDatabase.getAppDatabase(getContext());
        categoryDatabase.categoryDao().insertAll(categories);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage",PAGE);
    }


    @Override
    public void onCategoryClick(int position) {

        if (getActivity() instanceof ContainerActivity){
            try {
                ContainerActivity a = (ContainerActivity)getActivity();
                assert a != null;
                String title = categoryList.get(position).getName();
                a.setTitle(title);
                a.addFragment(PostListFragment.newInstance(String.valueOf(categoryList.get(position).getId()),null,null,null),title);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (getActivity() instanceof MainActivity){
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("title",categoryList.get(position).getName());
            intent.putExtra("screen","posts");
            intent.putExtra("category",String.valueOf(categoryList.get(position).getId()));
            startActivity(intent);
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

    private void insertAd(com.google.android.gms.ads.nativead.NativeAd nativeAd, int frequency){
        adapter.insertAdItem(nativeAd,firstItemPosition);
        if (firstItemPosition<categoryList.size()) {
            categoryList.add(firstItemPosition, new Category());
        }
        firstItemPosition = firstItemPosition + frequency;
    }
}
