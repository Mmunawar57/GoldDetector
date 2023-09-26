package com.example.golddetector.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.golddetector.GoldApp
import com.example.golddetector.R
import com.example.golddetector.adapters.LanguageAdapter
import com.example.golddetector.ads_manager.AppNativeAds

import com.example.golddetector.databinding.ActivityLanguageBinding
import com.example.golddetector.interfaces.OnitemClickListener
import com.example.golddetector.model.Languages


class LanguageActivity : LocalizationActivity(), OnitemClickListener {
    private val binding by lazy {
        ActivityLanguageBinding.inflate(layoutInflater)
    }
    private val items = mutableListOf(
        Languages(R.drawable.english_flag_icon, "English", "en"),
        Languages(R.drawable.arabic_flag, "Arabic", "ar"),
        Languages(R.drawable.philipino_flag, "Philipino", "fil"),
        Languages(R.drawable.french_flag, "French", "fr"),
        Languages(R.drawable.hindi_flag, "Hindi", "hi"),
        Languages(R.drawable.portuguese_flag, "Portuguese", "pt"),
        Languages(R.drawable.russian_flag, "Russian", "ru"),
        Languages(R.drawable.turkey_flag, "Turkish", "tr"),
        Languages(R.drawable.pakistan_flag, "Urdu", "ur"),

    )
    private lateinit var adapter: LanguageAdapter
    private var selectedItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppNativeAds.inflateBigAds(this,binding.framelayoutAds)
        val savedLanguageCode = GoldApp.prefs!!.getString("selected_language", "en")
        val savedLanguagePosition = items.indexOfFirst { it.languageCode == savedLanguageCode }

        selectedItemPosition = if (savedLanguagePosition != -1) savedLanguagePosition else 0

        binding.done.setOnClickListener {
            val selectedLanguageCode = items[selectedItemPosition].languageCode
            if(!GoldApp.prefs!!.getBoolean("IS_OPENED",false)) {
                Log.d("language_checked", "gold activity is not opened ")
                setLanguage(selectedLanguageCode)
                recreate()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                // Set the selected language using LocalizationActivityDelegate
                setLanguage(selectedLanguageCode)
                Log.d("language_checked", "gold activity already opened ")
                // Recreate the current activity to apply the language change
                recreate()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.languagesRecycler.layoutManager = LinearLayoutManager(this)

        adapter = LanguageAdapter(items, this, selectedItemPosition) // Pass selectedItemPosition
        binding.languagesRecycler.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    // Implement the onItemClick method to handle item clicks
    override fun onItemClick(position: Int) {
        if (position != selectedItemPosition) {
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = position
            adapter.setSelectedItemPosition(selectedItemPosition)
            adapter.notifyItemChanged(previousSelectedItemPosition)
            adapter.notifyItemChanged(selectedItemPosition)

            // Save the selected language code in SharedPreferences
            val selectedLanguageCode = items[selectedItemPosition].languageCode
            GoldApp.prefs!!.putString("selected_language", selectedLanguageCode)
        }
    }
}