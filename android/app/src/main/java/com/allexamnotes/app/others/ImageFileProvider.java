package com.allexamnotes.app.others;

import android.net.Uri;

public class ImageFileProvider extends androidx.core.content.FileProvider {
    @Override
    public String getType(Uri uri) { return "image/jpeg"; }
}