package com.itsanubhav.wordroid4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.itsanubhav.wordroid4.fragment.post.PostViewModel;
import com.itsanubhav.wordroid4.fragment.relatedpost.RelatedPostFragment;
import com.itsanubhav.wordroid4.others.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.chip.ChipGroup;
import com.itsanubhav.libdroid.database.PostDatabase;
import com.itsanubhav.libdroid.database.dao.PostDao;
import com.itsanubhav.libdroid.model.post.Post;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;

public class DetailActivity extends AppCompatActivity {

    int postId;
    String title;
    String imageUrl;
    boolean offline;

    private PostViewModel mViewModel;
    private Post currentPost;
    private InterstitialAd mInterstitialAd;
    private ImageView featureImageView;
    private TextView titleTextView;
    private AdvancedWebView contentWebView;
    private IconicsTextView metaContents;
    private IconicsButton commentBtn;
    private ChipGroup chipGroup;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sharedPreferences  = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int postCount = sharedPreferences.getInt("postcount",0);
        editor.putInt("postcount",postCount+1);
        editor.apply();

        if (getIntent()!=null){
            postId = getIntent().getIntExtra("postId",0);
            title = getIntent().getStringExtra("title");
            imageUrl = getIntent().getStringExtra("img");
            offline = getIntent().getBooleanExtra("offline",false);
        }

        toolbar = findViewById(R.id.toolbar);
        metaContents = findViewById(R.id.metaTextView);
        featureImageView = findViewById(R.id.featuredImage);
        titleTextView = findViewById(R.id.titleTextView);
        contentWebView = findViewById(R.id.contentWebView);
        chipGroup = findViewById(R.id.postDetailChipGroup);
        commentBtn = findViewById(R.id.commentsBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());

        //setting initial category

