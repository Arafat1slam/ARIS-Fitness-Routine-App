package com.example.arisfitness.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.view.HapticFeedbackConstants
import android.view.View
import com.example.arisfitness.R

object SoundManager {
    private const val PREFS_NAME = "aris_sound_prefs"
    private const val KEY_SOUND_ENABLED = "key_sound_enabled"
    private const val KEY_HAPTIC_ENABLED = "key_haptic_enabled"

    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = 0
    private var completeSoundId: Int = 0
    private var isInitialized: Boolean = false

    private fun getPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isSoundEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setSoundEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }

    fun isHapticEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_HAPTIC_ENABLED, true)
    }

    fun setHapticEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_HAPTIC_ENABLED, enabled).apply()
    }

    @Volatile private var clickLoaded = false
    @Volatile private var completeLoaded = false

    @Synchronized
    fun initialize(context: Context) {
        if (isInitialized) return
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val pool = SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build()

            pool.setOnLoadCompleteListener { _, sampleId, status ->
                if (status == 0) {
                    if (sampleId == clickSoundId) clickLoaded = true
                    if (sampleId == completeSoundId) completeLoaded = true
                }
            }

            clickSoundId = pool.load(context.applicationContext, R.raw.click_sound, 1)
            completeSoundId = pool.load(context.applicationContext, R.raw.complete_sound, 1)
            soundPool = pool
            isInitialized = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playClick(context: Context) {
        try {
            if (!isSoundEnabled(context)) return
            if (!isInitialized) initialize(context)
            if (clickLoaded) {
                soundPool?.play(clickSoundId, 0.85f, 0.85f, 1, 0, 1.0f)
            }
        } catch (e: Exception) {
            // Ignore sound playback exceptions silently
        }
    }

    fun playComplete(context: Context) {
        try {
            if (!isSoundEnabled(context)) return
            if (!isInitialized) initialize(context)
            if (completeLoaded) {
                soundPool?.play(completeSoundId, 1.0f, 1.0f, 2, 0, 1.0f)
            }
        } catch (e: Exception) {
            // Ignore sound playback exceptions silently
        }
    }

    fun performHaptic(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}
