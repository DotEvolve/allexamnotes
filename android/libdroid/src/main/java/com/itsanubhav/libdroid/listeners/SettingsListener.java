package com.itsanubhav.libdroid.listeners;

import com.itsanubhav.libdroid.model.settings.AppSettings;

public interface SettingsListener {
        void onSuccessful(AppSettings settings);

        void onFaliure(String errorMsg);
}
