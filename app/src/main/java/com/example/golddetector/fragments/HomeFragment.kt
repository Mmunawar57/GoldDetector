package com.example.golddetector.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.golddetector.R
import com.example.golddetector.activities.LanguageActivity

import com.example.golddetector.activities.TipsTricksActivity
import com.example.golddetector.ads_manager.AppLivin_BannerAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.FragmentHomeBinding
import com.example.golddetector.databinding.TipsTricksLayoutBinding


class HomeFragment : Fragment() {

    private lateinit var toggle: ActionBarDrawerToggle
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        AppNativeAds.inflateBigAds(requireContext(),binding.homeNativeAdd.adContainer)

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawer,
            R.string.open,
            R.string.close
        )

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {


                R.id.rate_us -> {

                    try {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=com.bitz.find.gold.scan.detect")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                    }

                }

                R.id.more_apps -> {
                    try {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/developer?id=Bitz+Tech")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                    }
                }

                R.id.privacy -> {
                    try {
                        val uri = Uri.parse("https://sites.google.com/view/golddetctorprivacy/home")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                    }
                }
                R.id.languages->{
                    try {
                        startActivity(Intent(requireContext(),LanguageActivity::class.java))
                    }catch (e:ActivityNotFoundException){}

                }

            }
            return@setNavigationItemSelectedListener true

        }
        binding.sideMenu.setOnClickListener {

            val navDrawer: DrawerLayout = binding.drawer
            // If the navigation drawer is not open then open it, if its already open then close it.
            if(!navDrawer.isDrawerOpen(GravityCompat.START))
                navDrawer.openDrawer(GravityCompat.START) else navDrawer.closeDrawer(
                GravityCompat.END
            )

        }
        binding.tipsTricks.setOnClickListener {
          startActivity(Intent(requireContext(),TipsTricksActivity::class.java))
        }
        binding.howToUse.setOnClickListener {
//            startActivity(Intent(requireContext(), How_to_use_Activity::class.java))
            findNavController().navigate(R.id.action_homeFragment_to_how_to_use_fragment)
        }

        binding.goldDetector.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_featuresFragment)
        }
        binding.metalDetector.setOnClickListener {
          findNavController().navigate(R.id.action_homeFragment_to_metalCalibirationFragment)
        }
        return binding.root
    }

    private fun tipsTricks() {
        val dialogueBinding = TipsTricksLayoutBinding.inflate(layoutInflater)
        val diloge = Dialog(requireContext(), ViewGroup.LayoutParams.MATCH_PARENT)
        diloge.setContentView(dialogueBinding.root)
//        AppLivin_BannerAd.createBannerAd(dialogueBinding.instructionBannerAdd,requireContext())
//        AppNativeAds.inflateNativeMainAd(requireContext(),dialogueBinding.instructionsNativeAdd.adContainer,dialogueBinding.instructionsNativeAdd.parent)
        dialogueBinding.backBtn.setOnClickListener {

            diloge.dismiss()


        }
//        diloge.setOnDismissListener {
////            AppInterstitialAds.showInterAd(requireContext()){
////
////            }
//        }
        diloge.show()
    }


}