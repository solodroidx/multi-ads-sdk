package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.FACEBOOK;
import static com.solodroid.ads.sdk.util.Constant.FAN;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_ADMOB;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_IRONSOURCE;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.IRONSOURCE;
import static com.solodroid.ads.sdk.util.Constant.MOPUB;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;
import static com.solodroid.ads.sdk.util.Constant.UNITY;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.solodroid.ads.sdk.helper.AppLovinCustomEventInterstitial;
import com.solodroid.ads.sdk.util.OnInterstitialAdDismissedListener;
import com.solodroid.ads.sdk.util.OnInterstitialAdShowedListener;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.mediation.IInterstitialAdLoadListener;
import com.unity3d.mediation.IInterstitialAdShowListener;
import com.unity3d.mediation.errors.LoadError;
import com.unity3d.mediation.errors.ShowError;

import java.util.concurrent.TimeUnit;

public class InterstitialAd {

    @SuppressWarnings("deprecation")
    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private com.google.android.gms.ads.interstitial.InterstitialAd adMobInterstitialAd;
        private AdManagerInterstitialAd adManagerInterstitialAd;
        private com.facebook.ads.InterstitialAd fanInterstitialAd;
        private StartAppAd startAppAd;
        private com.unity3d.mediation.InterstitialAd unityInterstitialAd;
        private MaxInterstitialAd maxInterstitialAd;
        public AppLovinInterstitialAdDialog appLovinInterstitialAdDialog;
        public AppLovinAd appLovinAd;
        public com.wortise.ads.interstitial.InterstitialAd wortiseInterstitialAd;
        private int retryAttempt;
        private int counter = 1;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobInterstitialId = "";
        private String googleAdManagerInterstitialId = "";
        private String fanInterstitialId = "";
        private String unityInterstitialId = "";
        private String appLovinInterstitialId = "";
        private String appLovinInterstitialZoneId = "";
        private String mopubInterstitialId = "";
        private String ironSourceInterstitialId = "";
        private String wortiseInterstitialId = "";
        private int placementStatus = 1;
        private int interval = 3;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadInterstitialAd();
            return this;
        }

        public Builder build(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            loadInterstitialAd(onInterstitialAdDismissedListener);
            return this;
        }

        public void show() {
            showInterstitialAd();
        }

        public void show(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            showInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobInterstitialId(String adMobInterstitialId) {
            this.adMobInterstitialId = adMobInterstitialId;
            return this;
        }

        public Builder setGoogleAdManagerInterstitialId(String googleAdManagerInterstitialId) {
            this.googleAdManagerInterstitialId = googleAdManagerInterstitialId;
            return this;
        }

        public Builder setFanInterstitialId(String fanInterstitialId) {
            this.fanInterstitialId = fanInterstitialId;
            return this;
        }

        public Builder setUnityInterstitialId(String unityInterstitialId) {
            this.unityInterstitialId = unityInterstitialId;
            return this;
        }

        public Builder setAppLovinInterstitialId(String appLovinInterstitialId) {
            this.appLovinInterstitialId = appLovinInterstitialId;
            return this;
        }

        public Builder setAppLovinInterstitialZoneId(String appLovinInterstitialZoneId) {
            this.appLovinInterstitialZoneId = appLovinInterstitialZoneId;
            return this;
        }

        public Builder setMopubInterstitialId(String mopubInterstitialId) {
            this.mopubInterstitialId = mopubInterstitialId;
            return this;
        }

        public Builder setIronSourceInterstitialId(String ironSourceInterstitialId) {
            this.ironSourceInterstitialId = ironSourceInterstitialId;
            return this;
        }

        public Builder setWortiseInterstitialId(String wortiseInterstitialId) {
            this.wortiseInterstitialId = wortiseInterstitialId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                loadBackupInterstitialAd();
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        AdManagerInterstitialAd.load(activity, googleAdManagerInterstitialId, Tools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                                super.onAdLoaded(adManagerInterstitialAd);
                                adManagerInterstitialAd = interstitialAd;
                                adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        adManagerInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                adManagerInterstitialAd = null;
                                loadBackupInterstitialAd();
                                Log.d(TAG, "Failed load Ad Manager Interstitial Ad");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                                loadBackupInterstitialAd();
                            }
                        });
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                                loadBackupInterstitialAd();
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                loadBackupInterstitialAd();
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadBackupInterstitialAd();
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case WORTISE:
                        wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, wortiseInterstitialId);
                        wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                loadInterstitialAd();
                                Log.d(TAG, "[Wortise] Interstitial Ad dismissed");
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                loadBackupInterstitialAd();
                                Log.d(TAG, "[Wortise] Failed to load Interstitial Ad");
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                Log.d(TAG, "[Wortise] Interstitial Ad loaded");
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }
                        });
                        wortiseInterstitialAd.loadAd();
                        break;
                }
            }
        }

        public void loadBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        AdManagerInterstitialAd.load(activity, googleAdManagerInterstitialId, Tools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                                super.onAdLoaded(adManagerInterstitialAd);
                                adManagerInterstitialAd = interstitialAd;
                                adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        adManagerInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                adManagerInterstitialAd = null;
                                Log.d(TAG, "Failed load Ad Manager Interstitial Ad");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case WORTISE:
                        wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, wortiseInterstitialId);
                        wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                loadInterstitialAd();
                                Log.d(TAG, "[Wortise] [Backup] Interstitial Ad dismissed");
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                Log.d(TAG, "[Wortise] [Backup] Failed to load Interstitial Ad");
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                Log.d(TAG, "[Wortise] [Backup] Interstitial Ad loaded");
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }
                        });
                        wortiseInterstitialAd.loadAd();
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                if (counter == interval) {
                    switch (adNetwork) {
                        case ADMOB:
                        case FAN_BIDDING_ADMOB:
                            if (adMobInterstitialAd != null) {
                                adMobInterstitialAd.show(activity);
                                Log.d(TAG, "admob interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "admob interstitial null");
                            }
                            break;

                        case GOOGLE_AD_MANAGER:
                        case FAN_BIDDING_AD_MANAGER:
                            if (adManagerInterstitialAd != null) {
                                adManagerInterstitialAd.show(activity);
                                Log.d(TAG, "ad manager interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "ad manager interstitial null");
                            }
                            break;

                        case FAN:
                        case FACEBOOK:
                            if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                                fanInterstitialAd.show();
                                Log.d(TAG, "fan interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "fan interstitial null");
                            }
                            break;

                        case STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd();
                                Log.d(TAG, "startapp interstitial not null [counter] : " + counter);
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "startapp interstitial null");
                            }
                            break;

                        case UNITY:
                            final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                                @Override
                                public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                    Log.d(TAG, "unity ads show failure");
                                    showBackupInterstitialAd();
                                }
                            };
                            unityInterstitialAd.show(showListener);
                            break;

                        case APPLOVIN:
                        case APPLOVIN_MAX:
                        case FAN_BIDDING_APPLOVIN_MAX:
                            if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                                Log.d(TAG, "ready : " + counter);
                                maxInterstitialAd.showAd();
                                Log.d(TAG, "show ad");
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;

                        case APPLOVIN_DISCOVERY:
                            if (appLovinInterstitialAdDialog != null) {
                                appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                            }
                            break;

                        case MOPUB:
                            //Mopub has been acquired by AppLovin
                            break;

                        case IRONSOURCE:
                        case FAN_BIDDING_IRONSOURCE:
                            if (IronSource.isInterstitialReady()) {
                                IronSource.showInterstitial(ironSourceInterstitialId);
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;

                        case WORTISE:
                            if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                                wortiseInterstitialAd.showAd();
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;
                    }
                    counter = 1;
                } else {
                    counter++;
                }
                Log.d(TAG, "Current counter : " + counter);
            }
        }

        public void showBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobInterstitialAd != null) {
                            adMobInterstitialAd.show(activity);
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerInterstitialAd != null) {
                            adManagerInterstitialAd.show(activity);
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                        }
                        break;

                    case UNITY:
                        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                            @Override
                            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                Log.d(TAG, "unity ads show failure");
                            }
                        };
                        unityInterstitialAd.show(showListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            maxInterstitialAd.showAd();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(ironSourceInterstitialId);
                        }
                        break;

                    case WORTISE:
                        if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                            wortiseInterstitialAd.showAd();
                        }
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void loadInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd(onInterstitialAdDismissedListener);
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        AdManagerInterstitialAd.load(activity, googleAdManagerInterstitialId, Tools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                                super.onAdLoaded(adManagerInterstitialAd);
                                adManagerInterstitialAd = interstitialAd;
                                adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        loadInterstitialAd(onInterstitialAdDismissedListener);
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        adManagerInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                adManagerInterstitialAd = null;
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                Log.d(TAG, "Failed load Ad Manager Interstitial Ad");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }
                        });
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd(onInterstitialAdDismissedListener);
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case WORTISE:
                        wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, wortiseInterstitialId);
                        wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                loadInterstitialAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                Log.d(TAG, "[Wortise] Interstitial Ad dismissed");
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                Log.d(TAG, "[Wortise] Failed to load Interstitial Ad");
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                Log.d(TAG, "[Wortise] Interstitial Ad loaded");
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }
                        });
                        wortiseInterstitialAd.loadAd();
                        break;
                }
            }
        }

        public void loadBackupInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd(onInterstitialAdDismissedListener);
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        AdManagerInterstitialAd.load(activity, googleAdManagerInterstitialId, Tools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                                super.onAdLoaded(adManagerInterstitialAd);
                                adManagerInterstitialAd = interstitialAd;
                                adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        loadInterstitialAd(onInterstitialAdDismissedListener);
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        adManagerInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                adManagerInterstitialAd = null;
                                Log.d(TAG, "Failed load Ad Manager Interstitial Ad");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd(onInterstitialAdDismissedListener);
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case WORTISE:
                        wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, wortiseInterstitialId);
                        wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                loadInterstitialAd(onInterstitialAdDismissedListener);
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                Log.d(TAG, "[Wortise] [Backup] Interstitial Ad dismissed");
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                Log.d(TAG, "[Wortise] [Backup] Failed to load Interstitial Ad");
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                Log.d(TAG, "[Wortise] [Backup] Interstitial Ad loaded");
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }
                        });
                        wortiseInterstitialAd.loadAd();
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                if (counter == interval) {
                    switch (adNetwork) {
                        case ADMOB:
                        case FAN_BIDDING_ADMOB:
                            if (adMobInterstitialAd != null) {
                                adMobInterstitialAd.show(activity);
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "admob interstitial not null");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "admob interstitial null");
                            }
                            break;

                        case GOOGLE_AD_MANAGER:
                        case FAN_BIDDING_AD_MANAGER:
                            if (adManagerInterstitialAd != null) {
                                adManagerInterstitialAd.show(activity);
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "ad manager interstitial not null");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "ad manager interstitial null");
                            }
                            break;

                        case FAN:
                        case FACEBOOK:
                            if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                                fanInterstitialAd.show();
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "fan interstitial not null");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "fan interstitial null");
                            }
                            break;

                        case STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd(new AdDisplayListener() {
                                    @Override
                                    public void adHidden(Ad ad) {
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void adDisplayed(Ad ad) {
                                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                                    }

                                    @Override
                                    public void adClicked(Ad ad) {

                                    }

                                    @Override
                                    public void adNotDisplayed(Ad ad) {

                                    }
                                });
                                Log.d(TAG, "startapp interstitial not null [counter] : " + counter);
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "startapp interstitial null");
                            }
                            break;

                        case UNITY:
                            final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                                @Override
                                public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                    Log.d(TAG, "unity ads show failure");
                                    showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                }
                            };
                            unityInterstitialAd.show(showListener);
                            break;

                        case APPLOVIN:
                        case APPLOVIN_MAX:
                        case FAN_BIDDING_APPLOVIN_MAX:
                            if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                                Log.d(TAG, "ready : " + counter);
                                maxInterstitialAd.showAd();
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "show ad");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;

                        case APPLOVIN_DISCOVERY:
                            if (appLovinInterstitialAdDialog != null) {
                                appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                                    @Override
                                    public void adDisplayed(AppLovinAd appLovinAd) {
                                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                                    }

                                    @Override
                                    public void adHidden(AppLovinAd appLovinAd) {
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }
                                });
                                appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                            }
                            break;

                        case MOPUB:
                            //Mopub has been acquired by AppLovin
                            break;

                        case IRONSOURCE:
                        case FAN_BIDDING_IRONSOURCE:
                            if (IronSource.isInterstitialReady()) {
                                IronSource.showInterstitial(ironSourceInterstitialId);
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;

                        case WORTISE:
                            if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                                wortiseInterstitialAd.showAd();
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;
                    }
                    counter = 1;
                } else {
                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                    counter++;
                }
                Log.d(TAG, "Current counter : " + counter);
            } else {
                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
            }
        }

        public void showBackupInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobInterstitialAd != null) {
                            adMobInterstitialAd.show(activity);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerInterstitialAd != null) {
                            adManagerInterstitialAd.show(activity);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd(new AdDisplayListener() {
                                @Override
                                public void adHidden(Ad ad) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void adDisplayed(Ad ad) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adClicked(Ad ad) {

                                }

                                @Override
                                public void adNotDisplayed(Ad ad) {

                                }
                            });
                        }
                        break;

                    case UNITY:
                        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                            @Override
                            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            }

                            @Override
                            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                Log.d(TAG, "unity ads show failure");
                            }
                        };
                        unityInterstitialAd.show(showListener);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            maxInterstitialAd.showAd();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                                @Override
                                public void adDisplayed(AppLovinAd appLovinAd) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adHidden(AppLovinAd appLovinAd) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }
                            });
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(ironSourceInterstitialId);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case WORTISE:
                        if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                            wortiseInterstitialAd.showAd();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            } else {
                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
            }
        }

    }

}
