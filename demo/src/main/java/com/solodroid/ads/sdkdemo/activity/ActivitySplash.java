package com.solodroid.ads.sdkdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdk.format.AppOpenAd;
import com.solodroid.ads.sdkdemo.BuildConfig;
import com.solodroid.ads.sdkdemo.R;
import com.solodroid.ads.sdkdemo.callback.CallbackConfig;
import com.solodroid.ads.sdkdemo.data.Constant;
import com.solodroid.ads.sdkdemo.database.SharedPref;
import com.solodroid.ads.sdkdemo.rest.RestAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";
    Call<CallbackConfig> callbackConfigCall = null;
    public static int DELAY_PROGRESS = 1500;
    AdNetwork.Initialize adNetwork;
    AppOpenAd.Builder appOpenAdBuilder;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = new SharedPref(this);
        initAds();
        requestConfig();
    }

    private void requestConfig() {
        requestAPI("https://raw.githubusercontent.com/solodroidev/content/uploads/json/android.json");
    }

    private void requestAPI(@SuppressWarnings("SameParameterValue") String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            if (url.contains("https://drive.google.com")) {
                String driveUrl = url.replace("https://", "").replace("http://", "");
                List<String> data = Arrays.asList(driveUrl.split("/"));
                String googleDriveFileId = data.get(3);
                callbackConfigCall = RestAdapter.createApi().getDriveJsonFileId(googleDriveFileId);
            } else {
                callbackConfigCall = RestAdapter.createApi().getJsonUrl(url);
            }
        } else {
            callbackConfigCall = RestAdapter.createApi().getDriveJsonFileId(url);
        }
        callbackConfigCall.enqueue(new Callback<CallbackConfig>() {
            public void onResponse(@NonNull Call<CallbackConfig> call, @NonNull Response<CallbackConfig> response) {
                CallbackConfig resp = response.body();
                if (resp != null) {
                    sharedPref.savePostList(resp.android);
                    loadOpenAds();
                    Log.d(TAG, "responses success");
                } else {
                    loadOpenAds();
                    Log.d(TAG, "responses null");
                }
            }

            public void onFailure(@NonNull Call<CallbackConfig> call, @NonNull Throwable th) {
                Log.d(TAG, "responses failed: " + th.getMessage());
                loadOpenAds();
            }
        });
    }

    private void initAds() {
        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setAdMobAppId(null)
                .setStartappAppId(Constant.STARTAPP_APP_ID)
                .setUnityGameId(Constant.UNITY_GAME_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setIronSourceAppKey(Constant.IRONSOURCE_APP_KEY)
                .setWortiseAppId(Constant.WORTISE_APP_ID)
                .setDebug(BuildConfig.DEBUG)
                .build();
    }

    private void loadOpenAds() {
        if (Constant.OPEN_ADS_ON_START) {
            appOpenAdBuilder = new AppOpenAd.Builder(this)
                    .setAdStatus(Constant.AD_STATUS)
                    .setAdNetwork(Constant.AD_NETWORK)
                    .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                    .setAdMobAppOpenId(Constant.ADMOB_APP_OPEN_AD_ID)
                    .setAdManagerAppOpenId(Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID)
                    .setApplovinAppOpenId(Constant.APPLOVIN_APP_OPEN_AP_ID)
                    .setWortiseAppOpenId(Constant.WORTISE_APP_OPEN_AD_ID)
                    .build(this::startMainActivity);
        } else {
            startMainActivity();
        }
    }

    public void startMainActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_PROGRESS);
    }

}
