package com.example.golddetector.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.golddetector.adapters.AdapterItems
import com.example.golddetector.adapters.SliderAdapter
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppInterstitialAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.FragmentHowToUseFragmentBinding
import kotlin.math.abs


class How_to_use_fragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter
    private val binding by lazy {
        FragmentHowToUseFragmentBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initview()
        AppNativeAds.inflateBigAds(requireContext(),binding.howtouseNativeAdd)
        binding.skip.setOnClickListener {
            AppInterstitialAds.showInterAd(requireContext()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().popBackStack()
                }, 100)
            }
        }
        return binding.root
    }
    private fun initview() {
        viewPager = binding.viewPager
        val list = ArrayList<AdapterItems>()
        list.add(AdapterItems(R.drawable.how1))
        list.add(AdapterItems(R.drawable.how2))
        list.add(AdapterItems(R.drawable.how3))
        list.add(AdapterItems(R.drawable.how4))

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // Set orientation if needed
        viewPager.setPageTransformer(transformation())
        sliderAdapter = SliderAdapter(list)
        viewPager.adapter = sliderAdapter
        binding.indicator.setViewPager(viewPager)

        // Set a ViewPager2.OnPageChangeCallback
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Check if it's the last index and make the skip button visible
                if (position == list.size - 1) {
                    binding.skip.visibility = View.VISIBLE
                } else {
                    binding.skip.visibility = View.INVISIBLE
                }
            }
        })

        Log.d("List_size", "initview: ")
    }

    private fun transformation(): CompositePageTransformer {
        val transform= CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(30))
        transform.addTransformer { page, position ->
            page.scaleY=0.85f+(1- abs(position))*0.15f



        }
        return transform
    }
//    private fun getimages():List<Int>{
//        val data= arrayListOf<Int>()
//        data.add(R.drawable.img1)
//        data.add(R.drawable.img2)
//        data.add(R.drawable.img3)
//        return data
//    }

    }
