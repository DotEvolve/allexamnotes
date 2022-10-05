package com.itsanubhav.wordroid4.fragment;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.itsanubhav.wordroid4.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;

public class ImageFragment extends Fragment {


    private String imgurl;
    private ImageView imageView;
    private Button wallpaperBtn;
    private Toolbar toolbar;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String image) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("img", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgurl = getArguments().getString("img");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.imageView);
        wallpaperBtn = view.findViewById(R.id.wallpaperBtn);
        /*if (!Config.USE_SET_AS_WALLPAPER_FEATURE){
            wallpaperBtn.setVisibility(View.GONE);
        }*/
        wallpaperBtn.setOnClickListener(view1 -> setAsWallpaper());
        Glide.with(getActivity()).load(imgurl).into(imageView);
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setAsWallpaper(){
        Toast.makeText(getActivity(),"Please wait...", Toast.LENGTH_SHORT).show();
        Glide.with(getActivity()).asBitmap().load(imgurl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            WallpaperManager.getInstance(getContext()).setBitmap(resource);
                            Toast.makeText(getActivity(),"Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
