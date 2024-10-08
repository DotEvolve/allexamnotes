package com.allexamnotes.app.others;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.allexamnotes.app.DetailActivity;
import com.allexamnotes.app.ImageViewerActivity;
import com.allexamnotes.app.PostContainerActivity;

import java.util.List;

public class WebAppInterface {
    private Context mContext;
    private WebView webView;
    List<String> img;

    public WebAppInterface(Context c, WebView webView, List<String> imgs) {
        mContext = c;
        this.webView = webView;
        this.img = imgs;
    }

    @JavascriptInterface
    public void siteUrlClicked(String url){
        if(mContext instanceof PostContainerActivity) {
            ((PostContainerActivity) mContext).addFragment(getSlugFromUrl(url));
        }else {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("slug",getSlugFromUrl(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    @JavascriptInterface
    public void imageClicked(int index){
        Intent intent = new Intent(mContext, ImageViewerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("size",img.size());
        intent.putExtra("index",index);
        for(int j=0;j<img.size();j++)
            intent.putExtra("image_"+j,""+img.get(j));
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void youtubeUrl(String url){
        Toast.makeText(mContext,"Url: "+url,Toast.LENGTH_SHORT).show();
    }

    private String getSlugFromUrl(String url){
        Uri uri = Uri.parse(url);
        return uri.getLastPathSegment();
    }
}
