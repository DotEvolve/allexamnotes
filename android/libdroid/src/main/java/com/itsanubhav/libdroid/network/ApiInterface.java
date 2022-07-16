package com.itsanubhav.libdroid.network;



import com.itsanubhav.libdroid.model.WorDroidSection;
import com.itsanubhav.libdroid.model.auth.AuthResponse;
import com.itsanubhav.libdroid.model.media.Media;
import com.itsanubhav.libdroid.model.playlist.Playlist;
import com.itsanubhav.libdroid.model.playlistvideos.MPlaylistVideos;
import com.itsanubhav.libdroid.model.posts.Posts;
import com.itsanubhav.libdroid.model.category.Category;
import com.itsanubhav.libdroid.model.comment.Comment;
import com.itsanubhav.libdroid.model.post.Post;
import com.itsanubhav.libdroid.model.settings.AppSettings;
import com.itsanubhav.libdroid.model.tag.Tag;
import com.itsanubhav.libdroid.model.user.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("wp-json/wordroid/v4/settings")
    Call<AppSettings> getSettings(@Query("app_token") String token);

    @POST("wp-json/jwt-auth/v1/token")
    @FormUrlEncoded
    Call<AuthResponse> makeAuth(
                        @Field("username") String username,
                        @Field("password") String password
    );

    @POST("wp-json/jwt-auth/v1/token/validate")
    Call<AuthResponse> validateAuth();

    @GET("wp-json/wp/v2/posts")
    Call<List<Posts>> getNewsList(@Query("page") Integer page,
                                  @Query("search") String search,
                                  @Query("categories") String categories,
                                  @Query("tags") String tag,
                                  @Query("author") String author,
                                  @Query("app") String app);

    @GET("wp-json/wp/v2/posts/{id}")
    Call<Post> getPost(
            @Path("id") Integer id
    );

    @GET("wp-json/wp/v2/posts/")
    Call<List<Post>> getPost(
            @Query("slug") String slug
    );

    @GET("wp-json/wp/v2/media")
    Call<List<Media>> getMedia(
            @Query("page") Integer page,
            @Query("search") String searchQuey
    );

    @Multipart
    @POST("wp-json/wp/v2/media")
    Call<Media> uploadMedia(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    @GET("wp-json/wp/v2/categories")
    Call<List<Category>> getCategoryList(
            @Query("page") Integer page,
            @Query("search") String query,
            @Query("parent") Integer parent,
            @Query("hide_empty") boolean hideEmpty
    );

    @GET("wp-json/wp/v2/tags")
    Call<List<Tag>> getTagsList(
            @Query("page") Integer page,
            @Query("search") String query
    );

    @GET("wp-json/wp/v2/comments")
    Call<List<Comment>> getComments(
            @Query("page") Integer page,
            @Query("parent") Integer parent,
            @Query("post") Integer post,
            @Query("search") String query
    );

    @POST("wp-json/wp/v2/comments")
    @FormUrlEncoded
    Call<Comment> postComment(
            @Header("Authorization") String token,
            @Field("parent") int parent,
            @Field("post") int post,
            @Field("content") String content
    );

    @POST("wp-json/wp/v2/comments")
    @FormUrlEncoded
    Call<Comment> postCommentUnAuthenticated(
            @Field("parent") int parent,
            @Field("post") int post,
            @Field("author_name") String name,
            @Field("author_email") String email,
            @Field("content") String content
    );

    @GET("wp-json/wp/v2/users")
    Call<List<User>> getUsers(
            @Query("page") int page,
            @Query("search") String query
    );

    @GET("wp-json/wordroid/v4/homepage")
    Call<List<WorDroidSection>> getHomePageSections(@Query("app_token") String token);

    @GET
    Call<MPlaylistVideos> getPlaylistVideos(@Url String url,
                                            @Query("key") String DevloperKey,
                                            @Query("channelId") String channelId,
                                            @Query("pageToken") String pageToken,
                                            @Query("playlistId") String playlistId,
                                            @Query("part") String part,
                                            @Query("maxResults") int maxResults);

    @GET
    Call<Playlist> getPlaylists(@Url String url,
                                @Query("key") String DevloperKey,
                                @Query("channelId") String channelId,
                                @Query("pageToken") String pageToken,
                                @Query("part") String part,
                                @Query("maxResults") int maxResults

    );



    /*@GET
    Call<PlaylistSearch> searchPlaylists(@Url String url,
                                         @Query("key") String DevloperKey,
                                         @Query("q") String searchQuery,
                                         @Query("channelId") String channelId,
                                         @Query("pageToken") String pageToken,
                                         @Query("part") String part,
                                         @Query("type") String type,
                                         @Query("maxResults") int maxResults

    );*/


}
