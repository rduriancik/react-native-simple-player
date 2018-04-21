package com.reactlibrary

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.lang.ref.WeakReference
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
    private var mFilePath: String = ""
    private var mPreventLoudMusic: Boolean = false
    private var mActivity: WeakReference<Activity?>? = null
    private var mExecutor: ScheduledExecutorService? = null
    private var mPositionUpdateTask: Runnable? = null

    private val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var mIsAudioFocused = false
    private var mResumeOnFocusGain = false
    private val mFocusLock: Any = Any()
    private var mAudioFocusRequest: AudioFocusRequest? = null
    private val mOnAudioFocusChangedListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (mResumeOnFocusGain) {
                    Log.d(TAG, "AudioFocus gained")
                    synchronized(mFocusLock) {
                        mResumeOnFocusGain = false
                    }
                    play()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                Log.d(TAG, "AudioFocus loss")
                synchronized(mFocusLock) {
                    mResumeOnFocusGain = false
                }
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(TAG, "AudioFocus loss transient")
                synchronized(mFocusLock) {
                    mResumeOnFocusGain = true
                }
                pause()
            }
        }
    }


    private fun initMediaPlayer(preventLoudMusic: Boolean) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer().apply {
                setOnCompletionListener {
                    stopUpdatingProgressCallback(true)
                    releaseAudioFocus()
                    mPlaybackListener?.let {
                        it.onStateChanged(PlaybackListener.States.COMPLETED)
                        it.onPlaybackCompleted()
                    }
                }

                Log.d(TAG, "Prevent loud music? $preventLoudMusic")
                if (preventLoudMusic) {
//                    initAudioManager()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val audioAttributes = AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        setAudioAttributes(audioAttributes.build())
                    } else {
                        setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
                    }
                }
            }
        }
    }

    private fun initAudioManager() {
//        val audiomgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        audiomgr.setStreamVolume(AudioManager.MODE_IN_CALL, audiomgr.getStreamMaxVolume(AudioManager.MODE_IN_CALL) / 2, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
//        audiomgr.mode = AudioManager.MODE_IN_CALL
//        audiomgr.isSpeakerphoneOn = false
    }

    override fun loadMedia(filePath: String, preventLoudMusic: Boolean, activity: WeakReference<Activity?>?): Boolean {
        mFilePath = filePath
        mPreventLoudMusic = preventLoudMusic
        mActivity = activity
        if (mPreventLoudMusic) {
            activity?.get()?.volumeControlStream = AudioManager.STREAM_VOICE_CALL
        }
        initMediaPlayer(preventLoudMusic)
        val file = File(filePath)

        try {
            FileInputStream(file).use {
                mMediaPlayer?.setDataSource(it.fd)
            }
            mMediaPlayer?.prepare()
            Log.d(TAG, "Media player initialized")
        } catch (e: Exception) {
            Log.e(TAG, "File not found. Path $filePath")
            e.printStackTrace()
            return false
        }

        initProgressCallback()
        return true
    }

    override fun release() {
        mMediaPlayer?.let {
            Log.d(TAG, "Release media player")
            it.release()
            if (!mIsAudioFocused) releaseAudioFocus()
            mMediaPlayer = null
        }
    }

    override fun isPlaying(): Boolean = mMediaPlayer?.isPlaying ?: false

    override fun play() {
        mMediaPlayer?.let {
            if (!it.isPlaying) {
                requestAudioFocus()
                if (mIsAudioFocused) {
                    it.start()
                    mPlaybackListener?.onStateChanged(PlaybackListener.States.PLAYING)
                    startUpdatingProgressCallback()
                }
            }
        }
    }

    override fun pause() {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                Log.d(TAG, "Pause media player")
                it.pause()
                mPlaybackListener?.onStateChanged(PlaybackListener.States.PAUSED)
            }
        }
    }

    override fun reset() {
        mMediaPlayer?.let {
            Log.d(TAG, "Reset media player")
            it.reset()
            loadMedia(mFilePath, mPreventLoudMusic, mActivity)
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

    private fun requestAudioFocus() {
        Log.d(TAG, "Request audio focus")
        val result: Int

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = if (mPreventLoudMusic) {
                AudioAttributes.Builder().apply {
                    setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                }.build()
            } else {
                AudioAttributes.Builder().apply {
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                }.build()
            }

            mAudioFocusRequest =
                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).apply {
                        setAudioAttributes(audioAttributes)
                        setAcceptsDelayedFocusGain(false)
                        setOnAudioFocusChangeListener(mOnAudioFocusChangedListener)
                    }.build()

            result = mAudioManager.requestAudioFocus(mAudioFocusRequest)
        } else {
            val streamType = if (mPreventLoudMusic) AudioManager.STREAM_VOICE_CALL else AudioManager.STREAM_MUSIC;
            result = mAudioManager.requestAudioFocus(mOnAudioFocusChangedListener, streamType, AudioManager.AUDIOFOCUS_GAIN)
        }

        synchronized(mFocusLock) {
            mIsAudioFocused = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            Log.d(TAG, "Audio focus granted? $mIsAudioFocused")
        }

    }

    private fun releaseAudioFocus() {
        Log.d(TAG, "Release media player")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest)
            mAudioFocusRequest = null
        } else {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangedListener)
        }

        mIsAudioFocused = false
    }
}