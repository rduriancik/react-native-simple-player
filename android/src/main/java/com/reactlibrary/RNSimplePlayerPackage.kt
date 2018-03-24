package com.reactlibrary

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class RNSimplePlayerPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return mutableListOf<NativeModule>(
                RNSimplePlayerModule(reactContext),
                RNSimplePlayerDialogModule(reactContext)
        )
    }

    // Deprecated from RN 0.47
    override fun createJSModules(): List<Class<out JavaScriptModule>> {
        return listOf()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}