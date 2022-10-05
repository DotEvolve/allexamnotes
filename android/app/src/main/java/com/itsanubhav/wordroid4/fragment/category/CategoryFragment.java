package com.itsanubhav.wordroid4.fragment.category;

import androidx.lifecycle.ViewModelProviders;

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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.MainActivity;
import com.itsanubhav.wordroid4.MainApplication;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.fragment.postlist.PostListFragment;
import com.itsanubhav.wordroid4.others.EndlessScrollListener;
import com.itsanubhav.wordroid4.others.ItemOffsetDecoration;
import com.itsanubhav.libdroid.database.CategoryDatabase;
import com.itsanubhav.libdroid.model.category.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryAdapter.onCategoryListener {

    private static final boolean LAYOUT_LIST_MODE = true; //true means list and false means grid

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
            Toast.makeText(getContext(),"Current Page "+savedInstanceState.getInt("currentPage"),Toast.LENGTH_SHORT).show();
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
            fetchCategories();
        });

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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
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
        mViewModel.getCategory(PAGE,SEARCH,PARENT).observe(this, categoryResponse -> {
            loadingFlag = false;
            hasLoadedOnce = true;
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            if (PAGE==1){
                categoryList.clear();
                adapter.clearAdapter();
            }
            if (MainApplication.getAppSettings(getActivity()).getSettings().getPostListSettings().isNativeAdsEnabled()) {
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

    public void loadAdmobNativeAds(){
        AdLoader adLoader = new AdLoader.Builder(getContext(), getResources().getString(R.string.admob_native_ad_unit_id))
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    insertAd(unifiedNativeAd, MainApplication.getAppSettings(getActivity()).getSettings().getPostListSettings().getNativeAdsFrequency());
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {

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

    private void insertAd(UnifiedNativeAd unifiedNativeAd, int frequency){
        adapter.insertAdItem(unifiedNativeAd,firstItemPosition);
        if (firstItemPosition<categoryList.size()) {
            categoryList.add(firstItemPosition, new Category());
        }
        firstItemPosition = firstItemPosition + frequency;
    }
}
