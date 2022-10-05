package com.allexamnotes.app.fragment.post;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.allexamnotes.app.Config;
import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.MainApplication;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.relatedpost.RelatedPostFragment;
import com.allexamnotes.app.others.Utils;
import com.allexamnotes.app.others.WebAppInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.allexamnotes.libdroid.database.PostDatabase;
import com.allexamnotes.libdroid.database.dao.PostDao;
import com.allexamnotes.libdroid.model.post.CategoriesDetailItem;
import com.allexamnotes.libdroid.model.post.Post;
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

import im.delight.android.webview.AdvancedWebView;

import static android.content.Context.DOWNLOAD_SERVICE;

public class PostFragment extends Fragment implements TextToSpeech.OnInitListener {

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
    private AdvancedWebView contentWebView;
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

    public static PostFragment newInstance(int postID) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PostFragment newInstance(String slug) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("slug",slug);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PostFragment newInstance(int postID,String title,String img) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postID);
        bundle.putString("title",title);
        bundle.putString("img",img);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PostFragment newInstance(int postID,String title,String img,boolean offline) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postID);
        bundle.putString("title",title);
        bundle.putString("img",img);
        bundle.putBoolean("offline",offline);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            nightMode = true;
        }else {
            nightMode = false;
        }
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
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_fragment, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Config.defaultSharedPref,Context.MODE_PRIVATE);
        root = rootView;
        if (getArguments()!=null){
            postId = getArguments().getInt("postId");
            imageUrl = getArguments().getString("img");
            title = getArguments().getString("title");
            slug = getArguments().getString("slug");
            offline = getArguments().getBoolean("offline",false);
        }else {
            getActivity().finish();
        }
        tts = new TextToSpeech(getContext(), this);

        fbAboveContent = rootView.findViewById(R.id.fbAdAboveContent);
        fbBelowContent = rootView.findViewById(R.id.fbAdBelowContent);
        relatedPostTitle = rootView.findViewById(R.id.relatedPostTitle);
        aboveContent = rootView.findViewById(R.id.adViewAboveContent);
        belowContent = rootView.findViewById(R.id.adViewBelowContent);
        metaContents = rootView.findViewById(R.id.metaTextView);
        featureImageView = rootView.findViewById(R.id.featuredImage);
        titleTextView = rootView.findViewById(R.id.titleTextView);
        contentWebView = rootView.findViewById(R.id.contentWebView);
        chipGroup = rootView.findViewById(R.id.postDetailChipGroup);
        commentBtn = rootView.findViewById(R.id.commentsBtn);
        loadingView = rootView.findViewById(R.id.loading_layout);
        speakBtn = rootView.findViewById(R.id.speakBtn);
        speakBtn.setOnClickListener(view -> readPost());

        contentWebView.getSettings().setJavaScriptEnabled(true);

        if (title!=null)
            titleTextView.setText(Jsoup.parse(title).text());
        Glide.with(getContext()).load(imageUrl).into(featureImageView);

        if (!MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().isSpeakEnabled()){
            speakBtn.setVisibility(View.GONE);
        }

        shareBtn = rootView.findViewById(R.id.shareBtn);
        saveBtn = rootView.findViewById(R.id.saveBtn);
        shareBtn.setOnClickListener(view -> {
            //new SharePost(getContext()).execute(currentPost.getFeaturedImgUrl());
            shareLink();
            Toast.makeText(getContext(),"Choose the app to share with",Toast.LENGTH_SHORT).show();
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
            saveBtn.setIconTint(ContextCompat.getColorStateList(getContext(),R.color.md_white_1000));
            shareBtn.setIconTint(ContextCompat.getColorStateList(getContext(),R.color.md_white_1000));
        }
        inflateAds();
        showInterstitiaAd();
        return rootView;
    }

    private void populateChipGroup(){
        if (!offline) {
            if (currentPost != null) {
                for (CategoriesDetailItem item : currentPost.getCategoriesDetails()) {
                    Chip chip = new Chip(getContext());
                    chip.setText(item.getName());
                    chip.setOnClickListener(view -> {
                        Intent intent = new Intent(getContext(), ContainerActivity.class);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (offline){
            loadOfflinePost(postId);
        }else {
            if (slug==null) {
                mViewModel.getPost(postId).observe(getViewLifecycleOwner(), post -> {
                    if (post != null) {
                        setPostData(post);
                    }
                });
            }else {
                mViewModel.getPost(slug).observe(getViewLifecycleOwner(), post -> {
                    if (post != null) {
                        setPostData(post);
                    }
                });
            }
        }
    }

    private void loadPost(){
        if (imageUrl!=null)
            Glide.with(getContext())
                    .load(imageUrl)
                    .into(featureImageView);

        if (title!=null)
            titleTextView.setText(Jsoup.parse(title).text());

        mViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mViewModel.getPost(postId).observe(getViewLifecycleOwner(), post -> {
            if (post!=null){
                setPostData(post);
                //inflateAds();
            }
        });
    }

    private void inflateAds(){
        //inflateFBAds();
        if (MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().isBannerAdsEnabled()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            aboveContent.loadAd(adRequest);
            aboveContent.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    aboveContent.setVisibility(View.VISIBLE);
                }
            });
            AdRequest adRequest2 = new AdRequest.Builder().build();
            belowContent.loadAd(adRequest2);
            belowContent.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    belowContent.setVisibility(View.VISIBLE);
                }
            });
        }

        if(MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().isInterstitialAdsEnabled()){
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(getActivity(),getResources().getString(R.string.admob_banner_ad_unit_id), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                        }
                    });
        }

        /*AdRequest adRequest = new AdRequest.Builder().build();
        aboveContent.loadAd(adRequest);
        aboveContent.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                aboveContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });
        AdRequest adRequest2 = new AdRequest.Builder().build();
        belowContent.loadAd(adRequest2);
        belowContent.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                belowContent.setVisibility(View.VISIBLE);
            }
        });


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_banner_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                //
            }
        });*/

    }

    private void inflateFBAds(){
        adView2 = new com.facebook.ads.AdView(getContext(), getResources().getString(R.string.fb_interstitial_ad_placement_id), AdSize.BANNER_HEIGHT_50);

        adView = new com.facebook.ads.AdView(getContext(), getResources().getString(R.string.fb_interstitial_ad_placement_id), AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        fbBelowContent.addView(adView);
        fbAboveContent.addView(adView2);

        // Request an ad
        adView.loadAd();
        adView2.loadAd();
    }

    private void fbInterstitialAd(){
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(getContext(), getResources().getString(R.string.fb_interstitial_ad_placement_id));
        // load the ad
        fbInterstitialAd.loadAd();
    }

    private boolean showInterstitiaAd(){
        int postCount = sharedPreferences.getInt("postRead",0);
        if (postCount>=MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().getInterstitialAdFrequency()){
            /*if (mInterstitialAd.isLoaded()){
                editor.putInt("postcount",0);
                mInterstitialAd.show();
            }*/
            fbInterstitialAd();
            return true;
        }else {
            return false;
        }

    }


    private void setPostData(Post postData){
        currentPost = postData;
        if (postData.getFeaturedImgUrl()!=null&&!offline) {
            Glide.with(getContext())
                    .load(postData.getFeaturedImgUrl())
                    .into(featureImageView);
        }
        titleTextView.setText(Jsoup.parse(postData.getTitle().getRendered()).text());

        //Build meta string
        String metaString = "{faw-calendar} "+ Utils.parseDate(postData.getDate()) +"   {faw-user} "+postData.getAuthorName();
        metaContents.setText(metaString);
        metaContents.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(),ContainerActivity.class);
            intent.putExtra("screen","posts");
            intent.putExtra("title", String.format(getResources().getString(R.string.posts_by_author),postData.getAuthorName()));
            intent.putExtra("author",String.valueOf(postData.getAuthor()));
            startActivity(intent);
        });

        //Load Tags Chips
        populateChipGroup();

        //LoadRelatedPosts
        addRelatedPostsLayout();

        //comment btn
        commentBtn.setVisibility(View.VISIBLE);
        commentBtn.setText(String.format(getResources().getString(R.string.comments_text),String.valueOf(postData.getCommentCount())));
        commentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("title",postData.getCommentCount()+" Comments");
            intent.putExtra("screen","comments");
            intent.putExtra("postId",postData.getId());
            startActivity(intent);
        });
        //WebViewContent
        String css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\" ><script src=\"script.js\"></script>";
        if(nightMode){
            css = "<link rel=\"stylesheet\" href=\"defaultstyles.css\" ><script src=\"script.js\"></script><style>*{background-color:#111111;color:#fff!important;}</style>";
        }

        Document document = Jsoup.parse(css + postData.getContent().getRendered());
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
                Log.e("PostFragment", e.html());
            }
        }

        Elements rem = document.select("img[srcset]");
        for (Element img: rem){
            img.removeAttr("srcset");
            Log.e("Tag","Tag; "+img.toString());
        }
        if(currentPost.getFeaturedImgUrl()!=null) {
            Element element = document.selectFirst("img");
            if (element != null && element.attr("src").equals(currentPost.getFeaturedImgUrl())) {
                element.remove();
            }
        }

        loadingView.setVisibility(View.GONE);
        contentWebView.addJavascriptInterface(new WebAppInterface(getActivity(),contentWebView,images), "Android");
        contentWebView.postDelayed(() -> contentWebView.loadDataWithBaseURL("file:///android_asset/",document.html(),"text/html","UTF-8",null),1000);
        if (!MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().isContentCopyEnabled()){
            contentWebView.setOnLongClickListener(view -> true);
            contentWebView.setLongClickable(false);
            contentWebView.setHapticFeedbackEnabled(false);
        }


        contentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String option = MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().getExternalLinks();
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
                            Intent intent = new Intent(getContext(), ContainerActivity.class);
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

        //TODO: Change the download listener file name
        contentWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(url));

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Name of your downloadble file goes here, example: Mathematics II ");
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getContext(), "Downloading File", //To notify the Client that the file is being downloaded
                    Toast.LENGTH_LONG).show();

        });

        contentWebView.addPermittedHostname("file:///android_asset/");


        contentWebView.setListener(getActivity(), new AdvancedWebView.Listener() {
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
                Toast.makeText(getContext(),"Download requested "+suggestedFilename,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExternalPageRequest(String url) {
                String option = MainApplication.getAppSettings(getContext()).getSettings().getPostSettings().getExternalLinks();
                switch (option) {
                    case "app": {
                        Intent intent = new Intent(getContext(), ContainerActivity.class);
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
        });
    }

    private void loadOfflinePost(int postId){
        PostDatabase postDatabase = PostDatabase.getAppDatabase(getContext());
        PostDao postDao = postDatabase.postsDao();
        LiveData<List<Post>> posts = postDao.getPost(postId);
        posts.observe(getViewLifecycleOwner(),posts1 -> {
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
            RelatedPostFragment relatedPostFragment;
            relatedPostFragment = RelatedPostFragment.newInstance(getCategoryString());
            getChildFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.relatedPostFrame, relatedPostFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getCategoryString(){
        if (currentPost.getCategories()!=null) {
            Integer[] arr = currentPost.getCategories().toArray(new Integer[currentPost.getCategories().size()]);
            String s = Arrays.toString(arr);
            s = s.substring(1, s.length() - 1);
            //Log.e("Categories", "" + s);
            return s;
        }else {
            return null;
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("hi","IN"));

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
    public void onResume() {
        super.onResume();
        if (contentWebView!=null)
            contentWebView.onResume();
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu,menu);
        menu.findItem(R.id.option_share).setVisible(true);
    }

    class SavePost extends AsyncTask<Post,Void,Void> {

        @Override
        protected Void doInBackground(Post... posts) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getContext());
            PostDao postDao = postDatabase.postsDao();
            postDao.insertPost(posts[0]);
            return null;
        }
    }

    class DeletePost extends AsyncTask<Post,Void,Void>{

        @Override
        protected Void doInBackground(Post... posts) {
            PostDatabase postDatabase = PostDatabase.getAppDatabase(getContext());
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
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Jsoup.parse(currentPost.getTitle().getRendered()).text()+"\n"+currentPost.getLink());
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void showSnackbar(String text){
        Snackbar snackbar = Snackbar.make(root,text,Snackbar.LENGTH_LONG);
                snackbar.setAction(getResources().getString(R.string.snackbar_close_text), view -> snackbar.dismiss())
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tts.isSpeaking()){
            tts.stop();
        }
        if (contentWebView!=null)
            contentWebView.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (tts!=null){
            tts.shutdown();
        }
    }

}
