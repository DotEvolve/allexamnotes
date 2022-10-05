package com.allexamnotes.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.allexamnotes.libdroid.model.post.CategoriesDetailItem;
import com.allexamnotes.app.fragment.post.PostViewModel;
import com.allexamnotes.app.fragment.relatedpost.RelatedPostFragment;
import com.allexamnotes.app.others.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.chip.ChipGroup;
import com.allexamnotes.libdroid.database.PostDatabase;
import com.allexamnotes.libdroid.database.dao.PostDao;
import com.allexamnotes.libdroid.model.post.Post;
import com.allexamnotes.app.others.WebAppInterface;
import com.mikepenz.iconics.view.IconicsButton;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private boolean isActive;
    private PostViewModel mViewModel;
    private int postId;
    private String slug;
    private boolean nightMode;
    private String title;
    private String imageUrl;
    private boolean offline;
    private MaterialButton shareBtn,saveBtn;
    private Post currentPost;
    private InterstitialAd mInterstitialAd;
    private ImageView featureImageView;
    private TextView titleTextView;
    private WebView contentWebView;
    private IconicsTextView metaContents;
    private IconicsButton commentBtn;
    private ChipGroup chipGroup;
    private Toolbar toolbar;
    private TextToSpeech tts;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AdView aboveContent;
    private AdView belowContent;
    private FloatingActionButton speakBtn;
    private TextView relatedPostTitle;
    private View root;
    private List<String> images = new ArrayList<>();
    private LinearLayout fbAboveContent,fbBelowContent;
    com.facebook.ads.AdView adView,adView2;
    com.facebook.ads.InterstitialAd fbInterstitialAd;
    private View loadingView;
    private boolean fromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
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
            slug = getIntent().getStringExtra("slug");
            offline = getIntent().getBooleanExtra("offline",false);
            fromNotification = getIntent().getBooleanExtra("fromNotification",false);
        }

        tts = new TextToSpeech(getApplicationContext(), this);

        root = findViewById(R.id.root);
        AdSettings.addTestDevice("b79d5b8e-8506-4781-bd39-f8f506a6755b");
        fbAboveContent = findViewById(R.id.fbAdAboveContent);
        fbBelowContent = findViewById(R.id.fbAdBelowContent);
        relatedPostTitle = findViewById(R.id.relatedPostTitle);
        aboveContent = findViewById(R.id.adViewAboveContent);
        belowContent = findViewById(R.id.adViewBelowContent);
        metaContents = findViewById(R.id.metaTextView);
        featureImageView = findViewById(R.id.featuredImage);
        titleTextView = findViewById(R.id.titleTextView);
        contentWebView = findViewById(R.id.contentWebView);
        chipGroup = findViewById(R.id.postDetailChipGroup);
        commentBtn = findViewById(R.id.commentsBtn);
        loadingView = findViewById(R.id.loading_layout);
        speakBtn = findViewById(R.id.speakBtn);
        speakBtn.setOnClickListener(view -> readPost());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //setting initial category

        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);

        if (title!=null)
            titleTextView.setText(Jsoup.parse(title).text());
        Glide.with(getApplicationContext()).load(imageUrl).into(featureImageView);

        if (!MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isSpeakEnabled()){
            speakBtn.setVisibility(View.GONE);
        }

        shareBtn = findViewById(R.id.shareBtn);
        saveBtn = findViewById(R.id.saveBtn);
        shareBtn.setOnClickListener(view -> {
            //new SharePost(getApplicationContext()).execute(currentPost.getFeaturedImgUrl());
            shareLink();
            Toast.makeText(getApplicationContext(),"Choose the app to share with",Toast.LENGTH_SHORT).show();
        });
        saveBtn.setOnClickListener(view -> {
            if (!offline) {
                if (currentPost!=null) {
                    new SavePost().execute(currentPost);
                    offline = true;
                    saveBtn.setIcon(getResources().getDrawable(R.drawable.ic_delete));
                    showSnackbar(getResources().getString(R.string.post_saved_successfully));
                }
            }else {
                if (currentPost!=null){
                    new DeletePost().execute(currentPost);
                    offline = false;
                    saveBtn.setIcon(getResources().getDrawable(R.drawable.ic_save));
                    showSnackbar(getResources().getString(R.string.post_deleted_successfully));
                }
            }
        });
        if (offline){
            saveBtn.setIcon(getResources().getDrawable(R.drawable.ic_delete));
        }

        if (nightMode){
            commentBtn.setTextColor(getResources().getColor(R.color.md_white_1000));
            saveBtn.setIconTint(ContextCompat.getColorStateList(getApplicationContext(),R.color.md_white_1000));
            shareBtn.setIconTint(ContextCompat.getColorStateList(getApplicationContext(),R.color.md_white_1000));
        }
        if (offline){
            loadOfflinePost(postId);
        }else {
            loadPost();
        }
        MainApplication.INT_AD_COUNTER++;
    }

    private void populateChipGroup(){
        if (!offline) {
            if (currentPost != null) {
                for (CategoriesDetailItem item : currentPost.getCategoriesDetails()) {
                    Chip chip = new Chip(getApplicationContext());
                    chip.setText(item.getName());
                    chip.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                        intent.putExtra("screen", "posts");
                        intent.putExtra("category", String.valueOf(item.getId()));
                        intent.putExtra("title", item.getName());
                        startActivity(intent);
                    });
                    chipGroup.addView(chip);
                }
            }
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
        if (postId!=0) {
            mViewModel.getPost(postId).observe(this, post -> {
                if (post != null) {
                    setPostData(post);
                    new Handler().postDelayed(() -> {
                        if(Config.ADMOB_ADS)
                            inflateAds();
                        if (Config.FB_ADS)
                            inflateFBAds();
                    },2000);
                }
            });
        }else {
            mViewModel.getPost(slug).observe(this, post -> {
                if (post != null) {
                    setPostData(post);
                    new Handler().postDelayed(() -> {
                        if(Config.ADMOB_ADS)
                            inflateAds();
                        if (Config.FB_ADS)
                            inflateFBAds();
                    },2000);
                }
            });
        }
    }

    private void inflateAds(){
        //inflateFBAds();
        if (MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isBannerAdsEnabled() && !MainApplication.getAppSettings(getApplicationContext()).getSettings().getAdsType().equals("disabled")) {
            AdRequest adRequest = new AdRequest.Builder().build();
            aboveContent.loadAd(adRequest);
            aboveContent.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Utils.showDebugToast("Banner ad failed"+loadAdError.getMessage(),getApplicationContext());
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Utils.showDebugToast("Top Banner ad loaded",getApplicationContext());
                    aboveContent.setVisibility(View.VISIBLE);
                }
            });
            AdRequest adRequest2 = new AdRequest.Builder().build();
            belowContent.loadAd(adRequest2);
            belowContent.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Utils.showDebugToast("Banner ad failed"+loadAdError.getMessage(),getApplicationContext());
                }
                @Override
                public void onAdLoaded() {
                    belowContent.setVisibility(View.VISIBLE);
                }
            });
        }

        if(MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isInterstitialAdsEnabled() && !MainApplication.getAppSettings(getApplicationContext()).getSettings().getAdsType().equals("disabled")){
            Utils.showDebugToast("Loading full screen ad",getApplicationContext());
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this,getResources().getString(R.string.admob_interstitial_ad_id), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            Log.i(TAG, "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i(TAG, loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });
        }

    }

    private void inflateFBAds(){
        adView2 = new com.facebook.ads.AdView(getApplicationContext(), getResources().getString(R.string.fb_banner_ad_placement_id), AdSize.BANNER_HEIGHT_50);

        adView = new com.facebook.ads.AdView(getApplicationContext(), getResources().getString(R.string.fb_banner_ad_placement_id), AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        fbBelowContent.addView(adView);
        fbAboveContent.addView(adView2);

        // Request an ad
        adView.loadAd();
        adView2.loadAd();

        fbInterstitialAd();
    }

    String TAG = "DetailActivity";
    private void fbInterstitialAd(){

        fbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), getResources().getString(R.string.fb_interstitial_ad_placement_id));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                if(fbInterstitialAd.isAdLoaded()) {
                    fbInterstitialAd.show();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        fbInterstitialAd.loadAd(
                fbInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    private void setPostData(Post postData){
        currentPost = postData;
        if (postData.getFeaturedImgUrl()!=null&&!offline) {
            Glide.with(getApplicationContext())
                    .load(postData.getFeaturedImgUrl())
                    .into(featureImageView);
        }
        titleTextView.setText(Jsoup.parse(postData.getTitle().getRendered()).text());

        //Build meta string
        String metaString = "{faw-calendar} "+ Utils.parseDate(postData.getModified()) +"   {faw-user} "+postData.getAuthorName();
        metaContents.setText(metaString);
        metaContents.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),ContainerActivity.class);
            intent.putExtra("screen","posts");
            intent.putExtra("title", String.format(getResources().getString(R.string.posts_by_author),postData.getAuthorName()));
            intent.putExtra("author",String.valueOf(postData.getAuthor()));
            startActivity(intent);
        });

        //Load Tags Chips
        //populateChipGroup();

        //comment btn
        commentBtn.setVisibility(View.VISIBLE);
        commentBtn.setText(String.format(getResources().getString(R.string.comments_text),String.valueOf(postData.getCommentCount())));
        commentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("title",postData.getCommentCount()+" Comments");
            intent.putExtra("screen","comments");
            intent.putExtra("postId",postData.getId());
            startActivity(intent);
        });
        //WebViewContent
        //WebViewContent
        String css = "<style>@font-face {\n" +
                "    font-family: 'DefaultFont';\n" +
                "    font-style: normal;\n" +
                "    font-weight: 400;\n" +
                "    src: local('DefaultFont Regular'), local('Slabo'), url(file:///android_asset/fonts/DefaultFont.ttf) format('truetype');\n" +
                "}\n" +
                "body{max-width:100%!important;direction:ltr}\n" +
                "*{font-family: 'DefaultFont', sans-serif; color:#666;font-size: 18px; text-align: left;line-height:26px;}\n" +
                "iframe,video{max-width:100%!important;}\n" +
                "a{color:#2874f0;}\n" +
                "div{width:auto!important;height:auto!important;}\n" +
                "img{max-width:100%!important;width:100%!important;height:auto!important;display:block;margin:5px auto;}\n" +
                "figure{max-width:100%!important;width:100%!important;margin:0px}\n</style>";
        if(Config.FORCE_RTL){
            css = "<style>@font-face {\n" +
                    "    font-family: 'DefaultFont';\n" +
                    "    font-style: normal;\n" +
                    "    font-weight: 400;\n" +
                    "    src: local('DefaultFont Regular'), local('Slabo'), url(file:///android_asset/fonts/DefaultFont.ttf) format('truetype');\n" +
                    "}\n" +
                    "body{max-width:100%!important;direction:rtl}\n" +
                    "*{font-family: 'DefaultFont', sans-serif; color:#666;font-size: 18px; text-align: right;line-height:26px;}\n" +
                    "iframe,video{max-width:100%!important;}\n" +
                    "a{color:#2874f0;}\n" +
                    "div{width:auto!important;height:auto!important;}\n" +
                    "img{max-width:100%!important;width:100%!important;height:auto!important;display:block;margin:5px auto;}\n" +
                    "figure{max-width:100%!important;width:100%!important;margin:0px}\n</style>";
        }
        String script = "<script>console.log(\"Javascript file detected\");\n" +
                "\n" +
                "function siteUrlClicked(url){\n" +
                "    Android.siteUrlClicked(url);\n" +
                "}\n" +
                "function imageClicked(index){\n" +
                "    Android.imageClicked(index);\n" +
                "}\n" +
                "function youtubeUrl(url){\n" +
                "    Android.youtubeUrl(url);\n" +
                "}</script>";
        if(nightMode){
            css += "<style>*{background-color:#111111;color:#fff!important;}</style>";
        }/*else {
            css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\"><script src=\"script.js\"></script>";
        }*/
        //css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\"><script src=\"script.js\"></script>";
        String html = css + script + postData.getContent().getRendered();
        //html = html.replaceAll("src=\"//","src=\"https://");
        Document document = Jsoup.parse(html);


        Elements imgs = document.select("img");
        Elements atagsWithImg = document.select("a[href*='.jpg'],a[href*='.png']");
        for (Element aTag : atagsWithImg){
            Element e = aTag.selectFirst("img");
            aTag.replaceWith(e);
        }
        for (int i=0;i<imgs.size();i++){
            images.add(imgs.get(i).attr("src"));
            imgs.get(i).attr("onclick","imageClicked("+i+")");
        }
        Elements atags = document.select("a[href^=\""+ Config.SITE_URL+"\"]");
        for(Element e:atags){
            if (e.toString().contains(".png")||e.toString().contains(".jpg")||e.toString().contains(".gif")||
                    e.toString().contains(".zip")||e.toString().contains(".pdf")||e.toString().contains(".mp3")||e.toString().contains(".mp4")||e.toString().contains(".rar")){
                e.attr("download");
            }else {
                e.attr("onclick", "siteUrlClicked('" + e.attr("href") + "')");
                e.attr("href", "#");
                //Log.e("PostFragment", e.html());
            }
        }

        Elements rem = document.select("img[srcset]");
        for (Element img: rem){
            img.removeAttr("srcset");
            //Log.e("Tag","Tag; "+img.toString());
        }
        if(currentPost.getFeaturedImgUrl()!=null) {
            Element element = document.selectFirst("img");
            if (element != null && element.attr("src").equals(currentPost.getFeaturedImgUrl())) {
                element.remove();
            }
        }

        contentWebView.addJavascriptInterface(new WebAppInterface(getApplicationContext(),contentWebView,images), "Android");
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setAllowFileAccess(true);
        contentWebView.getSettings().setAllowContentAccess(true);
        contentWebView.getSettings().setAppCacheEnabled(true);
        //Handler handler = new Handler();
        //handler.postDelayed(() -> contentWebView.loadDataWithBaseURL("file:///android_asset/",document.html(),"text/html","UTF-8",null), 10) ;
        Log.e("MakingRequest","HTML : "+document.html().substring(100,200));
        //contentWebView.postDelayed(() -> contentWebView.loadDataWithBaseURL("file:///android_asset/",document.html(),"text/html","UTF-8",null),1000);
        //contentWebView.postDelayed(() -> contentWebView.loadData(document.html(),"text/html","UTF-8"),1000);
        contentWebView.setVisibility(View.VISIBLE);
        if (!MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().isContentCopyEnabled()){
            contentWebView.setOnLongClickListener(view -> true);
            contentWebView.setLongClickable(false);
            contentWebView.setHapticFeedbackEnabled(false);
        }

        contentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String option = MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getExternalLinks();
                String url;
                String path;
                try {
                    url = request.getUrl().toString();
                    path = request.getUrl().getHost();
                }catch (Exception e){
                    return false;
                }

                if (!url.contains(Config.SITE_URL)){
                    //This is external url
                    switch (option) {
                        case "app": {
                            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                            intent.putExtra("screen", "webview");
                            intent.putExtra("url", url);
                            startActivity(intent);
                            break;
                        }
                        case "chrome": {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage("com.android.chrome");
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                // Chrome browser presumably not installed so allow user to choose instead
                                intent.setPackage(null);
                                startActivity(intent);
                            }
                            break;
                        }
                        case "chooser":
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            break;
                    }
                }
                return true;
            }
        });

        new Handler().postDelayed(() -> {

        },2000);

        loadingView.setVisibility(View.GONE);
        contentWebView.loadDataWithBaseURL("file:///android_asset/",document.html(),"text/html","UTF-8",null);
        addRelatedPostsLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        contentWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        contentWebView.onPause();
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

    private void addRelatedPostsLayout(){
        if(offline){
            relatedPostTitle.setVisibility(View.GONE);
        }else {
            if(isActive) {
                RelatedPostFragment relatedPostFragment = RelatedPostFragment.newInstance(getCategoryString());
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.relatedPostFrame, relatedPostFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    private String getCategoryString(){
        if (currentPost.getCategories()!=null) {
            Integer[] arr = currentPost.getCategories().toArray(new Integer[currentPost.getCategories().size()]);
            String s = Arrays.toString(arr);
            s = s.substring(1, s.length() - 1);
            return s;
        }else {
            return null;
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                showSnackbar(getResources().getString(R.string.text_to_speak_language_not_supported));
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void readPost(){
        if (tts.isSpeaking()){
            tts.stop();
        }else {
            if (currentPost!=null) {
                tts.speak(Jsoup.parse(currentPost.getContent().getRendered()).text(), TextToSpeech.QUEUE_FLUSH, null,null);
                showSnackbar(getResources().getString(R.string.text_to_speak_initializing_reader));
            }
        }
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

    @Override
    public void onBackPressed() {
        if(tts.isSpeaking())
            tts.stop();
        if (MainApplication.INT_AD_COUNTER>=MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getInterstitialAdFrequency()){
            if (mInterstitialAd!=null||fbInterstitialAd!=null){
                MainApplication.INT_AD_COUNTER = 0;
                if(mInterstitialAd!=null) {
                    mInterstitialAd.show(this);
                }
                if(fbInterstitialAd!=null&&fbInterstitialAd.isAdLoaded()) {
                    fbInterstitialAd.show();
                }
            }else {
                if(fromNotification){
                    finish();
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                }else{
                    finish();
                }
            }
        }else {
            if(fromNotification){
                finish();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }else{
                finish();
            }
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

    class SavePost extends AsyncTask<Post,Void,Void> {

        @Override
        protected Void doInBackground(Post... posts) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getApplicationContext());
            PostDao postDao = postDatabase.postsDao();
            postDao.insertPost(posts[0]);
            return null;
        }
    }

    class DeletePost extends AsyncTask<Post,Void,Void>{

        @Override
        protected Void doInBackground(Post... posts) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getApplicationContext());
            PostDao postDao = postDatabase.postsDao();
            postDao.deletePost(posts[0]);
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
                        .get(); // needs to be called on background thread

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
            //intent.putExtra(Intent.EXTRA_STREAM, result);
            startActivity(Intent.createChooser(intent, "Share post"));
        }
    }

    private void shareLink(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Jsoup.parse(currentPost.getTitle().getRendered()).text()+"\n"+Config.SITE_URL+"?p="+currentPost.id);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void showSnackbar(String text){
        Snackbar snackbar = Snackbar.make(root,text,Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.snackbar_close_text), view -> snackbar.dismiss())
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}
