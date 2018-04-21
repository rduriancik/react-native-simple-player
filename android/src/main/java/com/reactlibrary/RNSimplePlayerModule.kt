package com.reactlibrary

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.lang.ref.WeakReference

class RNSimplePlayerModule(private val reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext), PlaybackListener {

    companion object {
        private const val ON_STATE_CHANGED = "ON_STATE_CHANGED"
        private const val ON_POSITION_CHANGED = "ON_POSITION_CHANGED"
        private const val ON_DURATION_CHANGED = "ON_DURATION_CHANGED"
        private const val ON_PLAYBACK_COMPLETED = "ON_PLAYBACK_COMPLETED"
    }

    private val mPlayerController: PlayerController =
            MediaPlayerHolder(reactContext).apply { setPlaybackListener(this@RNSimplePlayerModule) }

    @ReactMethod
    fun loadAudioFile(filePath: String, preventLoudMusic: Boolean, promise: Promise) {
        promise.resolve(mPlayerController.loadMedia(filePath, preventLoudMusic, WeakReference(currentActivity)))
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
        val data = Arguments.createMap().apply {
            putInt("state", state.toInt())
        }
        sendEvent(ON_STATE_CHANGED, data)
    }

    override fun onPositionChanged(position: Int) {
        val data = Arguments.createMap().apply {
            putInt("position", position)
        }
        sendEvent(ON_POSITION_CHANGED, data)
    }

    override fun onDurationChanged(duration: Int) {
        val data = Arguments.createMap().apply {
            putInt("duration", duration)
        }
        sendEvent(ON_DURATION_CHANGED, data)
    }

    override fun onPlaybackCompleted() {
        sendEvent(ON_PLAYBACK_COMPLETED)
    }

    private fun sendEvent(eventName: String, data: WritableMap = Arguments.createMap()) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, data)
    }

    override fun getConstants(): MutableMap<String, Any> {
        return mutableMapOf(
                stateToString(PlaybackListener.States.INVALID) to PlaybackListener.States.INVALID,
                stateToString(PlaybackListener.States.PLAYING) to PlaybackListener.States.PLAYING,
                stateToString(PlaybackListener.States.PAUSED) to PlaybackListener.States.PAUSED,
                stateToString(PlaybackListener.States.RESET) to PlaybackListener.States.RESET,
                stateToString(PlaybackListener.States.COMPLETED) to PlaybackListener.States.COMPLETED,
                ON_STATE_CHANGED to ON_STATE_CHANGED,
                ON_DURATION_CHANGED to ON_DURATION_CHANGED,
                ON_POSITION_CHANGED to ON_POSITION_CHANGED,
                ON_PLAYBACK_COMPLETED to ON_PLAYBACK_COMPLETED
        )
    }

    override fun getName() = "RNSimplePlayer"
}