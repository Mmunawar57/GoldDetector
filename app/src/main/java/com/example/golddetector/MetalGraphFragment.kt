package com.example.golddetector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.golddetector.ads_manager.AppInterstitialAds
import com.example.golddetector.ads_manager.AppNativeAds
import com.example.golddetector.databinding.FragmentMetalGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class MetalGraphFragment : Fragment(), SensorEventListener, OnChartValueSelectedListener {

    var DECIMAL_FORMATTER: DecimalFormat? = null
    private var result: TextView? = null
    private var sensorManager: SensorManager? = null
    private var vMediaPlayer: MediaPlayer? = null
    private var vChart: LineChart? = null
    private val binding by lazy {
        FragmentMetalGraphBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppNativeAds.inflateBigAds(requireContext(), binding.graphNativeadd)
        initView()
        return binding.root
    }

        @SuppressLint("SuspiciousIndentation")
        private fun initView() {
            vMediaPlayer = MediaPlayer()
            sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
            result = binding.result
            // define decimal formatter
            val symbols = DecimalFormatSymbols(Locale.US)
            symbols.decimalSeparator = '.'


            DECIMAL_FORMATTER = DecimalFormat("#.000", symbols)

            //
            binding.ivBack.setOnClickListener {
                AppInterstitialAds.showInterAd(requireContext()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
                }
            }
            populateGraphView()
        }
        override fun onResume() {
            super.onResume()
            sensorManager?.registerListener(this,
                sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL)
            try {
                if (vMediaPlayer != null) {
                    vMediaPlayer!!.stop()
                    vMediaPlayer!!.release()
                }
            } catch (e: Exception) {
                Log.d("ERROR1", "onResume: $e")
            }
        }

        override fun onPause() {
            super.onPause()
            sensorManager?.unregisterListener(this)
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
                binding.valueX.text = Math.round(magX).toString()
                binding.valueY.text = Math.round(magY).toString()
                binding.valueZ.text = Math.round(magZ).toString()
                val sqrt = Math.sqrt((magX * magX + magY * magY + magZ * magZ).toDouble()).toFloat()
                addEntry(sqrt)
                vChart!!.axisLeft.axisMaximum = 40.0f + sqrt
            }
        }

        private fun setMagnitudeText(mag: Double) {
            if (mag < 60) {
                binding.radText.text = getString(R.string.no_potential_camera_detected)
            } else if (mag > 60 && mag < 80) {
                binding.radText.text = getString(R.string.mineral_resources_detected)
            } else if (mag > 80 && mag < 120) {
                startBeep()
                binding.radText.text = getString(R.string.potential_material_detected)
            } else if (mag > 120 && mag < 150) {
                startBeep()
                binding.radText.text = getString(R.string.material_detected)
            } else {
                binding.radText.text = getString(R.string.other_high_radiation_metal_detected)
            }
        }

        private fun startBeep() {
            vMediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep)
            vMediaPlayer!!.start()
        }

        private fun populateGraphView() {
            val lineChart = binding.chartview
            vChart = lineChart
            lineChart.setOnChartValueSelectedListener(this)
            vChart!!.description.isEnabled = true
            vChart!!.setTouchEnabled(true)
            vChart!!.isDragEnabled = true
            vChart!!.setScaleEnabled(true)
            vChart!!.setDrawGridBackground(false)
            vChart!!.setPinchZoom(true)
            vChart!!.setBackgroundColor(Color.parseColor("#464645"))
            val lineData = LineData()
            lineData.setValueTextColor(-1)
            vChart!!.data = lineData
            val legend = vChart!!.legend
            legend.form = Legend.LegendForm.LINE
            legend.textColor = -1
            val xAxis = vChart!!.xAxis
            xAxis.textColor = -1
            xAxis.setDrawGridLines(false)
            xAxis.setAvoidFirstLastClipping(true)
            xAxis.isEnabled = true
            val axisLeft = vChart!!.axisLeft
            axisLeft.textColor = -1
            axisLeft.axisMaximum = 100.0f
            axisLeft.axisMinimum = 0.0f
            axisLeft.setDrawGridLines(true)
            vChart!!.axisRight.isEnabled = false
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


        //MPA Chart
        override fun onValueSelected(e: Entry?, h: Highlight?) {}

        override fun onNothingSelected() {}

        private fun addEntry(f: Float) {
            val lineData = vChart!!.data as LineData
            if (lineData != null) {
                var iDataSet= lineData.getDataSetByIndex(0)
                if (iDataSet == null) {
                    iDataSet = createSet()
                    lineData.addDataSet(iDataSet as ILineDataSet)
                }
                lineData.addEntry(Entry(iDataSet.entryCount.toFloat(), f), 0)
                lineData.notifyDataChanged()
                vChart!!.notifyDataSetChanged()
                vChart!!.setVisibleXRangeMaximum(2000.0f)
                vChart!!.moveViewToX(lineData.entryCount.toFloat())
            }
        }

        private fun createSet(): LineDataSet {
            val lineDataSet = LineDataSet(null, "Radiation Data")
            lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSet.color = Color.parseColor("#FFFFFF")
            lineDataSet.lineWidth = 2.0f
            lineDataSet.setDrawCircles(false)
            lineDataSet.fillAlpha = 65
            lineDataSet.fillColor = Color.parseColor("#FFFFFF")
            lineDataSet.highLightColor = Color.parseColor("#FFFFFF")
            lineDataSet.valueTextColor = -1
            lineDataSet.valueTextSize = 9.0f
            lineDataSet.setDrawValues(false)
            return lineDataSet
        }
    }