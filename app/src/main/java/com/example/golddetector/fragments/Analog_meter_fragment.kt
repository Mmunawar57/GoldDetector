package com.example.golddetector.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.golddetector.R
import com.example.golddetector.ads_manager.AppNativeAds

import com.example.golddetector.databinding.FragmentAnalogMeterFragmentBinding
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class Analog_meter_fragment : Fragment(),SensorEventListener {
    var DECIMAL_FORMATTER: DecimalFormat? = null
    var lineEntries: ArrayList<*>? = null
    private var result: TextView? = null
    private var sensorManager: SensorManager? = null
    private var vMediaPlayer: MediaPlayer? = null

    private val binding by lazy {
        FragmentAnalogMeterFragmentBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       AppNativeAds.inflateBigAds(requireContext(),binding.analogNativeAd)
      initView()
        return binding.root
    }
    private fun initView() {

        vMediaPlayer = MediaPlayer()
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        result = binding.result
        // define decimal formatter
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.decimalSeparator = '.'
        DECIMAL_FORMATTER = DecimalFormat("#.000", symbols)
        lineEntries = ArrayList<Any>()
        //
        binding.Back.setOnClickListener {
            findNavController().navigate(R.id.action_analog_meter_fragment_to_featuresFragment)
        }





    }



    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        try {
            if (vMediaPlayer != null) {
                vMediaPlayer!!.stop()
                vMediaPlayer!!.release()
            }
        }catch (e: Exception){
            Log.d("ERROR1", "onResume: $e")
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            val magX = event.values[0]
            val magY = event.values[1]
            val magZ = event.values[2]
            val magnitude = Math.sqrt((magX * magX + magY * magY + magZ * magZ).toDouble())
            // set value on the screen
            result!!.text = DECIMAL_FORMATTER!!.format(magnitude) + " \u00B5T"
            setMagnitudeText(magnitude)
            binding.value1.text = magX.roundToInt().toString()
            binding.value2.text = magY.roundToInt().toString()
            binding.value3.text = magZ.roundToInt().toString()
            populateGraphView(magnitude.toInt())
        }
    }

    private fun setMagnitudeText(mag: Double) {
        if (mag < 60) {
            binding.radText.text = getString(R.string.no_potential_camera_detected)
        } else if (mag > 60 && mag < 80) {
            binding.radText.text = getString(R.string.computer_tv_mobile_detected)
        } else if (mag > 80 && mag < 120) {
            startBeep()
            binding.radText.text = getString(R.string.potential_small_speaker_detected)
        } else if (mag > 120 && mag < 150) {
            startBeep()
            binding.radText.text = getString(R.string.camera_detected)
        } else {
            binding.radText.text = getString(R.string.high_radiation_detected)
        }
    }

    private fun startBeep() {
        vMediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep)
        vMediaPlayer!!.start()
    }

    private fun populateGraphView(mag: Int) {
        val speedmeter = binding.meterView
        speedmeter.maxSpeed = 100f
        speedmeter.speedTo(mag.toFloat())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
