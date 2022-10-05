package com.allexamnotes.app.fragment.media;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allexamnotes.app.R;
import com.allexamnotes.app.others.ItemOffsetDecoration;

public class MediaFragment extends Fragment {

    private MediaViewModel mViewModel;
    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;

    public static MediaFragment newInstance() {
        return new MediaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.media_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.mediaRecyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(10);
        recyclerView.addItemDecoration(itemOffsetDecoration);

        mediaAdapter = new MediaAdapter(getContext());

        recyclerView.setAdapter(mediaAdapter);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MediaViewModel.class);

    }

}
