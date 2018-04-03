package com.reactlibrary

interface PlayerController {
    fun loadMedia(resourceId: Int)
    fun release()
    fun isPlaying(): Boolean
    fun play()
    fun pause()
    fun reset()
    fun seekTo(position: Int)
    fun setPlaybackListener(playbackListener: PlaybackListener)
}