package com.itsanubhav.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.itsanubhav.libdroid.model.comment.Comment;
import com.itsanubhav.libdroid.model.post.Post;
import com.itsanubhav.libdroid.model.response.CommentResponse;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {

    private static CommentRepository commentRepository;
    private ApiInterface apiInterface;
    public CommentCallback callback;

    public interface CommentCallback {

        void onPostLoaded(List<Post> posts, int pages);

        void onError(String msg);

    }

    public void setPostCallback(CommentCallback postCallback){
        this.callback = postCallback;
    }


    public static CommentRepository getInstance(){
        if (commentRepository ==null){
            commentRepository = new CommentRepository();
        }
        return commentRepository;
    }

    public CommentRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }


    public MutableLiveData<CommentResponse> getComments(int page, Integer parent, Integer post, String search) {
        final MutableLiveData<CommentResponse> posts = new MutableLiveData<>();
        Call<List<Comment>> listCall = apiInterface.getComments(page, parent, post, search);
        Log.e("Making Request", listCall.request().url().toString());
        listCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    try{
                        Log.d("Making Request",response.body().size()+" posts loaded");
                        int totalPostPages = Integer.parseInt(response.headers().get("x-wp-totalpages"));
                        posts.postValue(new CommentResponse(response.body(),totalPostPages));
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }
                    //posts.postValue(new PostResponse(response.body(),totalPostPages));
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                posts.postValue(null);
            }
        });
        return posts;
    }

    public MutableLiveData<Comment> postCommentAnonymously(int postId,int parent,String name,String email,String content){
        final MutableLiveData<Comment> comment = new MutableLiveData<>();
        Call<Comment> commentCall = apiInterface.postCommentUnAuthenticated(parent,postId,name,email,content);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    comment.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
        return comment;
    }

    public MutableLiveData<Comment> postComment(String token, int postId,int parent,String content){
        final MutableLiveData<Comment> comment = new MutableLiveData<>();
        Call<Comment> commentCall = apiInterface.postComment("Bearer " + token,parent,postId,content);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    comment.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
        return comment;
    }
}
