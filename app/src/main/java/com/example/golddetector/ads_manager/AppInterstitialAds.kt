package com.example.golddetector.ads_manager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.bumptech.glide.Glide
import com.example.golddetector.R
import com.example.golddetector.databinding.AdLoadinDialoagBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.schedule

class AppInterstitialAds {

    companion object {
        private var dialog: Dialog? = null
        var maxInterAd: MaxInterstitialAd? = null
        var adRequest = 0
        var isInterstitialAdsShowing=false
        var testID= "ca-app-pub-3940256099942544/1033173"

        var amInterAD: InterstitialAd? = null

        fun loadAmInterstitial(context: Context) {
            if (amInterAD != null)
                amInterAD = null

            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(
                context,
                context.getString(R.string.AM_INTER_AD_ID),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("Interstitial_ADS","Admob_ ${adError.message}")
                        amInterAD = null
                        Log.d("Interstitial_ADS", "Admob_onAdFailedToLoad:$ ")

                    }
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("Interstitial_ADS", "Admob_ Ad Loaded")
                        amInterAD = interstitialAd
                    }
                })
        }

        fun loadMaxInterAd(context: Context) {
            maxInterAd = null
            val id="18b7e3e74f76836g"
            val realId="7a4b5dc0ed1bd48d"
            maxInterAd = MaxInterstitialAd(realId, context as Activity)
            maxInterAd!!.loadAd()

        }
        fun showInterAd(context: Context, adCloseCallback: (Boolean) -> Unit) {
            val ctx = context as Activity
            if (amInterAD != null) {
                if (!ctx.isDestroyed && !ctx.isFinishing) {
                    showDialog(ctx)
                    amInterAD!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d("Interstitial_ADS", "Admob_onAdDismissedFullScreenContent: ")
                            super.onAdDismissedFullScreenContent()
                            isInterstitialAdsShowing = false
                            loadAmInterstitial(context)
                            adCloseCallback.invoke(true)
                        }


                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            isInterstitialAdsShowing=true
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            super.onAdFailedToShowFullScreenContent(error)
                            Log.d(" Interstitial_ADS", "onAdFailedToShowFullScreenContent: ${error.code}::${error.message}")
                        }
                    }

                    CoroutineScope(Dispatchers.Default).launch {
                        delay(700)
                        withContext(Dispatchers.Main) {
                            hideDialog(context)
                            if (amInterAD != null) {
                                amInterAD!!.show(context as Activity)
                            }
                        }
                    }
                }
            } else if (maxInterAd != null && maxInterAd!!.isReady) {

                if (!ctx.isDestroyed && !ctx.isFinishing) {
                    showDialog(ctx)

                    maxInterAd!!.setListener(object : MaxAdListener {
                        override fun onAdLoaded(ad: MaxAd?) {}
                        override fun onAdDisplayed(ad: MaxAd?) {
                            isInterstitialAdsShowing=true
                            Log.d("Interstitial_ADS", "AppLovin_onAdDisplayed: $")
                        }

                        override fun onAdHidden(ad: MaxAd?) {
                            loadMaxInterAd(context)
                            isInterstitialAdsShowing = false
                            adCloseCallback.invoke(true)
                        }
                        override fun onAdClicked(ad: MaxAd?) {
                        }

                        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                            Log.d("Interstitial_ADS", "AppLovin_onAdLoadFailed: ")
                        }
                        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                        }
                    })

                    Timer("ad").schedule(1000) {
                        (context as Activity).runOnUiThread {
                            val ctx = context as Activity
                            if (!ctx.isDestroyed && !ctx.isFinishing) {
                                hideDialog(context)
                                if (maxInterAd != null) {
                                    maxInterAd!!.showAd()
                                }
                            }
                        }
                    }
                }


            } else {
                loadMaxInterAd(context)
                loadAmInterstitial(context)
                adCloseCallback.invoke(false)
            }
        }

        private fun showDialog(context: Context) {
            val binding = AdLoadinDialoagBinding.inflate(LayoutInflater.from(context))
            dialog = Dialog(context)
            dialog!!.setContentView(binding.root)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCancelable(false)
            Glide.with(context).load(R.drawable.loading1).into(binding.loadingImage)

            dialog!!.show()
        }
        private fun hideDialog(context: Context) {
            val ctx = context as Activity
            if (!ctx.isDestroyed && !ctx.isFinishing) {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
            }
        }
    }
}