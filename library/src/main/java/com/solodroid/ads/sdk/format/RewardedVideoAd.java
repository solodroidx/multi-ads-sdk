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
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;
import static com.solodroid.ads.sdk.util.Constant.UNITY;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoManualListener;
import com.solodroid.ads.sdk.util.OnRewardedAdCompleteListener;
import com.solodroid.ads.sdk.util.OnRewardedAdErrorListener;
import com.solodroid.ads.sdk.util.OnRewardedAdLoadedListener;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.wortise.ads.rewarded.models.Reward;

import java.util.Map;

public class RewardedVideoAd {

    public static class Builder {

        private static final String TAG = "SoloRewarded";
        private final Activity activity;
        private RewardedAd adMobRewardedAd;
        private RewardedAd adManagerRewardedAd;
        private com.facebook.ads.RewardedVideoAd fanRewardedVideoAd;
        private StartAppAd startAppAd;
        private MaxRewardedAd applovinMaxRewardedAd;
        private AppLovinIncentivizedInterstitial incentivizedInterstitial;
        private com.wortise.ads.rewarded.RewardedAd wortiseRewardedAd;
        private String adStatus = "";
        private String mainAds = "";
        private String backupAds = "";
        private String adMobRewardedId = "";
        private String adManagerRewardedId = "";
        private String fanRewardedId = "";
        private String unityRewardedId = "";
        private String applovinMaxRewardedId = "";
        private String applovinDiscRewardedZoneId = "";
        private String ironSourceRewardedId = "";
        private String wortiseRewardedId = "";
        private int placementStatus = 1;
        private boolean legacyGDPR = false;
        private boolean showRewardedAdIfLoaded = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
            loadRewardedAd(onLoaded, onComplete, onError);
            return this;
        }

