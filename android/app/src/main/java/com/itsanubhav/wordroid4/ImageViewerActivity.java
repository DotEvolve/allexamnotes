package com.itsanubhav.wordroid4;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.itsanubhav.wordroid4.adapter.ImageViewerViewPagerAdapter;
import com.itsanubhav.wordroid4.fragment.ImageFragment;
import com.itsanubhav.wordroid4.others.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerActivity extends AppCompatActivity {


    CustomViewPager viewPager;
    private int currentPosition;
    private ImageViewerViewPagerAdapter adapter;
    private List<String> images = new ArrayList<>();
    private int length,position;
    private Toolbar toolbar;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        //Config.switchLayoutDirection(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        toolbar = findViewById(R.id.imageViewerToolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (getIntent()!=null){
            length = getIntent().getIntExtra("size",0);
            position = getIntent().getIntExtra("index",0);
            for (int i=0;i<length;i++){
                images.add(getIntent().getStringExtra("image_"+i));
            }
        }
        getSupportActionBar().setTitle((position+1)+"/"+length);
        viewPager = findViewById(R.id.imageViewPager);
        setViewPager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE){
            downloadImage(images.get(currentPosition));
        }
    }

    private static final int REQUEST_WRITE_STORAGE = 112;
    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            } else {
                downloadImage(images.get(currentPosition));
            }
        } else {
            downloadImage(images.get(currentPosition));
        }
    }

    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < adapter.getCount(); i++){
            adapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }

    private void setViewPager(){
        adapter = new ImageViewerViewPagerAdapter(getSupportFragmentManager());
        for (String s:images){
            adapter.addFragment(ImageFragment.newInstance(s));
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
                currentPosition = position;
                getSupportActionBar().setTitle((position+1)+"/"+length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(images.size());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position,false);
        invalidateFragmentMenus(viewPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if (Config.USE_DOWNLOAD_IMAGE_FEATURE) {
            MenuItem m = menu.findItem(R.id.option_download);
            m.setVisible(true);
            tintMenuIcon(ImageViewerActivity.this, m, R.color.md_white_1000);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_download:
                askPermission();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadImage(String url){
            String filename;
            if (url.contains("?")) {
                filename = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
            } else {
                filename = url.substring(url.lastIndexOf("/") + 1, url.length());
            }
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle(filename);
            request.setDescription(getResources().getString(R.string.app_name) + " is downloading " + filename);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            downloadManager.enqueue(request);
    }

    public void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

}
