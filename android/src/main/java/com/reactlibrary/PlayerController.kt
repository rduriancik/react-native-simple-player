package com.reactlibrary

import android.app.Activity
import java.lang.ref.WeakReference

interface PlayerController {
    fun loadMedia(filePath: String, preventLoudMusic: Boolean, activity: WeakReference<Activity?>?): Boolean
    fun release()
    fun isPlaying(): Boolean
    fun play()
    fun pause()
    fun reset()
    fun seekTo(position: Int)
    fun setPlaybackListener(playbackListener: PlaybackListener)
}