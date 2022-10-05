package com.allexamnotes.app.fragment.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.allexamnotes.app.Config;
import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.MainApplication;
import com.allexamnotes.app.R;
import com.onesignal.OneSignal;

public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RelativeLayout nightMode;
    private SwitchMaterial notificationSwitch;
    private RelativeLayout aboutUs;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.defaultSharedPref, Context.MODE_PRIVATE);

        aboutUs = rootView.findViewById(R.id.aboutUs);
        aboutUs.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("screen","webview");
            intent.putExtra("url", MainApplication.getAppSettings(getContext()).getSettings().getAboutUrl());
            startActivity(intent);
        });

        notificationSwitch = rootView.findViewById(R.id.notificationSwitch);
        boolean notificationState = sharedPreferences.getBoolean("notification",true);
        notificationSwitch.setChecked(notificationState);
        notificationSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("notification",b);
            editor.apply();
            OneSignal.disablePush(b);
        });

        nightMode = rootView.findViewById(R.id.nightMode);
        nightMode.setOnClickListener(view -> {
            showNighModeDialog();
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void showNighModeDialog(){
        int selected = sharedPreferences.getInt("dark_mode",0);
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),R.style.AlertDialogCustom))
                .setSingleChoiceItems(getResources().getStringArray(R.array.theme_options), selected, null)
                .setPositiveButton(R.string.dialog_ok_btn, (dialog, whichButton) -> {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    switch (selectedPosition){
                        case 1:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                        default:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                    }
                    editor = sharedPreferences.edit();
                    editor.putInt("dark_mode",selectedPosition);
                    editor.apply();
                    getActivity().recreate();
                })
                .show();
    }

}
