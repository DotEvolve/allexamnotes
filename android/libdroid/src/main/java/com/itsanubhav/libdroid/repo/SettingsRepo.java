package com.itsanubhav.libdroid.repo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.itsanubhav.libdroid.Utility;
import com.itsanubhav.libdroid.listeners.SettingsListener;
import com.itsanubhav.libdroid.model.settings.AppSettings;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SettingsRepo {

    private SettingsListener listener;

    public SettingsListener getListener() {
        return listener;
    }

    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }

    public void getSettings(final Context context){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AppSettings> call =  apiInterface.getSettings(Utility.getRandomString());
        Log.e("Making Request", call.request().url().toString());
        call.enqueue(new Callback<AppSettings>() {
            @Override
            public void onResponse(Call<AppSettings> call, Response<AppSettings> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    if (response.body().getSettings()!=null)
                        listener.onSuccessful(response.body());
                    else
                        listener.onFaliure("Setting not saved");
                }else {
                    listener.onFaliure("Something wrong with the settings");
                }
            }

            @Override
            public void onFailure(Call<AppSettings> call, Throwable t) {
                Toast.makeText(context,"Error in JSON : "+t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
