package com.example.golddetector.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppInterstitialAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.FragmentFeaturesBinding

class FeaturesFragment : Fragment() {
    private val binding by lazy {
        FragmentFeaturesBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       AppNativeAds.inflateBigAds(requireContext(),binding.framelayoutAds)
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.graphMeter.setOnClickListener {
            AppInterstitialAds.showInterAd(requireContext()) { isClose ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().navigate(R.id.action_featuresFragment_to_graphs_meter_fragment)
                    },100)

            }
        }
        binding.digitalMeter.setOnClickListener {
                    findNavController().navigate(R.id.action_featuresFragment_to_digital_meter_fragment)

        }
        binding.analogueMeter.setOnClickListener {
            AppInterstitialAds.showInterAd(requireContext()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_featuresFragment_to_analog_meter_fragment)
                },100)
            }
        }

        binding.calibrationDetector.setOnClickListener {
                findNavController().navigate(R.id.action_featuresFragment_to_calibration_detector_fragment)
        }

        return binding.root

    }



    }