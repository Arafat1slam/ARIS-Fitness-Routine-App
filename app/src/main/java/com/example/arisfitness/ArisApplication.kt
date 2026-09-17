package com.example.arisfitness

import android.app.Application
import com.example.arisfitness.data.repository.ArisRepository
import com.example.arisfitness.notification.DailyRescheduleWorker
import com.example.arisfitness.notification.NotificationHelper

class ArisApplication : Application() {

    lateinit var repository: ArisRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        repository = ArisRepository(this)

        // Create high priority notification channels
        NotificationHelper.createNotificationChannels(this)

        // Queue periodic midnight alarm rescheduling
        DailyRescheduleWorker.scheduleDailyMidnightJob(this)
    }

    companion object {
        lateinit var instance: ArisApplication
            private set
    }
}
