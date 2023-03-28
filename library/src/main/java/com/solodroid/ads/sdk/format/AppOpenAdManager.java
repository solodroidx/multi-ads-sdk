package com.solodroid.ads.sdk.format;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

import java.util.Date;

public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAd";
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AppOpenAdManager() {
    }

    public void loadAd(Context context, String adManagerAppOpenAdUnitId) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        @SuppressLint("VisibleForTests") AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        AppOpenAd.load(context, adManagerAppOpenAdUnitId, request, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                appOpenAd = ad;
                isLoadingAd = false;
                loadTime = (new Date()).getTime();
                Log.d(LOG_TAG, "onAdLoaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoadingAd = false;
                Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
            }
        });
    }

    public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String appOpenAdUnitId) {
        showAdIfAvailable(activity, appOpenAdUnitId, () -> {
        });
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String appOpenAdUnitId, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener.onShowAdComplete();
            loadAd(activity, appOpenAdUnitId);
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                isShowingAd = false;

                Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");

                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, appOpenAdUnitId);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                appOpenAd = null;
                isShowingAd = false;
                Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, appOpenAdUnitId);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
            }
        });

        isShowingAd = true;
        appOpenAd.show(activity);
    }
}