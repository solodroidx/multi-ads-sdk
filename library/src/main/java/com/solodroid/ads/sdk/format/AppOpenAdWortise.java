package com.solodroid.ads.sdk.format;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

import java.util.Date;

public class AppOpenAdWortise {

    private static final String LOG_TAG = "AppOpenAd";
    private com.wortise.ads.appopen.AppOpenAd wortiseAppOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AppOpenAdWortise() {
    }

    public void loadAd(Context context, String wortiseAppOpenId) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }
        isLoadingAd = true;
        wortiseAppOpenAd = new com.wortise.ads.appopen.AppOpenAd(context, wortiseAppOpenId);
        wortiseAppOpenAd.setListener(wortiseAppOpenAdListener);
        wortiseAppOpenAd.loadAd();
    }

    public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return wortiseAppOpenAd != null && wortiseAppOpenAd.isAvailable() && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String appOpenAdUnitId) {
        showAdIfAvailable(activity, appOpenAdUnitId, () -> {
        });
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String wortiseAppOpenAdUnitId, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener.onShowAdComplete();
            loadAd(activity, wortiseAppOpenAdUnitId);
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");
        wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
            @Override
            public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

            }

            @Override
            public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                wortiseAppOpenAd = null;
                isShowingAd = false;
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, wortiseAppOpenAdUnitId);
                Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
            }

            @Override
            public void onAppOpenFailed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                isLoadingAd = false;
                wortiseAppOpenAd = null;
                isShowingAd = false;
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, wortiseAppOpenAdUnitId);
            }

            @Override
            public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                isLoadingAd = false;
                loadTime = (new Date()).getTime();
                Log.d(LOG_TAG, "onAdLoaded.");
            }

            @Override
            public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

            }
        });

        isShowingAd = true;
        wortiseAppOpenAd.showAd(activity);
    }

    com.wortise.ads.appopen.AppOpenAd.Listener wortiseAppOpenAdListener = new com.wortise.ads.appopen.AppOpenAd.Listener() {
        @Override
        public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

        }

        @Override
        public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
            wortiseAppOpenAd.loadAd();
        }

        @Override
        public void onAppOpenFailed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
            isLoadingAd = false;
            wortiseAppOpenAd.loadAd();
        }

        @Override
        public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
            isLoadingAd = false;
            loadTime = (new Date()).getTime();
        }

        @Override
        public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

        }
    };

}