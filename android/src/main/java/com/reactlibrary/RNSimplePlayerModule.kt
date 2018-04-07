package com.reactlibrary

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class RNSimplePlayerModule(private val reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext), PlaybackListener {

    private val mPlayerController: PlayerController =
            MediaPlayerHolder(reactContext).apply { setPlaybackListener(this@RNSimplePlayerModule) }

    @ReactMethod
    fun loadAudioFile(filePath: String, promise: Promise) {
        promise.resolve(mPlayerController.loadMedia(filePath))
    }

    @ReactMethod
    fun play() {
        mPlayerController.play()
    }

    @ReactMethod
    fun pause() {
        mPlayerController.pause()
    }

    @ReactMethod
    fun reset() {
        mPlayerController.reset()
    }

    @ReactMethod
    fun isPlaying(promise: Promise) {
        promise.resolve(mPlayerController.isPlaying())
    }

    @ReactMethod
    fun seekTo(position: Int) {
        mPlayerController.seekTo(position)
    }

    @ReactMethod
    fun release() {
        mPlayerController.release()
    }

    override fun onStateChanged(state: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPositionChanged(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDurationChanged(duration: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlaybackCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName() = "RNSimplePlayer"
}