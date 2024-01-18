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
import static com.solodroid.ads.sdk.util.Constant.UNITY_ADS_BANNER_HEIGHT_MEDIUM;
import static com.solodroid.ads.sdk.util.Constant.UNITY_ADS_BANNER_WIDTH_MEDIUM;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.helper.AppLovinCustomEventBanner;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.wortise.ads.AdError;

public class BannerAd {

    @SuppressWarnings("deprecation")
    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private AdView adView;
        private AdManagerAdView adManagerAdView;
        private com.facebook.ads.AdView fanAdView;
        private AppLovinAdView appLovinAdView;
        FrameLayout ironSourceBannerView;
        private IronSourceBannerLayout ironSourceBannerLayout;
        private com.wortise.ads.banner.BannerAd wortiseBannerAd;
        FrameLayout wortiseBannerView;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobBannerId = "";
        private String googleAdManagerBannerId = "";
        private String fanBannerId = "";
        private String unityBannerId = "";
        private String appLovinBannerId = "";
        private String appLovinBannerZoneId = "";
        private String mopubBannerId = "";
        private String ironSourceBannerId = "";
        private String wortiseBannerId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadBannerAd();
            return this;
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

        public Builder setAdMobBannerId(String adMobBannerId) {
            this.adMobBannerId = adMobBannerId;
            return this;
        }

        public Builder setGoogleAdManagerBannerId(String googleAdManagerBannerId) {
            this.googleAdManagerBannerId = googleAdManagerBannerId;
            return this;
        }

        public Builder setFanBannerId(String fanBannerId) {
            this.fanBannerId = fanBannerId;
            return this;
        }

        public Builder setUnityBannerId(String unityBannerId) {
            this.unityBannerId = unityBannerId;
            return this;
        }

        public Builder setAppLovinBannerId(String appLovinBannerId) {
            this.appLovinBannerId = appLovinBannerId;
            return this;
        }

        public Builder setAppLovinBannerZoneId(String appLovinBannerZoneId) {
            this.appLovinBannerZoneId = appLovinBannerZoneId;
            return this;
        }

        public Builder setMopubBannerId(String mopubBannerId) {
            this.mopubBannerId = mopubBannerId;
            return this;
        }

        public Builder setIronSourceBannerId(String ironSourceBannerId) {
            this.ironSourceBannerId = ironSourceBannerId;
            return this;
        }

