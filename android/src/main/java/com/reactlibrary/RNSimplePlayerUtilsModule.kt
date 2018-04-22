package com.reactlibrary

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import com.facebook.react.bridge.*

class RNSimplePlayerUtilsModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    companion object {
        private const val TAG = "RNSimplePLayerUtils"
    }

    private var loudMusicDialog: Dialog? = null
    private val mProximityWakeLock: PowerManager.WakeLock? by lazy {
        val powerManager = reactContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG)
        } else {
            null
        }
    }
    private var lastWindowParams: WindowManager.LayoutParams? = null

    override fun getName() = "RNSimplePlayerUtils"

    @ReactMethod
    fun showLoudMusicDialog() {
        showLoudMusicDialogWithParameters(null, null, null) // default params not working in react
    }

    @ReactMethod
    fun showLoudMusicDialogWithParameters(title: String? = null, message: String? = null, buttonText: String? = null) {
        if (loudMusicDialog == null) {
            loudMusicDialog = AlertDialog.Builder(currentActivity).apply {
                val inflater = LayoutInflater.from(currentActivity)

                setView(inflater.inflate(R.layout.dialog_layout, null))
                setTitle(title ?: reactContext.getString(R.string.dialog_loud_music_title))
                setMessage(message ?: reactContext.getString(R.string.dialog_loud_music_message))
                setNegativeButton(buttonText
                        ?: reactContext.getString(R.string.dialog_loud_music_close)) { dialog, _ ->
                    dialog.dismiss()
                }
                setCancelable(false)
            }.create()
        }

        loudMusicDialog?.show()
    }

    @ReactMethod
    fun hideLoudMusicDialog() {
        loudMusicDialog?.dismiss()
    }

    @ReactMethod
    fun isLoudMusicDialogShown(promise: Promise) {
        promise.resolve(loudMusicDialog?.isShowing)
    }

    @ReactMethod
    fun turnScreenOff() {
        if (mProximityWakeLock != null) {
            Log.d(TAG, "Turn screen off via proximity lock")
            acquireProximityWakeLock()
        } else {
            Log.d(TAG, "Turn screen off manually")
            manuallyTurnScreenOff()
        }
    }

    @ReactMethod
    fun turnScreenOn() {
        if (mProximityWakeLock != null) {
            Log.d(TAG, "Turn screen on via proximity lock")
            releaseProximityWakeLock()
        } else {
            Log.d(TAG, "Turn screen on manually")
            manuallyTurnScreenOn()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun acquireProximityWakeLock() {
        mProximityWakeLock?.let {
            synchronized(it) {
                if (!it.isHeld) {
                    Log.d(TAG, "Proximity wake lock acquired")
                    it.acquire()
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun releaseProximityWakeLock() {
        mProximityWakeLock?.let {
            synchronized(it) {
                if (it.isHeld) {
                    Log.d(TAG, "Proximity wake lock released")
                    it.release(PowerManager.RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY)
                }
            }
        }
    }

    private fun manuallyTurnScreenOff() {
        UiThreadUtil.runOnUiThread {
            currentActivity?.let {
                Log.d(TAG, "manually turn the screen off")
                val window = it.window
                val params = window.attributes
                lastWindowParams = params
                params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
                window.attributes = params
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    private fun manuallyTurnScreenOn() {
        UiThreadUtil.runOnUiThread {
            currentActivity?.let {
                Log.d(TAG, "manually turn the screen off")
                val window = it.window
                window.attributes = if (lastWindowParams != null) {
                    lastWindowParams
                } else {
                    val params = window.attributes
                    params.screenBrightness = -1f
                    params
                }
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}