package com.example.golddetector.ads_manager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.example.golddetector.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.*

class AppLivin_BannerAds {

    companion object {
        fun loadBannerAd(context: Context, bannerContainer: FrameLayout) {

            // Load AdMob banner ad
            val adView = AdView(context)
            val adSize = getAdaptiveAdSize(context, bannerContainer)
            adView.setAdSize(adSize)

            val adUnitId = context.getString(R.string.AM_BANNER_AD_ID)
            adView.adUnitId = adUnitId

            // Set up AdMob banner ad listener
            adView.adListener = object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    // If AdMob banner ad failed to load, load AppLovin banner ad
                    loadAppLovinBannerAd(context, bannerContainer)
                }

                override fun onAdLoaded() {
                    // If AdMob banner ad loaded successfully, show it
                    bannerContainer.visibility = View.VISIBLE
                    super.onAdLoaded()
                }
            }

            // Add AdMob banner ad to banner container
            bannerContainer.removeAllViews()
            bannerContainer.addView(adView)

            // Load AdMob banner ad
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        fun loadAppLovinBannerAd(context: Context, bannerContainer: FrameLayout) {

            // Load AppLovin banner ad
            val adView = MaxAdView(
                "838bdc5c7f681465",
                context
            )

            // Set up AppLovin banner ad listener
            adView.setListener(object : MaxAdViewAdListener {
                override fun onAdLoaded(ad: MaxAd?) {
                    // If AppLovin banner ad loaded successfully, show it
                    bannerContainer.visibility = View.VISIBLE
                }

                override fun onAdDisplayed(p0: MaxAd?) {
                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    // If AppLovin banner ad failed to load, show a default ad or hide the banner
                    bannerContainer.visibility = View.GONE
                }

                override fun onAdClicked(ad: MaxAd?) {}
                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}
                override fun onAdExpanded(ad: MaxAd?) {}
                override fun onAdCollapsed(p0: MaxAd?) {
                }

                override fun onAdHidden(ad: MaxAd?) {}
            })

            // Set up AppLovin banner ad view
            val adSize = getAdaptiveAdSize(context, bannerContainer)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                adSize.height // Set the height to the adaptive height
            )
            adView.layoutParams = layoutParams

            // Add AppLovin banner ad to banner container
            bannerContainer.removeAllViews()
            bannerContainer.addView(adView)

            // Load AppLovin banner ad
            adView.loadAd()
        }

        private fun getAdaptiveAdSize(context: Context, bannerContainer: FrameLayout): AdSize {
            val displayMetrics = context.resources.displayMetrics
            val widthPixels = displayMetrics.widthPixels
            val adWidth = (widthPixels / displayMetrics.density).toInt()

            // Calculate the height based on a desired aspect ratio (e.g., 6:5)
            val aspectRatio = 6.0 / 5.0


           return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)



        }
    }
}
