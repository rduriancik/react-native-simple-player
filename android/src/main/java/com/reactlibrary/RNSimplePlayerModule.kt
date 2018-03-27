package com.reactlibrary

import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class RNSimplePlayerModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var loudMusicDialog: Dialog? = null

    @ReactMethod
    fun showLoudMusicDialog(title: String?, message: String?, buttonText: String?) {
        if (loudMusicDialog == null) {
            loudMusicDialog = AlertDialog.Builder(currentActivity).apply {
                val inflater = LayoutInflater.from(currentActivity)

                setView(inflater.inflate(R.layout.dialog_layout, null))
                setTitle(title ?: reactContext.getString(R.string.dialog_loud_music_title))
                setMessage(message ?: reactContext.getString(R.string.dialog_loud_music_message))
                setNegativeButton(buttonText ?: reactContext.getString(R.string.dialog_loud_music_close)) { dialog, _ ->
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

    override fun getName() = "RNSimplePlayer"
}