        public Builder show(OnRewardedAdCompleteListener onRewardedAdCompleteListener, OnRewardedAdErrorListener onRewardedAdErrorListener) {
            showRewardedAd(onRewardedAdCompleteListener, onRewardedAdErrorListener);
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setMainAds(String mainAds) {
            this.mainAds = mainAds;
            return this;
        }

        public Builder setBackupAds(String backupAds) {
            this.backupAds = backupAds;
            return this;
        }

        public Builder setAdMobRewardedId(String adMobRewardedId) {
            this.adMobRewardedId = adMobRewardedId;
            return this;
        }

        public Builder setAdManagerRewardedId(String adManagerRewardedId) {
            this.adManagerRewardedId = adManagerRewardedId;
            return this;
        }

        public Builder setFanRewardedId(String fanRewardedId) {
            this.fanRewardedId = fanRewardedId;
            return this;
        }

        public Builder setUnityRewardedId(String unityRewardedId) {
            this.unityRewardedId = unityRewardedId;
            return this;
        }

        public Builder setApplovinMaxRewardedId(String applovinMaxRewardedId) {
            this.applovinMaxRewardedId = applovinMaxRewardedId;
            return this;
        }

        public Builder setApplovinDiscRewardedZoneId(String applovinDiscRewardedZoneId) {
            this.applovinDiscRewardedZoneId = applovinDiscRewardedZoneId;
            return this;
        }

        public Builder setIronSourceRewardedId(String ironSourceRewardedId) {
            this.ironSourceRewardedId = ironSourceRewardedId;
            return this;
        }

        public Builder setWortiseRewardedId(String wortiseRewardedId) {
            this.wortiseRewardedId = wortiseRewardedId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public Builder showRewardedAdIfLoaded(boolean showRewardedAdIfLoaded) {
            this.showRewardedAdIfLoaded = showRewardedAdIfLoaded;
            return this;
        }

        public void loadRewardedAd(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adMobRewardedAd = null;
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                adMobRewardedAd = ad;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adMobRewardedAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adMobRewardedAd = null;
                                    }
                                });
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        RewardedAd.load(activity, adManagerRewardedId, Tools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adManagerRewardedAd = null;
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                adManagerRewardedAd = ad;
                                adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adManagerRewardedAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adManagerRewardedAd = null;
                                    }
                                });
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, fanRewardedId);
                        fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                                .withAdListener(new RewardedVideoAdListener() {
                                    @Override
                                    public void onRewardedVideoCompleted() {
                                        onComplete.onRewardedAdComplete();
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                                    }

                                    @Override
                                    public void onRewardedVideoClosed() {
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad closed");
                                    }

                                    @Override
                                    public void onError(Ad ad, AdError adError) {
                                        loadRewardedBackupAd(onLoaded, onComplete, onError);
                                        Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + fanRewardedId + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(Ad ad) {
                                        if (showRewardedAdIfLoaded) {
                                            showRewardedAd(onComplete, onError);
                                        } else {
                                            onLoaded.onRewardedAdLoaded();
                                        }
                                        Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                                    }

                                    @Override
                                    public void onAdClicked(Ad ad) {

                                    }

                                    @Override
                                    public void onLoggingImpression(Ad ad) {

                                    }
                                })
                                .build());
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.setVideoListener(() -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                        });
                        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad, try to load backup ad: " + backupAds);

                            }
                        });
                        break;

                    case UNITY:
                        UnityAds.load(unityRewardedId, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Log.e(TAG, "[" + mainAds + "] " + "rewarded ad failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                            }
                        });
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        applovinMaxRewardedAd = MaxRewardedAd.getInstance(applovinMaxRewardedId, activity);
                        applovinMaxRewardedAd.setListener(new MaxRewardedAdListener() {
                            @Override
                            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onRewardedVideoStarted(MaxAd maxAd) {

                            }

                            @Override
                            public void onRewardedVideoCompleted(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdLoaded(MaxAd maxAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdHidden(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdClicked(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdLoadFailed(String s, MaxError maxError) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        applovinMaxRewardedAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        incentivizedInterstitial = AppLovinIncentivizedInterstitial.create(applovinDiscRewardedZoneId, AppLovinSdk.getInstance(activity));
                        incentivizedInterstitial.preload(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd appLovinAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                            @Override
                            public void onAdAvailable(AdInfo adInfo) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad is ready");
                            }

                            @Override
                            public void onAdUnavailable() {

                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdClicked(Placement placement, AdInfo adInfo) {

                            }

                            @Override
                            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {

                            }
                        });
                        if (showRewardedAdIfLoaded) {
                            if (IronSource.isRewardedVideoAvailable()) {
                                IronSource.showRewardedVideo(ironSourceRewardedId);
                            }
                        }
                        break;

                    case WORTISE:
                        wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, wortiseRewardedId);
                        wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                            @Override
                            public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                            }

                            @Override
                            public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull Reward reward) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad dismissed");
                            }

                            @Override
                            public void onRewardedFailed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
                                loadRewardedBackupAd(onLoaded, onComplete, onError);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + adError + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                            }
                        });
                        wortiseRewardedAd.loadAd();
                        break;
                }
            }
        }

        public void loadRewardedBackupAd(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adMobRewardedAd = null;
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                adMobRewardedAd = ad;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adMobRewardedAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adMobRewardedAd = null;
                                    }
                                });
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        RewardedAd.load(activity, adManagerRewardedId, Tools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adManagerRewardedAd = null;
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                adManagerRewardedAd = ad;
                                adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adManagerRewardedAd = null;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adManagerRewardedAd = null;
                                    }
                                });
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, fanRewardedId);
                        fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                                .withAdListener(new RewardedVideoAdListener() {
                                    @Override
                                    public void onRewardedVideoCompleted() {
                                        onComplete.onRewardedAdComplete();
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                                    }

                                    @Override
                                    public void onRewardedVideoClosed() {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad closed");
                                    }

                                    @Override
                                    public void onError(Ad ad, AdError adError) {
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + adError.getErrorMessage() + ", try to load backup ad: " + backupAds);
                                    }

                                    @Override
                                    public void onAdLoaded(Ad ad) {
                                        if (showRewardedAdIfLoaded) {
                                            showRewardedBackupAd(onComplete, onError);
                                        } else {
                                            onLoaded.onRewardedAdLoaded();
                                        }
                                        Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                                    }

                                    @Override
                                    public void onAdClicked(Ad ad) {

                                    }

                                    @Override
                                    public void onLoggingImpression(Ad ad) {

                                    }
                                })
                                .build());
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.setVideoListener(() -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                        });
                        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad, try to load backup ad: " + backupAds);

                            }
                        });
                        break;

                    case UNITY:
                        UnityAds.load(unityRewardedId, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Log.e(TAG, "[" + backupAds + "] [backup] " + "rewarded ad failed to load ad for " + placementId + " with error: [" + error + "] " + message);

                            }
                        });
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        applovinMaxRewardedAd = MaxRewardedAd.getInstance(applovinMaxRewardedId, activity);
                        applovinMaxRewardedAd.setListener(new MaxRewardedAdListener() {
                            @Override
                            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onRewardedVideoStarted(MaxAd maxAd) {

                            }

                            @Override
                            public void onRewardedVideoCompleted(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdLoaded(MaxAd maxAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdHidden(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdClicked(MaxAd maxAd) {

                            }

                            @Override
                            public void onAdLoadFailed(String s, MaxError maxError) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        applovinMaxRewardedAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        incentivizedInterstitial = AppLovinIncentivizedInterstitial.create(applovinDiscRewardedZoneId, AppLovinSdk.getInstance(activity));
                        incentivizedInterstitial.preload(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd appLovinAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedBackupAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                            @Override
                            public void onAdAvailable(AdInfo adInfo) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad is ready");
                            }

                            @Override
                            public void onAdUnavailable() {

                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onAdClicked(Placement placement, AdInfo adInfo) {

                            }

                            @Override
                            public void onAdRewarded(Placement placement, AdInfo adInfo) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {

                            }
                        });
                        if (showRewardedAdIfLoaded) {
                            if (IronSource.isRewardedVideoAvailable()) {
                                IronSource.showRewardedVideo(ironSourceRewardedId);
                            }
                        }
                        break;

                    case WORTISE:
                        wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, wortiseRewardedId);
                        wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                            @Override
                            public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                            }

                            @Override
                            public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull Reward reward) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad complete");
                            }

                            @Override
                            public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad dismissed");
                            }

                            @Override
                            public void onRewardedFailed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + adError + ", try to load backup ad: " + backupAds);
                            }

                            @Override
                            public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                                if (showRewardedAdIfLoaded) {
                                    showRewardedAd(onComplete, onError);
                                } else {
                                    onLoaded.onRewardedAdLoaded();
                                }
                                Log.d(TAG, "[" + backupAds + "] [backup]" + "rewarded ad loaded");
                            }

                            @Override
                            public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                            }
                        });
                        wortiseRewardedAd.loadAd();
                        break;
                }
            }
        }

        public void showRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobRewardedAd != null) {
                            adMobRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerRewardedAd != null) {
                            adManagerRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                            fanRewardedVideoAd.show();
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
//                            startAppAd.showAd(new AdDisplayListener() {
//                                @Override
//                                public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
//                                    Log.d(TAG, "[" + mainAds + "] " + "rewarded ad dismissed");
//                                }
//
//                                @Override
//                                public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {
//
//                                }
//
//                                @Override
//                                public void adClicked(com.startapp.sdk.adsbase.Ad ad) {
//
//                                }
//
//                                @Override
//                                public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {
//                                    loadRewardedBackupAd(onLoaded, onComplete, onError);
//                                }
//                            });
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case UNITY:
                        UnityAds.show(activity, unityRewardedId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                showRewardedBackupAd(onComplete, onError);
                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                onComplete.onRewardedAdComplete();
                            }
                        });
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (applovinMaxRewardedAd != null && applovinMaxRewardedAd.isReady()) {
                            applovinMaxRewardedAd.showAd();
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (incentivizedInterstitial != null) {
                            incentivizedInterstitial.show(activity, new AppLovinAdRewardListener() {
                                @Override
                                public void userRewardVerified(AppLovinAd ad, Map<String, String> response) {
                                    onComplete.onRewardedAdComplete();
                                    Log.d(TAG, "[" + mainAds + "] " + "rewarded ad complete");
                                }

                                @Override
                                public void userOverQuota(AppLovinAd ad, Map<String, String> response) {

                                }

                                @Override
                                public void userRewardRejected(AppLovinAd ad, Map<String, String> response) {

                                }

                                @Override
                                public void validationRequestFailed(AppLovinAd ad, int errorCode) {

                                }


                            }, null, new AppLovinAdDisplayListener() {
                                @Override
                                public void adDisplayed(AppLovinAd appLovinAd) {
                                    Log.d(TAG, "[" + mainAds + "] " + "rewarded ad displayed");
                                }

                                @Override
                                public void adHidden(AppLovinAd appLovinAd) {
                                    Log.d(TAG, "[" + mainAds + "] " + "rewarded ad dismissed");
                                }
                            });
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(ironSourceRewardedId);
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    case WORTISE:
                        if (wortiseRewardedAd != null && wortiseRewardedAd.isAvailable()) {
                            wortiseRewardedAd.showAd();
                        } else {
                            showRewardedBackupAd(onComplete, onError);
                        }
                        break;

                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void showRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobRewardedAd != null) {
                            adMobRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerRewardedAd != null) {
                            adManagerRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                            fanRewardedVideoAd.show();
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case UNITY:
                        UnityAds.show(activity, unityRewardedId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                onError.onRewardedAdError();
                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                onComplete.onRewardedAdComplete();
                            }
                        });
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (applovinMaxRewardedAd != null && applovinMaxRewardedAd.isReady()) {
                            applovinMaxRewardedAd.showAd();
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (incentivizedInterstitial != null) {
                            incentivizedInterstitial.show(activity, new AppLovinAdRewardListener() {
                                @Override
                                public void userRewardVerified(AppLovinAd ad, Map<String, String> response) {
                                    onComplete.onRewardedAdComplete();
                                }

                                @Override
                                public void userOverQuota(AppLovinAd ad, Map<String, String> response) {

                                }

                                @Override
                                public void userRewardRejected(AppLovinAd ad, Map<String, String> response) {

                                }

                                @Override
                                public void validationRequestFailed(AppLovinAd ad, int errorCode) {

                                }


                            }, new AppLovinAdVideoPlaybackListener() {
                                @Override
                                public void videoPlaybackBegan(AppLovinAd appLovinAd) {

                                }

                                @Override
                                public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {

                                }
                            }, new AppLovinAdDisplayListener() {
                                @Override
                                public void adDisplayed(AppLovinAd appLovinAd) {

                                }

                                @Override
                                public void adHidden(AppLovinAd appLovinAd) {
                                    Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad dismissed");
                                }
                            });
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(ironSourceRewardedId);
                        }
                        break;

                    case WORTISE:
                        if (wortiseRewardedAd != null && wortiseRewardedAd.isAvailable()) {
                            wortiseRewardedAd.showAd();
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void destroyRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case FAN:
                    case FACEBOOK:
                        if (fanRewardedVideoAd != null) {
                            fanRewardedVideoAd.destroy();
                            fanRewardedVideoAd = null;
                        }
                        break;
                }

                switch (backupAds) {
                    case FAN:
                    case FACEBOOK:
                        if (fanRewardedVideoAd != null) {
                            fanRewardedVideoAd.destroy();
                            fanRewardedVideoAd = null;
                        }
                        break;
                }
            }
        }

    }

}
