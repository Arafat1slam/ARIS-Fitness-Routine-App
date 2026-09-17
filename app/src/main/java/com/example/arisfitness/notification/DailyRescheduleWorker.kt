package com.example.arisfitness.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.arisfitness.data.local.ArisDatabaseHelper
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.repository.RoutineDataGenerator
import com.example.arisfitness.engine.CalorieEngine
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailyRescheduleWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val dbHelper = ArisDatabaseHelper(applicationContext)
        val profile = dbHelper.getUserProfile() ?: return Result.success()

        if (!profile.isConfigured) return Result.success()

        val rawRoutine = dbHelper.getCachedRoutine(profile.selectedCharacterId, profile.currentDayIndex)
            ?: RoutineDataGenerator.generateDay(profile.selectedCharacterId, profile.currentDayIndex)

        val character = CharacterProfile.getById(profile.selectedCharacterId)
        val scaleFactor = CalorieEngine.calculateScaleFactor(profile.tdee, character.referenceTdee)
        val scaledRoutine = CalorieEngine.scaleRoutineEntry(rawRoutine, scaleFactor)

        AlarmScheduler.scheduleRoutineAlarms(applicationContext, scaledRoutine)

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "aris_daily_reschedule_work"

        fun scheduleDailyMidnightJob(context: Context) {
            val now = Calendar.getInstance()
            val targetMidnight = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 5) // 00:05 AM
                set(Calendar.SECOND, 0)
            }
            val initialDelayMs = targetMidnight.timeInMillis - now.timeInMillis

            val periodicRequest = PeriodicWorkRequestBuilder<DailyRescheduleWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicRequest
            )
        }
    }
}
