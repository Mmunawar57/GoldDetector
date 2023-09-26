package com.example.golddetector.remoteconfig

import android.annotation.SuppressLint
import android.util.Log
import com.example.golddetector.GoldApp
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtil {
    private const val remoteTAG="RemoteConfig"
    private const val btn_color="default_color_of_the_button"

    private val DEFAULTS:HashMap< String,Any > =
        hashMapOf(

            btn_color to "#B88021"
        )
     fun getFirebaseRemoteConfig() {

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
         val configSettings = remoteConfigSettings {
             minimumFetchIntervalInSeconds = 0L
         }
         remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful){
                val color=remoteConfig["btn_color"].asString()
                Log.e("remotev","remote value is ${color}")
                GoldApp.prefs?.putString("btn_color",color)

            }

        }
            .addOnFailureListener {
                Log.e("remotev","fail")
            }

    }


}
