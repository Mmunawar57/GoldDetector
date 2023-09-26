package com.example.golddetector.fragments

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppInterstitialAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.FragmentMetalBinding
import java.util.logging.Handler


class MetalFragment : Fragment() {
    private val binding by lazy {
        FragmentMetalBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppNativeAds.inflateBigAds(requireContext(),binding.framelayoutAds)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.graphMeter.setOnClickListener {
//           AppInterstitialAds.showInterAd(requireContext()){
            findNavController().navigate(R.id.action_metalFragment_to_metalGraphFragment)
//            }

        }
        binding.digitalMeter.setOnClickListener {
            AppInterstitialAds.showInterAd(requireContext()){
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_metalFragment_to_metalDigitalFragment)
                },100)
           }
        }
        binding.analogueMeter.setOnClickListener {
//            AppInterstitialAds.showInterAd(requireContext()){
            findNavController().navigate(R.id.action_metalFragment_to_metalAnalogFragment)
//            }
        }

        binding.calibrationDetector.setOnClickListener {
           AppInterstitialAds.showInterAd(requireContext()){
             android.os.Handler(Looper.getMainLooper()).postDelayed({
                 findNavController().navigate(R.id.action_metalFragment_to_metalCalibirationFragment)
             },100)

            }

        }
        return binding.root
    }

}