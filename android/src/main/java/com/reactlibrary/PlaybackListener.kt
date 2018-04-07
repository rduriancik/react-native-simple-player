package com.reactlibrary

import android.support.annotation.IntDef

interface PlaybackListener {
    companion object States {
        const val INVALID = 0L
        const val PLAYING = 1L
        const val PAUSED = 2L
        const val RESET = 3L
        const val COMPLETED = 4L
    }

    @IntDef(States.INVALID, States.PLAYING, States.PAUSED, States.RESET, States.COMPLETED)
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