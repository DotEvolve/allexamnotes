package com.itsanubhav.wordroid4.fragment.saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.itsanubhav.wordroid4.PostContainerActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.listeners.OnHomePageItemClickListener;
import com.itsanubhav.libdroid.database.PostDatabase;
import com.itsanubhav.libdroid.database.dao.PostDao;
import com.itsanubhav.libdroid.model.post.Post;

import java.util.ArrayList;
import java.util.List;

public class SavedPostFragment extends Fragment implements OnHomePageItemClickListener {

    private SavedPostViewModel mViewModel;
    private RecyclerView recyclerView;
    private SavedPostAdapter adapter;
    private List<Post> masterList = new ArrayList<>();
    private ExtendedFloatingActionButton deleteAllFab;

    private View no_content;

    public static SavedPostFragment newInstance() {
        return new SavedPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_post_fragment, container, false);
        deleteAllFab = view.findViewById(R.id.deletefab);
        recyclerView = view.findViewById(R.id.recyclerView);
        no_content = view.findViewById(R.id.no_contents);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SavedPostAdapter(getContext(),this);
        recyclerView.setAdapter(adapter);
        deleteAllFab.setOnClickListener(view1 -> new DeleteAllRecords().execute());
        return view;
    }

    LiveData<List<Post>> posts;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SavedPostViewModel.class);

        PostDatabase postDatabase = PostDatabase.getAppDatabase(getContext());
        PostDao postDao = postDatabase.postsDao();
        posts = postDao.getAllPosts();

        posts.observe(this, posts -> {
            masterList.clear();
            masterList.addAll(posts);
            adapter.addPosts(posts);
            no_content.setVisibility(View.GONE);
            if (masterList.size()==0){
                deleteAllFab.setVisibility(View.GONE);
                no_content.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(int position, String type) {
        Intent intent = new Intent(getContext(), PostContainerActivity.class);
        intent.putExtra("postId", masterList.get(position).getId());
        intent.putExtra("img", masterList.get(position).getFeaturedImgUrl());
        intent.putExtra("title", masterList.get(position).getTitle().getRendered());
        intent.putExtra("offline",true);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position, String type) {

    }


    class DeleteAllRecords extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getContext());
            postDatabase.postsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            no_content.setVisibility(View.VISIBLE);
        }
    }

}


