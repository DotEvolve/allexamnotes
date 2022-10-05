package com.itsanubhav.wordroid4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.itsanubhav.wordroid4.adapter.ViewPagerAdapter;

import com.itsanubhav.wordroid4.fragment.ViewPagerTabFragment;
import com.itsanubhav.wordroid4.fragment.category.CategoryFragment;
import com.itsanubhav.wordroid4.fragment.homepage.HomePageFragment;
import com.itsanubhav.wordroid4.fragment.notification.NotificationFragment;
import com.itsanubhav.wordroid4.fragment.postlist.PostListFragment;
import com.itsanubhav.wordroid4.fragment.saved.SavedPostFragment;
import com.itsanubhav.wordroid4.fragment.tags.TagsFragment;
import com.itsanubhav.wordroid4.fragment.webview.WebViewFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.itsanubhav.libdroid.model.settings.BottomNavigation;
import com.itsanubhav.libdroid.model.settings.ItemsItem;
import com.itsanubhav.libdroid.model.settings.NavDrawer;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.onesignal.OneSignal;

import org.jsoup.Jsoup;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 119;
    public BottomNavigationView bottomNavigationView;

    public ViewPager mainPager;

    public NavigationView navigationView;

    public DrawerLayout navDrawer;

    public FloatingActionButton publish;

    private FirebaseAuth mAuth;

    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    ViewPagerAdapter adapter;
    AppBarLayout appBarLayout;
    AppUpdateManager appUpdateManager;



    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MainApplication.PREF_NIGHT_MODE,false);

        int nightModeFlags = getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                nightMode = true;
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                nightMode = false;
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainBottomNavigation);
        mainPager = findViewById(R.id.mainPager);
        navigationView = findViewById(R.id.navigationView);
        navDrawer = findViewById(R.id.navDrawer);
        publish = findViewById(R.id.publish_post);

        mAuth = FirebaseAuth.getInstance();

        appBarLayout = findViewById(R.id.appBarMain);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if(nightMode){
            window.setStatusBarColor(Color.parseColor("#333333"));
        }else {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        publish.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),PublishPostActivity.class));
        });

        OneSignal.getTags(tags -> {

        });

        showUpdateDialog();
        setupToolbar();
        inflateNavigationDrawerItems();
        inflateBottomNavigation();
        setupFragments();
        checkForUpdate();

        //loadBannerAd();
        if (getIntent()!=null) {
            String msgTitle = getIntent().getStringExtra("msgTitle");
            String msgBody = getIntent().getStringExtra("msgBody");
            if (msgTitle!=null&&msgBody!=null){
                createDialog(msgTitle,msgBody);
            }
        }

    }

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        TextView textView = findViewById(R.id.mainToolbarTitle);
        textView.setText(MainApplication.getAppSettings(getApplicationContext()).getSettings().getAppName());
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.navDrawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Active
                break;
        }
    }

    private Fragment getDestinationFragment(int destination, String data){
        switch (destination){
            case 1:
                return HomePageFragment.newInstance();
            case 2:
                return ViewPagerTabFragment.newInstance("posts",data);
            case 3:
                return CategoryFragment.newInstance();
            case 4:
                return TagsFragment.newInstance(null);
            case 5:
                return SavedPostFragment.newInstance();
            case 6:
                return ViewPagerTabFragment.newInstance("youtube",data);
            case 7:
                return NotificationFragment.newInstance();
            case 8:
                return WebViewFragment.newInstance(MainApplication.getAppSettings(getApplicationContext()).getSettings().getAboutUrl());
            case 9:
                return WebViewFragment.newInstance(MainApplication.getAppSettings(getApplicationContext()).getSettings().getPrivacyUrl());
            case 10:
                return WebViewFragment.newInstance(data);
            case 11:
                return PostListFragment.newInstance(data,null,null,null);
            default:
                return null;
        }
    }

    private void setupFragments(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());



        int size = MainApplication.getAppSettings(getApplicationContext()).getSettings().getBottomNavigation().getItems().size();

        for(int i=0;i<size;i++){
            ItemsItem tempItem = MainApplication.getAppSettings(getApplicationContext()).getSettings().getBottomNavigation().getItems().get(i);
            adapter.addFragment(getDestinationFragment(tempItem.getDestination(),tempItem.getData()));
        }

        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.option_search).setVisible(true);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(),ContainerActivity.class);
        SearchView searchView = (SearchView) menu.findItem(R.id.option_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    private void inflateNavigationDrawerItems(){
        View navHeader = navigationView.getHeaderView(0);
        MaterialButton signInBtn = navHeader.findViewById(R.id.loginBtn);
        if (sharedPreferences.getString("token",null)!=null||mAuth.getCurrentUser()!=null)
            signInBtn.setText(getResources().getString(R.string.logout_btn));
        signInBtn.setOnClickListener(view -> {
            if (mAuth.getCurrentUser()==null) {
                if (sharedPreferences.getString("token", null) == null) {
                    Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                    intent.putExtra("screen", "login");
                    startActivity(intent);
                } else {
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.remove("token");
                    sharedPreferencesEditor.remove("name");
                    sharedPreferencesEditor.remove("email");
                    sharedPreferencesEditor.apply();
                    recreate();
                }
            }else{
                mAuth.signOut();
            }
        });

        //Name and Email in nav header
        TextView nameHeader = navHeader.findViewById(R.id.userName);
        TextView emailHeader = navHeader.findViewById(R.id.userEmail);
        if (sharedPreferences.getString("token",null)!=null){
            nameHeader.setText(sharedPreferences.getString("name",null));
            emailHeader.setText(sharedPreferences.getString("email",null));
        }

        //Register Night mode switcher
        Switch nightSwitch = navHeader.findViewById(R.id.nightSwitch);
        nightSwitch.setChecked(nightMode);
        nightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            sharedPreferencesEditor = sharedPreferences.edit();
            if (b){
                sharedPreferencesEditor.putBoolean(MainApplication.PREF_NIGHT_MODE,true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                navDrawer.openDrawer(Gravity.LEFT);
            }else {
                sharedPreferencesEditor.putBoolean(MainApplication.PREF_NIGHT_MODE,false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                navDrawer.openDrawer(Gravity.LEFT);
            }
            sharedPreferencesEditor.apply();
        });


        try {
            NavDrawer drawerSettings = MainApplication.getAppSettings(getApplicationContext()).getSettings().getNavDrawer();
            //header color
            navigationView.getHeaderView(0).findViewById(R.id.nav_header_relative).setBackgroundColor(Color.parseColor(drawerSettings.getHeaderBackgroundColor()));
            //header visibility
            if (!drawerSettings.getHeaderVisibility()){
                navigationView.removeHeaderView(navigationView.getHeaderView(0));
            }
        }catch (Exception e){

        }


        /* The unique identifier for menu drawer starts from 10 to 40
         *  This means menu drawer can hold up to 30 items max
         *  Do not alter the menu identifier as it is set manually and it might clash with other
         */

        try {
            List<ItemsItem> drawerItems = MainApplication.getAppSettings(getApplicationContext()).getSettings().getNavDrawer().getItems();
            navigationView.setItemIconTintList(null);
            Menu menu = navigationView.getMenu();
            for (int i=0;i<drawerItems.size();i++){
                menu.add(Menu.NONE, i + 10, Menu.NONE, drawerItems.get(i).getTitle())
                        .setIcon(new IconicsDrawable(this, "faw-"+drawerItems.get(i).getIcon())
                                .color(IconicsColor.parse(drawerItems.get(i).getIconColor())).actionBar());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Navigation Drawer Exception",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }




        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            int pos = id-10;
            ItemsItem menuItem = MainApplication.getAppSettings(getApplicationContext()).getSettings().getNavDrawer().getItems().get(pos);
            if (menuItem.getDestination()==12){
                openFacebook(menuItem.getData());
            }else if (menuItem.getDestination()==13){
                openTwitter(menuItem.getData());
            }else if (menuItem.getDestination()==14){
                openInstagram(menuItem.getData());
            }else {
                startActivity(getDestinationIntent(menuItem.getTitle(), menuItem.getDestination(), menuItem.getData()));
            }
            return false;
        });
    }


    private Intent getDestinationIntent(String title,int destination,String data){
        Intent intent = new Intent(getApplicationContext(),ContainerActivity.class);
        intent.putExtra("title",title);
        if (destination==1){

        } else if(destination==2){
            intent.putExtra("screen","tabbed");
            intent.putExtra("data",data);
        } else if(destination==3){
            intent.putExtra("screen","categories");
        }else if(destination==4){
            intent.putExtra("screen","tags");
        }else if(destination==5){
            intent.putExtra("screen","savedPosts");
        }else if(destination==6){
            intent.putExtra("screen","youtube");
            intent.putExtra("data",data);
        }else if(destination==7){
            intent.putExtra("screen","notifications");
        }else if(destination==8){
            intent.putExtra("screen","about");
        }else if(destination==9){
            intent.putExtra("screen","privacy");
        }else if (destination==10){
            intent.putExtra("screen","webview");
            intent.putExtra("url",data);
        }else if (destination==11){
            intent.putExtra("screen","posts");
            intent.putExtra("category",data);
        }else if (destination==15){
            intent.putExtra("screen","settings");
        }
        return intent;
    }

    private void openTwitter(String data){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name="+data));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/"+data)));
        }
    }

    public void openFacebook(String facebookUrl) {

        try {
            Uri uri = null;

            int versionCode = getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0)
                    .versionCode;

            if (versionCode >= 3002850) {
                facebookUrl = facebookUrl.toLowerCase().replace("www.", "m.");
                if (!facebookUrl.startsWith("https")) {
                    facebookUrl = "https://" + facebookUrl;
                }
                uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
            } else {
                String pageID = facebookUrl.substring(facebookUrl.lastIndexOf("/"));

                uri = Uri.parse("fb://page" + pageID);
            }
            Log.d(TAG, "startFacebook: uri = " + uri.toString());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Throwable e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    private void openInstagram(String data){
        Uri uri = Uri.parse("http://instagram.com/_u/"+data);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/"+data)));
        }
    }

    private void inflateBottomNavigation() {
        try {
            BottomNavigation menuSettings = MainApplication.getAppSettings(getApplicationContext()).getSettings().getBottomNavigation();
            if (menuSettings.isVisible()) {
                List<ItemsItem> menuItems = menuSettings.getItems();

                Menu menu = bottomNavigationView.getMenu();

                for (int i = 0; i < menuItems.size(); i++) {
                    menu.add(Menu.NONE, i + 1, Menu.NONE, menuItems.get(i).getTitle())
                            .setIcon(new IconicsDrawable(this, "faw-"+menuItems.get(i).getIcon()).actionBar());
                }

                if (menuSettings.getShowLabels().equals("never")) {
                    bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
                } else if (menuSettings.getShowLabels().equals("always")) {
                    bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                } else {
                    bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
                }

                bottomNavigationView.setElevation(25);
                bottomNavigationView.setBackgroundColor(Color.parseColor(menuSettings.getBackgroundColor()));

                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mainPager.setCurrentItem(item.getItemId()-1,true);
                        return true;
                    }
                });

                //bottom nav background color
                if (nightMode){
                    ColorDrawable colorDrawable2 = new ColorDrawable();
                    colorDrawable2.setColor(Color.parseColor("#333333"));
                    bottomNavigationView.setItemBackground(colorDrawable2);
                    bottomNavigationView.setBackgroundColor(Color.parseColor("#333333"));
                    ColorStateList colorStateList = new ColorStateList(new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                            new int[]{
                                    Color.parseColor("#EEEEEE"),
                                    Color.parseColor("#757575")
                            });
                    bottomNavigationView.setItemTextColor(colorStateList);
                    bottomNavigationView.setItemIconTintList(colorStateList);
                }else {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(Color.parseColor(menuSettings.getBackgroundColor()));
                    bottomNavigationView.setItemBackground(colorDrawable);
                    ColorStateList colorStateList = new ColorStateList(new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                            new int[]{
                                    Color.parseColor(menuSettings.getCheckedItemColor()),
                                    Color.parseColor(menuSettings.getUncheckedItemColor())
                            });
                    bottomNavigationView.setItemTextColor(colorStateList);
                    bottomNavigationView.setItemIconTintList(colorStateList);
                }




                //scroll physics
                mainPager.setOffscreenPageLimit(menuItems.size());
                mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        bottomNavigationView.setSelectedItemId(position+1);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("MainActivity","Error: "+e.getLocalizedMessage());
            e.printStackTrace();
        }


    }

    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < adapter.getCount(); i++){
            adapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }

    public void hideToolbar(){
        appBarLayout.setVisibility(View.GONE);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.double_tap_exit_text), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    private void showUpdateDialog(){
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            float version = Float.parseFloat(pInfo.versionName);
            if (version<MainApplication.getAppSettings(getApplicationContext()).getUpdate().getVersion()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                        .setCancelable(MainApplication.getAppSettings(getApplicationContext()).getUpdate().isForceUpdate())
                        .setTitle(MainApplication.getAppSettings(getApplicationContext()).getUpdate().getTitle())
                        .setMessage(MainApplication.getAppSettings(getApplicationContext()).getUpdate().getMsg())
                        .setPositiveButton("Update", (dialogInterface, i) -> {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        });
                if (MainApplication.getAppSettings(getApplicationContext()).getUpdate().isForceUpdate()) {
                    builder.setNegativeButton("Later", (dialogInterface, i) -> {
                        hideDialog();
                    });
                }
                alertDialog = builder.create();
                alertDialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private AlertDialog alertDialog;
    private void createDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(Jsoup.parse(title).text())
                .setMessage(Jsoup.parse(msg).text())
                .setPositiveButton("Ok", (dialogInterface, i) -> {

                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideDialog(){
        if (alertDialog!=null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
    }

    private void checkForUpdate(){
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Exception : "+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show());
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                Toast.makeText(getApplicationContext(),"Update Available",Toast.LENGTH_SHORT).show();
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, REQUEST_CODE_FLEXI_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE){
                //Toast.makeText(getApplicationContext(),"Update Not Available",Toast.LENGTH_SHORT).show();
            }else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UNKNOWN){
                //Toast.makeText(getApplicationContext(),"Unknown",Toast.LENGTH_SHORT).show();
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED){
                if (appUpdateManager != null){
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        }
    };

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.mainWindowView),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (appUpdateManager != null){
                appUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    private static final int REQUEST_CODE_FLEXI_UPDATE = 17362;
    private void requestUpdate(AppUpdateInfo info){
        try {
            appUpdateManager.startUpdateFlowForResult(info,AppUpdateType.FLEXIBLE,this,REQUEST_CODE_FLEXI_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FLEXI_UPDATE) {
            switch (resultCode){
                case Activity.RESULT_OK:
                    Toast.makeText(getApplicationContext(),"Update downloaded",Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                    break;
            }
        }

    }

    private void loadBannerAd(){
            //There is no adview on homepage. Uncommenting this code may crash the app
            /*AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                }
            });*/
    }

}