        if (offline){
            loadOfflinePost(postId);
        }else {
            loadPost();
        }
    }

    private void inflateAds(){
        if (MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isBannerAdsEnabled()) {
            AdView aboveContent = findViewById(R.id.adViewAboveContent);
            AdView belowContent = findViewById(R.id.adViewBelowContent);
            AdRequest adRequest = new AdRequest.Builder().build();
            aboveContent.loadAd(adRequest);
            AdRequest adRequest2 = new AdRequest.Builder().build();
            belowContent.loadAd(adRequest2);
        }

        if(MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isInterstitialAdsEnabled()){
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_banner_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    finish();
                }
            });
        }
    }

    private boolean showInterstitiaAd(){
        int postCount = sharedPreferences.getInt("postRead",0);
        if (postCount>=MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getInterstitialAdFrequency()){
            if (mInterstitialAd.isLoaded()){
                editor.putInt("postcount",0);
                mInterstitialAd.show();
            }
            return true;
        }else {
            return false;
        }

    }

    private void loadPost(){
        if (imageUrl!=null)
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .into(featureImageView);

        if (title!=null)
            titleTextView.setText(Jsoup.parse(title).text());

        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mViewModel.getPost(postId).observe(this, post -> {
            if (post!=null){
                setPostData(post);
                inflateAds();
            }
        });
    }

    private void setPostData(Post postData){
        currentPost = postData;
        if (postData.getFeaturedImgUrl()!=null) {
            Glide.with(getApplicationContext())
                    .load(postData.getFeaturedImgUrl())
                    .into(featureImageView);
        }
        titleTextView.setText(Jsoup.parse(postData.getTitle().getRendered()).text());

        //Build meta string
        String metaString = "{faw-calendar} "+ Utils.parseDate(postData.getModified()) +"   {faw-user} "+postData.getAuthorName();
        metaContents.setText(metaString);

        //Load Tags Chips

        //LoadRelatedPosts
        addRelatedPostsLayout();

        //comment btn
        commentBtn.setVisibility(View.VISIBLE);
        commentBtn.setText(String.format(getResources().getString(R.string.comments_text),String.valueOf(postData.getCommentCount())));
        commentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),ContainerActivity.class);
            intent.putExtra("title",postData.getCommentCount()+" Comments");
            intent.putExtra("screen","comments");
            intent.putExtra("postId",postData.getId());
            startActivity(intent);
        });
        //WebViewContent
        String css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\" ><script src=\"script.js\"></script>";
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\" ><script src=\"script.js\"></script><style>*{background-color:#000000;color:#fff!important;}</style>";
        }

        Document document = Jsoup.parse(css + postData.getContent().getRendered());
        contentWebView.loadDataWithBaseURL("file:///android_asset/",document.html(),"text/html","UTF-8",null);
        if (!MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isContentCopyEnabled()){
            contentWebView.setOnLongClickListener(view -> true);
            contentWebView.setLongClickable(false);
            contentWebView.setHapticFeedbackEnabled(false);
        }
        contentWebView.addPermittedHostname("file:///android_asset/");
        contentWebView.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(String url) {

            }

            @Override
            public void onPageError(int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

            }

            @Override
            public void onExternalPageRequest(String url) {
                String option = MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getExternalLinks();
                if (option.equals("app")){
                    Intent intent = new Intent(getApplicationContext(),ContainerActivity.class);
                    intent.putExtra("screen","webview");
                    intent.putExtra("url",url);
                    startActivity(intent);
                }else if (option.equals("chrome")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }else if (option.equals("chooser")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
    }

    private void loadOfflinePost(int postId){
        PostDatabase postDatabase = PostDatabase.getAppDatabase(getApplicationContext());
        PostDao postDao = postDatabase.postsDao();
        LiveData<List<Post>> posts = postDao.getPost(postId);
        posts.observe(this,posts1 -> {
            if (posts1.size()>0) {
                setPostData(posts1.get(0));
            }else {
                loadPost();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.option_save).setVisible(true);
        menu.findItem(R.id.option_share).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_save:
                if (currentPost!=null){
                    new SavePost().execute(currentPost);
                    Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.option_share:
                if (currentPost!=null){
                    new SharePost(getApplicationContext()).execute(currentPost.getFeaturedImgUrl());
                    Toast.makeText(getApplicationContext(),"Choose the app to share with",Toast.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    private void addRelatedPostsLayout(){
        RelatedPostFragment relatedPostFragment;
        relatedPostFragment = RelatedPostFragment.newInstance(getCategoryString());
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .replace(R.id.relatedPostFrame, relatedPostFragment)
                .addToBackStack(null)
                .commit();
    }

    private String getCategoryString(){
        if (currentPost.getCategories()!=null) {
            Integer[] arr = currentPost.getCategories().toArray(new Integer[currentPost.getCategories().size()]);
            String s = Arrays.toString(arr);
            s = s.substring(1, s.length() - 1);
            Log.e("Categories", "" + s);
            return s;
        }else {
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        if (!showInterstitiaAd()){
            finish();
        }else {
            finish();
        }
    }


    class SavePost extends AsyncTask<Post,Void,Void>{

        @Override
        protected Void doInBackground(Post... posts) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getApplicationContext());
            PostDao postDao = postDatabase.postsDao();
            postDao.insertPost(posts[0]);
            return null;
        }
    }

    class SharePost extends AsyncTask<String, Void, File> {
        private final Context context;
        public SharePost(Context context) {
            this.context = context;
        }
        @Override protected File doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                // Glide v4
                return Glide
                        .with(context)
                        .downloadOnly()
                        .load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get() // needs to be called on background thread
                        ;
            } catch (Exception ex) {
                Log.w("SHARE", "Sharing " + " failed", ex);
                return null;
            }
        }

        @Override protected void onPostExecute(File result) {
            if (result == null) { return; }
            Toast.makeText(context,"Choose app to share with",Toast.LENGTH_SHORT).show();
            Uri uri = FileProvider.getUriForFile(context,context.getPackageName()+".provider", result);
            share(uri); // startActivity probably needs UI thread
        }

        private void share(Uri result) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_SUBJECT, currentPost.getTitle().getRendered());
            intent.putExtra(Intent.EXTRA_TEXT, currentPost.getTitle().getRendered()+"\n"+currentPost.getLink());
            intent.putExtra(Intent.EXTRA_STREAM, result);
            startActivity(Intent.createChooser(intent, "Share post"));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null){
            postId = getIntent().getIntExtra("postId",0);
            title = getIntent().getStringExtra("title");
            imageUrl = getIntent().getStringExtra("img");
            offline = getIntent().getBooleanExtra("offline",false);
            loadPost();

        }

    }
}
