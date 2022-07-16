package com.itsanubhav.wordroid4.fragment.comments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.listeners.OnCommentClickListener;
import com.itsanubhav.wordroid4.others.EndlessScrollListener;
import com.itsanubhav.libdroid.model.comment.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment implements OnCommentClickListener {

    private CommentsViewModel mViewModel;
    private int CURRENT_PAGE = 1;
    private Integer parent;
    private String searchQuery;
    private int postId;
    private int totalPages;
    private boolean loadFlag;

    private LinearLayout loadingView;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    public static CommentsFragment newInstance(int postId,Integer parent,String searchQuery) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search",searchQuery);
        bundle.putInt("postId",postId);
        bundle.putInt("parent",parent!=null?parent:0);
        commentsFragment.setArguments(bundle);
        return commentsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){
            Toast.makeText(getContext(),"Current Page "+savedInstanceState.getInt("currentPage"),Toast.LENGTH_SHORT).show();
        }

        if (getArguments()!=null){
            parent = getArguments().getInt("parent");
            postId = getArguments().getInt("postId");
            searchQuery = getArguments().getString("search");
        }else {
            getActivity().finish();
        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment, container, false);
        recyclerView = view.findViewById(R.id.commentRecyclerView);
        loadingView = view.findViewById(R.id.loadingView);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            CURRENT_PAGE = 1;
            getComments();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        adapter = new CommentAdapter(getContext(),this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (CURRENT_PAGE<=totalPages&&loadFlag){
                    CURRENT_PAGE++;
                    getComments();
                }else {
                    adapter.removeLoadingView();
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);
        getComments();
    }

    private void getComments(){
        loadFlag = false;
        mViewModel.getComments(CURRENT_PAGE,parent,postId,searchQuery).observe(this, commentResponse -> {
            if (commentResponse!=null){
                loadFlag = true;
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                loadingView.setVisibility(View.GONE);
                if (CURRENT_PAGE==1){
                    commentList.clear();
                    adapter.clearObjects();
                }
                if (commentResponse.getTotalPages()>0) {
                    if (commentResponse.getComments().size() > 0) {
                        totalPages = commentResponse.getTotalPages();
                        commentList.addAll(commentResponse.getComments());
                        adapter.addObjects(commentResponse.getComments());
                    } else {
                        adapter.removeLoadingView();
                    }
                }else{
                    //show no comments view
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage",CURRENT_PAGE);
    }

    public void showBottomSheet(int commentId,String title) {
        ReplyCommentDialogFragment addPhotoBottomDialogFragment =
                ReplyCommentDialogFragment.newInstance(postId,commentId,title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                ReplyCommentDialogFragment.TAG);
    }

    @Override
    public void onReplyBtnClicked(int position) {
        showBottomSheet(commentList.get(position).getId(),commentList.get(position).getAuthorName());
    }

    @Override
    public void onRepliesBtnClicked(int position) {
        try {
            ContainerActivity a = (ContainerActivity)getActivity();
            assert a != null;
            String title = commentList.get(position).getChildCommentCount() + " Replies to "+commentList.get(position).getAuthorName();
            a.addFragment(CommentsFragment.newInstance(postId,commentList.get(position).getId(),null),title);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}