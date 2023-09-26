package com.example.golddetector.ads_manager

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.example.golddetector.GoldApp
import com.example.golddetector.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AppNativeAds {

    companion object {
        var maxNativeAd: MaxAd? = null
        var maxNativeAdLoader: MaxNativeAdLoader? = null
        var amNativeAd: NativeAd? = null
        val testId = "ca-app-pub-3940256099942544/2247696210"

        fun initializeAdMobNativeAd(context: Context) {
            MobileAds.initialize(context) {}
        }

        var nativeRequesCounter = 0

        //splashAds
        private fun loadAmNativeAdSplash(context: Context, frameLayout: FrameLayout) {
            nativeRequesCounter++


            val adID = context.getString(R.string.AM_NATIVE_AD_ID)
            val builder =
                AdLoader.Builder(context, adID)
                    .forNativeAd { nativeAd ->

                        //Log.d("AMOB_NATIVE", "loadAmNativeAdSplash:$amNativeAd ")
                        inflateAdmobNativeAdBig(context, frameLayout, nativeAd)
                    }.withAdListener(object : AdListener() {
                        override fun onAdClosed() {
                            super.onAdClosed()
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            loadMaxNativeAdSplash(context, frameLayout)
                            Log.e("AM_NATIVE_Splash", "onAdError:failed-->${p0.code} ")
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            Log.e("AM_NATIVE", "onAdLoaded:native_ad_loaded ")
                        }
                    }).build()

            builder.loadAd(AdRequest.Builder().build())
        }

        fun loadMaxNativeAdSplash(context: Context, parent: FrameLayout) {
            Log.d("Native_Ads", "loadMaxNativeAdSplash: ")
            val maxNativeAdLoader = MaxNativeAdLoader(
                "492837417ee6e54d",
                context as Activity
            )
            maxNativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                    Log.d("Native_Ads", "onNativeAdLoaded: ")
                    super.onNativeAdLoaded(p0, p1)
                    val binder: MaxNativeAdViewBinder =
                        MaxNativeAdViewBinder.Builder(R.layout.max_native_ad_view)
                            .setTitleTextViewId(R.id.ad_title_view)
                            .setBodyTextViewId(R.id.ad_body_view)
                            .setAdvertiserTextViewId(R.id.ad_advertiser_view)
                            .setIconImageViewId(R.id.ad_icon_view)
                            .setMediaContentViewGroupId(R.id.ad_media_view)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.ad_cta_view)
                            .build()
                    val maxAdView = MaxNativeAdView(binder, context)
                    if(maxNativeAdLoader.render(maxAdView, p1)) {

                        val adcontent =
                            maxAdView.findViewById<LinearLayout>(R.id.appLovin_native_layout)
                        parent.visibility = View.VISIBLE
                        parent.removeAllViews()
                        parent.addView(maxAdView)
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)

                            adcontent.visibility = View.VISIBLE
                            loadMaxNativeAd(context)
                        }
                    }
                }


                override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                    super.onNativeAdLoadFailed(p0, p1)
                    Log.d("Native_Ads", "onNativeAdLoaded:${p1?.code} ")
                }

                override fun onNativeAdClicked(p0: MaxAd?) {
                    super.onNativeAdClicked(p0)
                }
            })

            maxNativeAdLoader.loadAd()

        }

        fun inflateSplashAds(context: Context?, frameLayout: FrameLayout) {

            Log.d("SPLASH_ADS", "inflateSplashAds: 0->$context")
            loadAmNativeAdSplash(context!!, frameLayout)


            Log.d("SPLASH_ADS", "inflateSplashAds: 1->$context")

        }


        //big Ads
        fun loadMaxNativeAd(context: Context) {
            Log.d("Native_Ads_Apploviin", "loadMaxNativeAd: ")
            if(maxNativeAd != null) {
                maxNativeAd = null
            }
            maxNativeAdLoader = MaxNativeAdLoader(
                "492837417ee6e54d",
                context as Activity
            )
            maxNativeAdLoader!!.setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                    super.onNativeAdLoaded(p0, p1)
                    maxNativeAd = p1
                    Log.e("Native_Ads_Apploviin", "onNativeAdLoaded:${p1.toString()} ")

                }

                override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                    super.onNativeAdLoadFailed(p0, p1)
                    // loadAmNativeAd(context,frameLayout){}
                    Log.d("Native_Ads_Apploviin", "onNativeAdLoaded:${p1?.code} ")
                }

                override fun onNativeAdClicked(p0: MaxAd?) {
                    super.onNativeAdClicked(p0)
                }
            })

            maxNativeAdLoader!!.loadAd()

        }
        fun loadAmNativeAd(context: Context, onAdAdLoadedCallback: (Boolean) -> Unit) {
            Log.d("Native_Ads", "loadAmNativeAd:->$onAdAdLoadedCallback ")
            nativeRequesCounter++

            val builder = AdLoader.Builder(context, context.getString(R.string.AM_NATIVE_AD_ID))
                .forNativeAd { nativeAd ->
                    amNativeAd = nativeAd
                    onAdAdLoadedCallback.invoke(true)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdClosed() {
                        super.onAdClosed()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        onAdAdLoadedCallback.invoke(false)
                        Log.e("Native_Ads", "Admob_onAdError:failed-->${p0.code} ")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        loadAmNativeAd(context) {}
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.e("Native_Ads", "Amob_onAdLoaded:")
                    }
                })
                .build()

            builder.loadAd(AdRequest.Builder().build())
        }

        private fun inflateAdmobNativeAdBig(
            context: Context?, frameLayout: FrameLayout,
            admobnative: NativeAd?,
        ) {
            Log.d("Native_Ads", "inflateAdmobNativeAdBig: ")
            if(admobnative == null)
                return
            frameLayout.removeAllViews()
            frameLayout.visibility = View.VISIBLE
            var admobview: NativeAdView? = null

            admobview = LayoutInflater.from(context)
                .inflate(R.layout.admob_native_ad_max, null) as NativeAdView?
            frameLayout.addView(admobview)
            val contentview = admobview?.findViewById<LinearLayout>(R.id.adMod_layout)
            /* CoroutineScope(Dispatchers.Main).launch {
                 delay(1000)*/
            contentview?.visibility = View.VISIBLE
            // Set other ad assets.
            if(admobview != null) {
                admobview.mediaView = admobview.findViewById(R.id.AdMedia)
            }
            if(admobview != null) {
                admobview.headlineView = admobview.findViewById(R.id.txtAdTitle)
            }
            if(admobview != null) {
                admobview.bodyView = admobview.findViewById(R.id.txtAdBody)
            }
            if(admobview != null) {
                admobview.callToActionView = admobview.findViewById(R.id.btnAdCta)
            }
            if(admobview != null) {
                admobview.iconView = admobview.findViewById(R.id.imgIcon)
            }

            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            if(admobview != null) {
                (admobview.headlineView as TextView).text = admobnative?.headline
            }
            admobnative?.mediaContent?.let { admobview?.mediaView?.setMediaContent(it) }

            if(admobnative?.body == null) {
                if(admobview != null) {
                    admobview.bodyView?.visibility = View.INVISIBLE
                }
            } else {
                if(admobview != null) {
                    admobview.bodyView?.visibility = View.VISIBLE
                }
                if(admobview != null) {
                    (admobview.bodyView as TextView).text = admobnative.body
                }
            }

            if(admobnative?.callToAction == null) {
                if(admobview != null) {
                    admobview.callToActionView?.visibility = View.INVISIBLE
                }
            } else {
                if(admobview != null) {
                    admobview.callToActionView?.visibility = View.VISIBLE
                }
                if(admobview != null) {
                    (admobview.callToActionView as TextView).text = admobnative.callToAction
                }
            }

            if(admobnative?.icon == null) {
                if(admobview != null) {
                    admobview.iconView?.visibility = View.GONE
                }
            } else {
                (admobview?.iconView as ImageView).setImageDrawable(
                    admobnative.icon?.drawable
                )
                admobview.iconView?.visibility = View.VISIBLE
            }

            if(admobnative != null) {

                admobview?.setNativeAd(admobnative)

            }

        }
        private fun inflateNativeMainAd(context: Context, parent: FrameLayout) {
            val binder: MaxNativeAdViewBinder =
                MaxNativeAdViewBinder.Builder(R.layout.max_native_ad_view)
                    .setTitleTextViewId(R.id.ad_title_view)
                    .setBodyTextViewId(R.id.ad_body_view)
                    .setAdvertiserTextViewId(R.id.ad_advertiser_view)
                    .setIconImageViewId(R.id.ad_icon_view)
                    .setMediaContentViewGroupId(R.id.ad_media_view)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.ad_cta_view)
                    .build()

            val maxAdView = MaxNativeAdView(binder, context)
            if(maxNativeAdLoader != null && maxNativeAd != null) {
                if(maxNativeAdLoader!!.render(maxAdView, maxNativeAd)) {
                    val adcontent =
                        maxAdView.findViewById<LinearLayout>(R.id.appLovin_native_layout)
                    parent.visibility = View.VISIBLE
                    parent.removeAllViews()
                    parent.addView(maxAdView)
                    /*  CoroutineScope(Dispatchers.Main).launch {
                          delay(1000)*/
                    adcontent.visibility = View.VISIBLE
                    loadMaxNativeAd(context)
                    //  }

                }
            } else {
                loadMaxNativeAd(context)
            }
        }
        fun inflateBigAds(context: Context, frameLayout: FrameLayout) {
            if(amNativeAd != null) {
                inflateAdmobNativeAdBig(context, frameLayout, amNativeAd)
                Log.d("ADMOD_APPlovin", "inflateBigAds:0$amNativeAd ")
            } else if(maxNativeAd != null) {
                inflateNativeMainAd(context, frameLayout)
                Log.d("ADMOD_APPlovin", "inflateBigAds:1$context ")
            } else {
                loadAmNativeAd(context) {}
            }
        }

    }
}