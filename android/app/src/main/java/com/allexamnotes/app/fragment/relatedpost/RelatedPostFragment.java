package com.allexamnotes.app.fragment.relatedpost;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allexamnotes.app.Config;
import com.allexamnotes.app.DetailActivity;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.postlist.PostListViewModel;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.allexamnotes.libdroid.model.posts.Posts;

import java.util.ArrayList;
import java.util.List;

public class RelatedPostFragment extends Fragment implements OnItemClickListener {

    private PostListViewModel mViewModel;

    private RecyclerView recyclerView;
    private RelatedPostAdapter adapter;
    private View loadingLayout;
    private List<Posts> postsList = new ArrayList<>();

    private String categories;

    public static RelatedPostFragment newInstance(String categories) {
        Bundle bundle = new Bundle();
        bundle.putString("categories",categories);
        RelatedPostFragment relatedPostFragment = new RelatedPostFragment();
        relatedPostFragment.setArguments(bundle);
        return relatedPostFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null){
            categories = getArguments().getString("categories");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.related_post_fragment, container, false);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        recyclerView = rootView.findViewById(R.id.relatedPostRecyclerView);
        adapter = new RelatedPostAdapter(getContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PostListViewModel.class);
        mViewModel.getNewsRepository(1,categories,null,null,null, Config.NO_OF_RELATED_POSTS_TO_LOAD).observe(getViewLifecycleOwner(), postResponse -> {
            if (postResponse!=null){
                loadingLayout.setVisibility(View.GONE);
                postsList.addAll(postResponse.getPosts());
                adapter.addItems(postResponse.getPosts());
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("postId",postsList.get(position).getId());
        intent.putExtra("title",postsList.get(position).getTitle());
        intent.putExtra("img",postsList.get(position).getFeaturedImg());
        intent.putExtra("offline",false);
        startActivity(intent);
    }
}