        public Builder setWortiseBannerId(String wortiseBannerId) {
            this.wortiseBannerId = wortiseBannerId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        FrameLayout adContainerView = activity.findViewById(R.id.admob_banner_view_container);
                        adContainerView.post(() -> {
                            adView = new AdView(activity);
                            adView.setAdUnitId(adMobBannerId);
                            adContainerView.removeAllViews();
                            adContainerView.addView(adView);
                            adView.setAdSize(Tools.getAdSize(activity));
                            adView.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                            adView.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    adContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    adContainerView.setVisibility(View.GONE);
                                    loadBackupBannerAd();
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when an ad opens an overlay that
                                    // covers the screen.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the user is about to return
                                    // to the app after tapping on an ad.
                                }
                            });
                        });
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + adMobBannerId);
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        FrameLayout googleAdContainerView = activity.findViewById(R.id.google_ad_banner_view_container);
                        googleAdContainerView.post(() -> {
                            adManagerAdView = new AdManagerAdView(activity);
                            adManagerAdView.setAdUnitId(googleAdManagerBannerId);
                            googleAdContainerView.removeAllViews();
                            googleAdContainerView.addView(adManagerAdView);
                            adManagerAdView.setAdSize(Tools.getAdSize(activity));
                            adManagerAdView.loadAd(Tools.getGoogleAdManagerRequest());
                            adManagerAdView.setAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    googleAdContainerView.setVisibility(View.GONE);
                                    loadBackupBannerAd();
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                    googleAdContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdOpened() {
                                    super.onAdOpened();
                                }
                            });
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanAdView = new com.facebook.ads.AdView(activity, fanBannerId, AdSize.BANNER_HEIGHT_50);
                        RelativeLayout fanAdViewContainer = activity.findViewById(R.id.fan_banner_view_container);
                        fanAdViewContainer.addView(fanAdView);
                        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                            @Override
                            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                                fanAdViewContainer.setVisibility(View.GONE);
                                loadBackupBannerAd();
                                Log.d(TAG, "Error load FAN : " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                fanAdViewContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        };
                        com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = fanAdView.buildLoadAdConfig().withAdListener(adListener).build();
                        fanAdView.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        RelativeLayout startAppAdView = activity.findViewById(R.id.startapp_banner_view_container);
                        Banner banner = new Banner(activity, new BannerListener() {
                            @Override
                            public void onReceiveAd(View banner) {
                                startAppAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailedToReceiveAd(View banner) {
                                startAppAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                                Log.d(TAG, adNetwork + " failed load startapp banner ad : ");
                            }

                            @Override
                            public void onImpression(View view) {

                            }

                            @Override
                            public void onClick(View banner) {
                            }
                        });
                        startAppAdView.addView(banner);
                        break;

                    case UNITY:
                        RelativeLayout unityAdView = activity.findViewById(R.id.unity_banner_view_container);
                        BannerView bottomBanner = new BannerView(activity, unityBannerId, new UnityBannerSize(UNITY_ADS_BANNER_WIDTH_MEDIUM, UNITY_ADS_BANNER_HEIGHT_MEDIUM));
                        bottomBanner.setListener(new BannerView.IListener() {
                            @Override
                            public void onBannerLoaded(BannerView bannerView) {
                                unityAdView.setVisibility(View.VISIBLE);
                                Log.d("Unity_banner", "ready");
                            }

                            @Override
                            public void onBannerShown(BannerView bannerAdView) {

                            }

                            @Override
                            public void onBannerClick(BannerView bannerView) {

                            }

                            @Override
                            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                                Log.d("SupportTest", "Banner Error" + bannerErrorInfo);
                                unityAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }

                            @Override
                            public void onBannerLeftApplication(BannerView bannerView) {

                            }
                        });
                        unityAdView.addView(bottomBanner);
                        bottomBanner.load();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + unityBannerId);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        RelativeLayout appLovinAdView = activity.findViewById(R.id.applovin_banner_view_container);
                        MaxAdView maxAdView = new MaxAdView(appLovinBannerId, activity);
                        maxAdView.setListener(new MaxAdViewAdListener() {
                            @Override
                            public void onAdExpanded(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdCollapsed(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdLoaded(@NonNull MaxAd ad) {
                                appLovinAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdDisplayed(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdClicked(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                                appLovinAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }

                            @Override
                            public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {

                            }
                        });

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                        if (darkTheme) {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_native_background_dark));
                        } else {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_native_background_light));
                        }
                        appLovinAdView.addView(maxAdView);
                        maxAdView.loadAd();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + appLovinBannerId);
                        break;

                    case APPLOVIN_DISCOVERY:
                        RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.applovin_discovery_banner_view_container);
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", appLovinBannerZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        this.appLovinAdView = new AppLovinAdView(adSize, activity);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }
                        });
                        appLovinDiscoveryAdView.addView(this.appLovinAdView);
                        this.appLovinAdView.loadNextAd();
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        ironSourceBannerView = activity.findViewById(R.id.ironsource_banner_view_container);
                        ISBannerSize size = ISBannerSize.BANNER;
                        ironSourceBannerLayout = IronSource.createBanner(activity, size);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        ironSourceBannerView.addView(ironSourceBannerLayout, 0, layoutParams);
                        if (ironSourceBannerLayout != null) {
                            ironSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                                @Override
                                public void onAdLoaded(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdLoaded");
                                    ironSourceBannerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdLoadFailed(IronSourceError ironSourceError) {
                                    Log.d(TAG, "onBannerAdLoadFailed" + " " + ironSourceError.getErrorMessage());
                                    loadBackupBannerAd();
                                }

                                @Override
                                public void onAdClicked(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdClicked");
                                }

                                @Override
                                public void onAdLeftApplication(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdLeftApplication");
                                }

                                @Override
                                public void onAdScreenPresented(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdScreenPresented");
                                }

                                @Override
                                public void onAdScreenDismissed(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdScreenDismissed");
                                }
                            });
                            IronSource.loadBanner(ironSourceBannerLayout, ironSourceBannerId);
                        } else {
                            Log.d(TAG, "IronSource.createBanner returned null");
                        }
                        break;

                    case WORTISE:
                        wortiseBannerAd = new com.wortise.ads.banner.BannerAd(activity);
                        wortiseBannerAd.setAdSize(Tools.getWortiseAdSize(activity));
                        wortiseBannerAd.setAdUnitId(wortiseBannerId);
                        wortiseBannerView = activity.findViewById(R.id.wortise_banner_view_container);
                        wortiseBannerView.addView(wortiseBannerAd);
                        wortiseBannerAd.loadAd();
                        wortiseBannerAd.setListener(new com.wortise.ads.banner.BannerAd.Listener() {
                            @Override
                            public void onBannerClicked(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                // Existing code for banner click handling
                            }

                            @Override
                            public void onBannerFailedToLoad(@NonNull com.wortise.ads.banner.BannerAd bannerAd, @NonNull AdError adError) {
                                wortiseBannerView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                                Log.d(TAG, "failed to load Wortise banner: " + adError);
                            }

                            @Override
                            public void onBannerImpression(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                // Add code here to handle banner impression events
                            }

                            @Override
                            public void onBannerLoaded(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                wortiseBannerView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "Wortise banner loaded");
                            }
                        });
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

        public void loadBackupBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        FrameLayout adContainerView = activity.findViewById(R.id.admob_banner_view_container);
                        adContainerView.post(() -> {
                            adView = new AdView(activity);
                            adView.setAdUnitId(adMobBannerId);
                            adContainerView.removeAllViews();
                            adContainerView.addView(adView);
                            adView.setAdSize(Tools.getAdSize(activity));
                            adView.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                            adView.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    adContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    adContainerView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when an ad opens an overlay that
                                    // covers the screen.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the user is about to return
                                    // to the app after tapping on an ad.
                                }
                            });
                        });
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + adMobBannerId);
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        FrameLayout googleAdContainerView = activity.findViewById(R.id.google_ad_banner_view_container);
                        googleAdContainerView.post(() -> {
                            adManagerAdView = new AdManagerAdView(activity);
                            adManagerAdView.setAdUnitId(googleAdManagerBannerId);
                            googleAdContainerView.removeAllViews();
                            googleAdContainerView.addView(adManagerAdView);
                            adManagerAdView.setAdSize(Tools.getAdSize(activity));
                            adManagerAdView.loadAd(Tools.getGoogleAdManagerRequest());
                            adManagerAdView.setAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    googleAdContainerView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                    googleAdContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdOpened() {
                                    super.onAdOpened();
                                }
                            });
                        });
                        break;

                    case FAN:
                    case FACEBOOK:
                        fanAdView = new com.facebook.ads.AdView(activity, fanBannerId, AdSize.BANNER_HEIGHT_50);
                        RelativeLayout fanAdViewContainer = activity.findViewById(R.id.fan_banner_view_container);
                        fanAdViewContainer.addView(fanAdView);
                        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                            @Override
                            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                                fanAdViewContainer.setVisibility(View.GONE);
                                Log.d(TAG, "Error load FAN : " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                fanAdViewContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }
                        };
                        com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = fanAdView.buildLoadAdConfig().withAdListener(adListener).build();
                        fanAdView.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        RelativeLayout startAppAdView = activity.findViewById(R.id.startapp_banner_view_container);
                        Banner banner = new Banner(activity, new BannerListener() {
                            @Override
                            public void onReceiveAd(View banner) {
                                startAppAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailedToReceiveAd(View banner) {
                                startAppAdView.setVisibility(View.GONE);
                                Log.d(TAG, adNetwork + " failed load startapp banner ad : ");
                            }

                            @Override
                            public void onImpression(View view) {

                            }

                            @Override
                            public void onClick(View banner) {
                            }
                        });
                        startAppAdView.addView(banner);
                        break;

                    case UNITY:
                        RelativeLayout unityAdView = activity.findViewById(R.id.unity_banner_view_container);
                        BannerView bottomBanner = new BannerView(activity, unityBannerId, new UnityBannerSize(UNITY_ADS_BANNER_WIDTH_MEDIUM, UNITY_ADS_BANNER_HEIGHT_MEDIUM));
                        bottomBanner.setListener(new BannerView.IListener() {
                            @Override
                            public void onBannerLoaded(BannerView bannerView) {
                                unityAdView.setVisibility(View.VISIBLE);
                                Log.d("Unity_banner", "ready");
                            }

                            @Override
                            public void onBannerShown(BannerView bannerAdView) {

                            }

                            @Override
                            public void onBannerClick(BannerView bannerView) {

                            }

                            @Override
                            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                                Log.d("SupportTest", "Banner Error" + bannerErrorInfo);
                                unityAdView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onBannerLeftApplication(BannerView bannerView) {

                            }
                        });
                        unityAdView.addView(bottomBanner);
                        bottomBanner.load();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + unityBannerId);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        RelativeLayout appLovinAdView = activity.findViewById(R.id.applovin_banner_view_container);
                        MaxAdView maxAdView = new MaxAdView(appLovinBannerId, activity);
                        maxAdView.setListener(new MaxAdViewAdListener() {
                            @Override
                            public void onAdExpanded(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdCollapsed(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdLoaded(@NonNull MaxAd ad) {
                                appLovinAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdDisplayed(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdClicked(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                                appLovinAdView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {

                            }
                        });

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                        if (darkTheme) {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_native_background_dark));
                        } else {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_native_background_light));
                        }
                        appLovinAdView.addView(maxAdView);
                        maxAdView.loadAd();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + appLovinBannerId);
                        break;

                    case APPLOVIN_DISCOVERY:
                        RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.applovin_discovery_banner_view_container);
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", appLovinBannerZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        this.appLovinAdView = new AppLovinAdView(adSize, activity);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryAdView.setVisibility(View.GONE);
                            }
                        });
                        appLovinDiscoveryAdView.addView(this.appLovinAdView);
                        this.appLovinAdView.loadNextAd();
                        break;

                    case MOPUB:
                        //Mopub has been acquired by AppLovin
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        ironSourceBannerView = activity.findViewById(R.id.ironsource_banner_view_container);
                        ISBannerSize size = ISBannerSize.BANNER;
                        ironSourceBannerLayout = IronSource.createBanner(activity, size);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        ironSourceBannerView.addView(ironSourceBannerLayout, 0, layoutParams);
                        if (ironSourceBannerLayout != null) {
                            ironSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                                @Override
                                public void onAdLoaded(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdLoaded");
                                    ironSourceBannerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdLoadFailed(IronSourceError ironSourceError) {
                                    Log.d(TAG, "onBannerAdLoadFailed" + " " + ironSourceError.getErrorMessage());
                                }

                                @Override
                                public void onAdClicked(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdClicked");
                                }

                                @Override
                                public void onAdLeftApplication(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdLeftApplication");
                                }

                                @Override
                                public void onAdScreenPresented(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdScreenPresented");
                                }

                                @Override
                                public void onAdScreenDismissed(AdInfo adInfo) {
                                    Log.d(TAG, "onBannerAdScreenDismissed");
                                }
                            });
                            IronSource.loadBanner(ironSourceBannerLayout, ironSourceBannerId);
                        } else {
                            Log.d(TAG, "IronSource.createBanner returned null");
                        }
                        break;

                    case WORTISE:
                        wortiseBannerAd = new com.wortise.ads.banner.BannerAd(activity);
                        wortiseBannerAd.setAdSize(Tools.getWortiseAdSize(activity));
                        wortiseBannerAd.setAdUnitId(wortiseBannerId);
                        wortiseBannerView = activity.findViewById(R.id.wortise_banner_view_container);
                        wortiseBannerView.addView(wortiseBannerAd);
                        wortiseBannerAd.loadAd();
                        wortiseBannerAd.setListener(new com.wortise.ads.banner.BannerAd.Listener() {
                            @Override
                            public void onBannerClicked(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                // Existing code for banner click handling
                            }

                            @Override
                            public void onBannerFailedToLoad(@NonNull com.wortise.ads.banner.BannerAd bannerAd, @NonNull AdError adError) {
                                wortiseBannerView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                                Log.d(TAG, "failed to load Wortise banner: " + adError);
                            }

                            @Override
                            public void onBannerImpression(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                // Add code here to handle banner impression events
                            }

                            @Override
                            public void onBannerLoaded(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                                wortiseBannerView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "Wortise banner loaded");
                            }
                        });
                        break;
                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

        public void destroyAndDetachBanner() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                if (adNetwork.equals(IRONSOURCE) || backupAdNetwork.equals(IRONSOURCE)) {
                    if (ironSourceBannerView != null) {
                        Log.d(TAG, "ironSource banner is not null, ready to destroy");
                        IronSource.destroyBanner(ironSourceBannerLayout);
                        ironSourceBannerView.removeView(ironSourceBannerLayout);
                    } else {
                        Log.d(TAG, "ironSource banner is null");
                    }
                }
            }
        }

    }

}
