package com.itsanubhav.wordroid4.others;

import android.net.Uri;

public class ImageFileProvider extends androidx.core.content.FileProvider {
    @Override
    public String getType(Uri uri) { return "image/jpeg"; }
}