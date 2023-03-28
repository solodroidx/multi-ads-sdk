package com.solodroid.ads.sdk.format;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

import java.util.Date;

public class AppOpenAdAppLovin {

    private static final String LOG_TAG = "AppOpenAd";
    private MaxAppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AppOpenAdAppLovin() {
    }

    public void loadAd(Context context, String maxAppOpenAdUnitId) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }
        isLoadingAd = true;
        appOpenAd = new MaxAppOpenAd(maxAppOpenAdUnitId, context);
        appOpenAd.loadAd();
        appOpenAd.setListener(maxAdListener);
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
        appOpenAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                isLoadingAd = false;
                loadTime = (new Date()).getTime();
                Log.d(LOG_TAG, "onAdLoaded.");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                appOpenAd = null;
                isShowingAd = false;
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, appOpenAdUnitId);
                Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                isLoadingAd = false;
                appOpenAd = null;
                isShowingAd = false;
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, appOpenAdUnitId);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                isLoadingAd = false;
                appOpenAd = null;
                isShowingAd = false;
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, appOpenAdUnitId);
            }
        });

        isShowingAd = true;
        appOpenAd.showAd();
    }

    MaxAdListener maxAdListener = new MaxAdListener() {
        @Override
        public void onAdLoaded(MaxAd ad) {
            isLoadingAd = false;
            loadTime = (new Date()).getTime();
        }

        @Override
        public void onAdDisplayed(MaxAd ad) {
        }

        @Override
        public void onAdHidden(MaxAd ad) {
            appOpenAd.loadAd();
        }

        @Override
        public void onAdClicked(MaxAd ad) {
        }

        @Override
        public void onAdLoadFailed(String adUnitId, MaxError error) {
            isLoadingAd = false;
        }

        @Override
        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            isLoadingAd = false;
            appOpenAd.loadAd();
        }
    };

}