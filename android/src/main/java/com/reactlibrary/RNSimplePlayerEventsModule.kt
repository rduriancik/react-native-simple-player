package com.reactlibrary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class RNSimplePlayerEventsModule(private val reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val ON_HEADSET_PLUGGED = "ON_HEADSET_PLUGGED"
        private const val ON_NEAR_EAR = "ON_NEAR_EAR"
        private const val IS_PLUGGED_IN = "IS_PLUGGED_IN"
        private const val IS_UNPLUGGED = "IS_UNPLUGGED"
        private const val IS_NEAR_EAR = "IS_NEAR_EAR"
        private const val IS_NOT_NEAR_EAR = "IS_NOT_NEAR_EAR"

        private const val TAG = "RNSimplePlayerEvents"
    }

    private var isHeadsetPluggedIn = false
    private var isNearEar = false
    private val sensorManager = reactContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                isNearEar = it.values[0].toInt() < 5
                Log.d(TAG, "onSensorChangedEvent - is near ear? $isNearEar")
                sendEvent(ON_NEAR_EAR, isNearEar)
            }
        }
    }
    private val onHeadsetPlugged = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_HEADSET_PLUG) {
                val state = intent.getIntExtra("state", -1)
                isHeadsetPluggedIn = when (state) {
                    0 -> false// Headset is unplugged
                    1 -> true // Headset is plugged in
                    else -> false // unknown state
                }
                Log.d(TAG, "OnHeadsetPlugged - is plugged in? $isHeadsetPluggedIn")
                sendEvent(ON_HEADSET_PLUGGED, isHeadsetPluggedIn)
            }
        }
    }

    @ReactMethod
    fun startEvents() {
        reactContext.registerReceiver(onHeadsetPlugged, IntentFilter(Intent.ACTION_HEADSET_PLUG))
        sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    @ReactMethod
    fun stopEvents() {
        reactContext.unregisterReceiver(onHeadsetPlugged)
        sensorManager.unregisterListener(sensorEventListener)
    }

    @ReactMethod
    fun isNearEar(promise: Promise) {
        promise.resolve(isNearEar)
    }

    @ReactMethod
    fun isHeadsetPluggedIn(promise: Promise) {
        promise.resolve(isHeadsetPluggedIn)
    } 

    override fun getName() = "RNSimplePlayerEvents"

    private fun sendEvent(eventName: String, state: Boolean) {
        val params = Arguments.createMap().apply {
            putBoolean("state", state)
        }

        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                ON_HEADSET_PLUGGED to ON_HEADSET_PLUGGED,
                ON_NEAR_EAR to ON_NEAR_EAR,
                IS_PLUGGED_IN to true,
                IS_UNPLUGGED to false,
                IS_NEAR_EAR to true,
                IS_NOT_NEAR_EAR to false
        )
    }
}