package com.example.golddetector.ads_manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.golddetector.R

import com.example.golddetector.GoldApp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class AppOpenManager(private val myApplication: GoldApp) :
    Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var isAppClosed = true // Add this flag


    private val UNIT_ID = "ca-app-pub-3940256099942544/5662855259"

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    val isAdAvailable: Boolean
        get() = appOpenAd != null
    init {

        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d("APP_OPEN_ADS", "onAppOpenAdFailedToLoad: ")
            }
        }
        val request = adRequest
        AppOpenAd.load(
            myApplication,
            myApplication.applicationContext.getString(R.string.AM_APPOPEN_AD_ID), request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback!!
        )
    }

    private fun showAdIfAvailable() {
        if (isShowingAd) {
            Log.d("APP_OPEN_ADS", "Interstitial ad is already showing. Skip AppOpenAd.")
            return
        }

        if (isAdAvailable) {
            Log.d("APP_OPEN_ADS", "Will show AppOpenAd.")
            isShowingAd = true // Set the flag to true when showing AppOpenAd

            val fullScreenContentCallback: FullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {

                    appOpenAd = null
                    fetchAd()
                    isShowingAd = false // Set the flag to false when AppOpenAd is dismissed

                }


                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)

                    isShowingAd = false // Set the flag to false when AppOpenAd fails to show

                }

                override fun onAdShowedFullScreenContent() {
                    // AppOpenAd is being shown

                }
            }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity!!)
        } else {
            Log.d("APP_OPEN_ADS", "No AppOpenAd available. Fetching new ad.")
            fetchAd()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity


    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        isAppClosed = true // Set the flag to true when app is stopped
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (!AppInterstitialAds.isInterstitialAdsShowing){
            showAdIfAvailable()
            Log.d("APP_OPEN_ADS", "onStart:->")
        }
        isAppClosed = false
        }
}

