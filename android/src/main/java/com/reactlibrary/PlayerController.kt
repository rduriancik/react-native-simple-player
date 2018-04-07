package com.reactlibrary

interface PlayerController {
    fun loadMedia(filePath: String): Boolean
    fun release()
    fun isPlaying(): Boolean
    fun play()
    fun pause()
    fun reset()
    fun seekTo(position: Int)
    fun setPlaybackListener(playbackListener: PlaybackListener)
}