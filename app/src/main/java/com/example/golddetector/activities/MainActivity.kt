package com.example.golddetector.activities


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.Navigation
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppInterstitialAds

import com.example.golddetector.databinding.ActivityMainBinding
import com.example.golddetector.databinding.ExitDialogueBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : LocalizationActivity() {
    val handler = Handler()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.postDelayed({
            AppInterstitialAds.showInterAd(this@MainActivity) {}
        }, 700)

    }

    override fun onBackPressed() {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)


        when (navController.currentDestination?.id) {
            R.id.featuresFragment -> {
                navController.navigate(R.id.homeFragment)
            }

            R.id.homeFragment -> {
                showexit()
            }

            R.id.how_to_use_fragment -> {
                navController.navigate(R.id.homeFragment)
            }

            R.id.graphs_meter_fragment -> {
                navController.navigate(R.id.featuresFragment)
            }

            R.id.digital_meter_fragment -> {
                        navController.popBackStack()
            }

            R.id.analog_meter_fragment -> {
                navController.navigate(R.id.featuresFragment)
            }

            R.id.calibration_detector_fragment -> {
                        navController.popBackStack()
            }
            R.id.metalAnalogFragment -> {
                navController.popBackStack()
            }

            R.id.metalDetector -> {
                navController.popBackStack()
            }

            R.id.metalCalibirationFragment -> {
                navController.popBackStack()
            }

            R.id.metalDigitalFragment -> {
                navController.popBackStack()
            }

            R.id.metalGraphFragment -> {
                AppInterstitialAds.showInterAd(this) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.popBackStack()
                    }, 100)
                }
            }
            R.id.metalAnalogFragment -> {
                AppInterstitialAds.showInterAd(this) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.popBackStack()
                    }, 100)
                }
            }
            R.id.metalFragment -> {
                navController.popBackStack()
            }
        }
    }

    private fun showexit() {
        val dialogBinding = ExitDialogueBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogBinding.root)
//        AppNativeAds.inflateNativeMainAd(this,dialogBinding.exitNativeAd.adContainer,dialogBinding.exitNativeAd.parent)

        dialogBinding.exitNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.exitYes.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
    }
}
