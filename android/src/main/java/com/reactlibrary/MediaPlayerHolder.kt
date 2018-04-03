package com.reactlibrary

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MediaPlayerHolder(private val context: Context) : PlayerController {
    companion object {
        private const val TAG = "MediaPlayerHolder"
        private const val PLAYBACK_POSITION_REFRESH_INTERVAL: Long = 1000
    }

    private var mMediaPlayer: MediaPlayer? = null
    private var mPlaybackListener: PlaybackListener? = null
    private var mResourceId: Int = 0
    private var mExecutor: ScheduledExecutorService? = null
    private var mPositionUpdateTask: Runnable? = null

    private fun initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer().apply {
                setOnCompletionListener {
                    stopUpdatingProgressCallback(true)
                    mPlaybackListener?.let {
                        it.onStateChanged(PlaybackListener.States.COMPLETED)
                        it.onPlaybackCompleted()
                    }
                }
            }
        }
    }

    override fun loadMedia(resourceId: Int) {
        mResourceId = resourceId

        initMediaPlayer()


        initProgressCallback()
    }

    override fun release() {
        mMediaPlayer?.let {
            Log.d(TAG, "Release media player")
            it.release()
            mMediaPlayer = null
        }
    }

    override fun isPlaying(): Boolean = mMediaPlayer?.isPlaying ?: false

    override fun play() {
        mMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                mPlaybackListener?.onStateChanged(PlaybackListener.States.PLAYING)
                startUpdatingProgressCallback()
            }
        }
    }

    override fun pause() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                mPlaybackListener?.onStateChanged(PlaybackListener.States.PAUSED)
            }
        }
    }

    override fun reset() {
        mMediaPlayer?.let {
            it.reset()
            loadMedia(mResourceId)
            mPlaybackListener?.onStateChanged(PlaybackListener.States.RESET)
            stopUpdatingProgressCallback(true)
        }
    }

    override fun seekTo(position: Int) {
        mMediaPlayer?.seekTo(position)
    }

    override fun setPlaybackListener(playbackListener: PlaybackListener) {
        this.mPlaybackListener = playbackListener
    }

    private fun initProgressCallback() {
        mMediaPlayer?.let {
            val duration = it.duration
            mPlaybackListener?.let {
                it.onDurationChanged(duration)
                it.onPositionChanged(0)
            }
        }
    }

    private fun updateProgressCallback() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                mPlaybackListener?.onPositionChanged(it.currentPosition)
            }
        }
    }

    private fun startUpdatingProgressCallback() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor()
        }

        if (mPositionUpdateTask == null) {
            mPositionUpdateTask = Runnable { updateProgressCallback() }
        }

        mExecutor!!.scheduleAtFixedRate(
                mPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL,
                TimeUnit.MILLISECONDS
        )
    }

    private fun stopUpdatingProgressCallback(resetPlaybackPosition: Boolean) {
        mExecutor?.let {
            it.shutdownNow()
            mExecutor = null
            mPositionUpdateTask = null
            if (resetPlaybackPosition) {
                mPlaybackListener?.onPositionChanged(0)
            }
        }
    }
}