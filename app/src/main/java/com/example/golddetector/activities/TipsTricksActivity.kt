package com.example.golddetector.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppInterstitialAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.ActivityTipsTricksBinding

class TipsTricksActivity : LocalizationActivity() {
    private lateinit var binding: ActivityTipsTricksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTipsTricksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            AppNativeAds.inflateBigAds(this@TipsTricksActivity,instructionsNativeAdd.adContainer)
            backBtn.setOnClickListener{
                onBackPressedDispatcher.onBackPressed()
                }

        }
    }
}