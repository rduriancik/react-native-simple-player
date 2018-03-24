package com.reactlibrary

import android.app.AlertDialog
import android.view.LayoutInflater
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

/**
 * Created by duriancik on 24/03/18.
 */
class RNSimplePlayerDialogModule(private val reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "RNSimplePlayerDialog"

    @ReactMethod
    fun showDialog() {
        val dialog = AlertDialog.Builder(reactContext).apply {
            val inflater = LayoutInflater.from(reactContext)

            setView(inflater.inflate(R.layout.dialog_layout, null))
            setTitle("Loud music alert!")
            setNegativeButton("Close") {
                dialog, _ -> dialog.dismiss()
            }
            setCancelable(false)
        }.create()

        dialog.show()
    }
}