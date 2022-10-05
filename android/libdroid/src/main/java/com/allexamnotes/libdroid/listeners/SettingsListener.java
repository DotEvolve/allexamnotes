package com.allexamnotes.libdroid.listeners;

import com.allexamnotes.libdroid.model.settings.AppSettings;

public interface SettingsListener {
        void onSuccessful(AppSettings settings);

        void onFaliure(String errorMsg);
}
