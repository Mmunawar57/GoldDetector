package com.example.golddetector.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.golddetector.GoldApp
import com.example.golddetector.R
import com.example.golddetector.databinding.ActivityWelcomeBinding
class WelcomeActivity : LocalizationActivity() {

    private val binding by lazy {
       ActivityWelcomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        GoldApp.prefs!!.putBoolean("IS_OPENED",true)
//        AppNativeAds.inflateNativeMainAd(this,binding.welcomeNativeAdd.adContainer,binding.welcomeNativeAdd.parent)
        var n=1
        binding.apply {
            n++
            Log.d("increment", "onCreate:$n ")
            btnContinue.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()

            }
            styleText(txtAppName,"Gold Metal Detector App")
        }
    }
    private fun styleText(textView: TextView, inputString: String) {
        val spannableString = SpannableString(inputString)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FFD700")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 5, inputString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }
}