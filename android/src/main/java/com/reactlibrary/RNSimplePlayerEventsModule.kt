package com.reactlibrary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.modules.core.DeviceEventManagerModule

class RNSimplePlayerEventsModule(private val reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext), LifecycleEventListener {

    companion object {
        private const val ON_HEADSET_PLUGGED = "ON_HEADSET_PLUGGED"
        private const val ON_NEAR_EAR = "ON_NEAR_EAR"
        private const val IS_PLUGGED_IN = "IS_PLUGGED_IN"
        private const val IS_UNPLUGGED = "IS_UNPLUGGED"
        private const val IS_NEAR_EAR = "IS_NEAR_EAR"
        private const val IS_NOT_NEAR_EAR = "IS_NOT_NEAR_EAR"

        private const val TAG = "RNSimplePlayerEvents"
    }

    private val sensorManager = reactContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                sendEvent(ON_NEAR_EAR, it.values[0].toInt() < 5)
            }
        }
    }
    private val onHeadsetPlugged = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_HEADSET_PLUG) {
                val state = intent.getIntExtra("state", -1)
                when (state) {
                    0 -> sendEvent(ON_HEADSET_PLUGGED, false) // Headset is unplugged
                    1 -> sendEvent(ON_HEADSET_PLUGGED, true) // Headset is plugged in
                    else -> sendEvent(ON_HEADSET_PLUGGED, false) // unknown state
                }
            }
        }
    }

    init {
        reactContext.addLifecycleEventListener(this)
    }

    override fun getName() = "RNSimplePlayerEvents"

    override fun onHostResume() {
        reactContext.registerReceiver(onHeadsetPlugged, IntentFilter(Intent.ACTION_HEADSET_PLUG))
        sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onHostPause() {
    }

    override fun onHostDestroy() {
        reactContext.unregisterReceiver(onHeadsetPlugged)
        sensorManager.unregisterListener(sensorEventListener)
    }

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