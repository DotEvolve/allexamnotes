package com.allexamnotes.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.allexamnotes.app.fragment.ViewPagerTabFragment;
import com.allexamnotes.app.fragment.auth.AuthFragment;
import com.allexamnotes.app.fragment.category.CategoryFragment;
import com.allexamnotes.app.fragment.comments.CommentsFragment;
import com.allexamnotes.app.fragment.notification.NotificationFragment;
import com.allexamnotes.app.fragment.postlist.PostListFragment;
import com.allexamnotes.app.fragment.saved.SavedPostFragment;
import com.allexamnotes.app.fragment.settings.SettingsFragment;
import com.allexamnotes.app.fragment.tags.TagsFragment;
import com.allexamnotes.app.fragment.webview.WebViewFragment;
import com.allexamnotes.app.fragment.youtube.PlaylistFragment;
import com.allexamnotes.app.fragment.youtube.VideoFragment;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class ContainerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private String screen, category, title;

    //For posts
    private String tags,author,searchQuery;

    //videos
    private String playlistId;

    //webview
    private String url;

    private String data;

    private int postId,parent;

    private Fragment destinationFragment;
    private TextView toolbatTitle;

    private List<String> titles = new ArrayList<>();
    private LinearLayout fbBannerSticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        titles.clear();
        if (getIntent()!=null){
            //Universal
            screen = getIntent().getStringExtra("screen");
            title = getIntent().getStringExtra("title");
            //Post List
            category = getIntent().getStringExtra("category");
            author = getIntent().getStringExtra("author");
            tags = getIntent().getStringExtra("tags");
            searchQuery = getIntent().getStringExtra("search");
            //Comments
            postId = getIntent().getIntExtra("postId",0);
            parent = getIntent().getIntExtra("parent",0);

            //video
            playlistId = getIntent().getStringExtra("playlist");

            //webview
            url = getIntent().getStringExtra("url");

            //data
            data = getIntent().getStringExtra("data");

        }
        if (screen==null){
            screen = "posts";
        }
        fbBannerSticky = findViewById(R.id.fbBanner);
        appBarLayout = findViewById(R.id.container_app_bar_layout);
        toolbar = findViewById(R.id.containerToolbar);
        toolbatTitle = findViewById(R.id.containerToolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        if (screen!=null){
            if (screen.equals("categories")){
                destinationFragment = CategoryFragment.newInstance();
            }else if (screen.equals("posts")){
                destinationFragment = PostListFragment.newInstance(category,author,tags,searchQuery);
            }else if (screen.equals("comments")){
                destinationFragment = CommentsFragment.newInstance(postId,parent,searchQuery);
            }else if(screen.equals("tags")){
                destinationFragment = TagsFragment.newInstance(null);
            }else if (screen.equals("playlist")){
                destinationFragment = PlaylistFragment.newInstance();
            }else if (screen.equals("videos")){
                destinationFragment = VideoFragment.newInstance(playlistId);
            }else if (screen.equals("login")){
                hideToolbar();
                //destinationFragment = CategoryFragment.newInstance();
                destinationFragment = AuthFragment.newInstance();
            }else if (screen.equals("webview")){
                destinationFragment = WebViewFragment.newInstance(url);
            }else if (screen.equals("notifications")){
                destinationFragment = NotificationFragment.newInstance();
            }else if (screen.equals("youtube")){
                destinationFragment = ViewPagerTabFragment.newInstance("youtube",data);
            }else if (screen.equals("savedPosts")){
                destinationFragment = SavedPostFragment.newInstance();
            }else if (screen.equals("about")){
                destinationFragment = WebViewFragment.newInstance(MainApplication.getAppSettings(getApplicationContext()).getSettings().getAboutUrl());
            }else if (screen.equals("privacy")){
                destinationFragment = WebViewFragment.newInstance(MainApplication.getAppSettings(getApplicationContext()).getSettings().getPrivacyUrl());
            }else if (screen.equals("settings")){
                destinationFragment = SettingsFragment.newInstance();
            }else if (screen.equals("tabbed")){
                destinationFragment = ViewPagerTabFragment.newInstance("posts",data);
            }
            addFragment(destinationFragment);
        }
        loadBannerAd();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("screen",screen);
        outState.putString("category", category);
        outState.putString("author",author);
        outState.putString("tags",tags);
        outState.putString("search",searchQuery);
        outState.putString("playlist",playlistId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (screen.equals("categories")){
                destinationFragment = CategoryFragment.newInstance(query,-1);
            }else if (screen.equals("posts")){
                destinationFragment = PostListFragment.newInstance(category,author,tags,query);
            }else if (screen.equals("comments")){
                destinationFragment = CommentsFragment.newInstance(postId,parent,query);
            }else if(screen.equals("tags")){
                destinationFragment = TagsFragment.newInstance(query);
            }else if (screen.equals("playlist")){
                destinationFragment = PlaylistFragment.newInstance();
            }else if (screen.equals("videos")){
                destinationFragment = VideoFragment.newInstance(playlistId);
            }
            addFragment(destinationFragment,query);
        }else {

            if (intent!=null){
                //Universal
                screen = intent.getStringExtra("screen");
                title = intent.getStringExtra("title");
                //Post List
                category = intent.getStringExtra("category");
                author = intent.getStringExtra("author");
                tags = intent.getStringExtra("tags");
                searchQuery = intent.getStringExtra("search");
                //Comments
                postId = intent.getIntExtra("postId",0);
                parent = intent.getIntExtra("parent",0);

                //video
                playlistId = intent.getStringExtra("playlist");

                //webview
                url = intent.getStringExtra("url");

                //data
                data = intent.getStringExtra("data");
            }

            if (screen.equals("categories")){
                destinationFragment = CategoryFragment.newInstance(searchQuery,null);
            }else if (screen.equals("posts")){
                destinationFragment = PostListFragment.newInstance(category,author,tags,searchQuery);
            }else if (screen.equals("comments")){
                destinationFragment = CommentsFragment.newInstance(postId,parent,searchQuery);
            }else if(screen.equals("tags")){
                destinationFragment = TagsFragment.newInstance(searchQuery);
            }else if (screen.equals("playlist")){
                destinationFragment = PlaylistFragment.newInstance();
            }else if (screen.equals("videos")){
                destinationFragment = VideoFragment.newInstance(playlistId);
            }
            addFragment(destinationFragment,title);
        }
    }

    public void addFragment(@NonNull Fragment fragment) {
        setTitle(title);
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .replace(R.id.containerFragment, fragment, screen)
                .addToBackStack("stack"+(entryCount+1))
                .commit();
    }

    public void addFragment(@NonNull Fragment fragment,String title) {
        if(title ==null)
            title = "";
        setTitle(title);
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                //.replace(R.id.containerFragment, fragment, screen)
                .add(R.id.containerFragment, fragment, screen)
                .addToBackStack("stack"+(entryCount+1))
                .commit();
    }

    private void hideSearchIcon(){

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.option_add_comment:
                if (destinationFragment instanceof CommentsFragment){
                    ((CommentsFragment) destinationFragment).showBottomSheet(0,null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        switch (screen){
            case "categories":
            case "posts":
            case "tags":
                menu.findItem(R.id.option_search).setVisible(true);
                break;
            case "comments":
                menu.findItem(R.id.option_add_comment).setVisible(true);
                break;
            case "playlist":
                break;
            case "videos":
                break;
            default:
                break;
        }
        if (screen.equals("categories")||screen.equals("comments")||screen.equals("posts")||screen.equals("tags")||
                screen.equals("playlist")||screen.equals("videos")) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            ComponentName componentName = new ComponentName(getApplicationContext(), ContainerActivity.class);
            SearchView searchView = (SearchView) menu.findItem(R.id.option_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        }
        return true;
    }

    public void setTitle(String title){
        toolbatTitle.setText(title);
        titles.add(title);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (destinationFragment instanceof WebViewFragment){
            destinationFragment.onActivityResult(requestCode, resultCode, data);
        }

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("test");
        if (fragment != null) {
            ((WebViewFragment) fragment).onActivityResult(requestCode, resultCode, data);
        }*/
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(screen);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1){
            if (fragment instanceof WebViewFragment){
                if (!((WebViewFragment) fragment).backPressed()) {
                    finish();
                }
            }else {
                finish();
            }
        }else if (fragment instanceof CommentsFragment) {
            popBack(count);
        } else {
            popBack(count);
        }

    }

    private void popBack(int count){
        getSupportFragmentManager().popBackStackImmediate();
        setTitle(titles.get(count-2));
    }

    private void loadBannerAd(){
        if (!screen.equals("login")) {
            if(!screen.equals("webview")) {
                if (MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostListSettings().isBannerAdsEnabled()) {
                    if(Config.ADMOB_ADS) {
                        AdView mAdView = findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        mAdView.setAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                            }

                            @Override
                            public void onAdLoaded() {
                                //Toast.makeText(getApplicationContext(),"Ad Loaded",Toast.LENGTH_LONG).show();
                                mAdView.setVisibility(View.VISIBLE);
                            }
                        });
                    }else if(Config.FB_ADS){
                        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(getApplicationContext(), getResources().getString(R.string.fb_banner_ad_placement_id), AdSize.BANNER_HEIGHT_50);
                        fbBannerSticky.addView(adView);
                        com.facebook.ads.AdListener listener = new com.facebook.ads.AdListener() {
                            @Override
                            public void onError(Ad ad, AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                fbBannerSticky.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        };
                        adView.loadAd(adView.buildLoadAdConfig().withAdListener(listener).build());
                    }
                }
            }
        }
    }


    public void hideToolbar(){
        appBarLayout.setVisibility(View.GONE);
    }
}
