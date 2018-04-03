package com.reactlibrary

import android.support.annotation.IntDef

interface PlaybackListener {
    companion object States {
        const val INVALID = -1L
        const val PLAYING = 0L
        const val PAUSED = 1L
        const val RESET = 2L
        const val COMPLETED = 3L
    }

    @IntDef(INVALID, PLAYING, PAUSED, RESET, COMPLETED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class State

    fun stateToString(@State state: Long) = when (state) {
        INVALID -> "INVALID"
        PLAYING -> "PLAYING"
        PAUSED -> "PAUSED"
        RESET -> "RESET"
        COMPLETED -> "COMPLETED"
        else -> "N/A"
    }

    fun onStateChanged(@State state: Long)
    fun onPositionChanged(position: Int)
    fun onDurationChanged(duration: Int)
    fun onPlaybackCompleted()

}