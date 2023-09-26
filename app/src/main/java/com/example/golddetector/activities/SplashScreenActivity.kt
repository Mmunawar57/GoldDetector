package com.example.golddetector.activities

import android.content.Intent

import android.os.Bundle
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity

import com.applovin.sdk.AppLovinSdk
import com.bumptech.glide.Glide
import com.example.golddetector.GoldApp
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.ads_manager.AppInterstitialAds

import com.example.golddetector.databinding.ActivitySplashScreenBinding
import com.example.golddetector.remoteconfig.RemoteConfigUtil
import com.facebook.ads.AudienceNetworkAds
import java.util.*
import kotlin.concurrent.schedule


class SplashScreenActivity : LocalizationActivity() {
    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        RemoteConfigUtil.getFirebaseRemoteConfig()
        initializeAdNetwork()

        binding.btnContinue.setOnClickListener {
            binding.loadingBar.visibility = View.VISIBLE
            Glide.with(this@SplashScreenActivity)
                .load(R.drawable.loading2)
                .into(binding.loadingBar)
            Timer("Start_timer").schedule(6000) {
                runOnUiThread {
                    if(!GoldApp.prefs!!.getBoolean("IS_OPENED", false)) {
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                LanguageActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                    }
                }

            }
        }
    }


    private fun initializeAdNetwork() {
      //  AudienceNetworkAds.initialize(this)
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this) {
            AppInterstitialAds.loadAmInterstitial(this)
            AppInterstitialAds.loadMaxInterAd(this)
            AppNativeAds.loadMaxNativeAd(this)
        }
        AppNativeAds.loadAmNativeAd(this){}

    }
}