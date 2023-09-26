package com.example.golddetector

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.ads_manager.AppOpenManager

import com.example.golddetector.utils.Prefs
import com.facebook.ads.AudienceNetworkAds
import java.util.Locale

class GoldApp:LocalizationApplication(){
     private lateinit var appOpenManager: AppOpenManager
    companion object {
        var prefs: Prefs? = null
    }
    override fun getDefaultLanguage(context: Context): Locale = Locale.ENGLISH
    override fun onCreate() {
        super.onCreate()
        prefs= Prefs(this)
        AudienceNetworkAds.initialize(this)
        appOpenManager=AppOpenManager(this)
     //   AppNativeAds.loadMaxNativeAd(this)
        AppNativeAds.initializeAdMobNativeAd(this)
      //  AppNativeAds.loadMaxNativeAd(this)
      // AppNativeAds.loadAmNativeAd(this){}
    }
}