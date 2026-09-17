package com.example.arisfitness.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.arisfitness.data.repository.ArisRepository

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            val repo = ArisRepository(context)
            val profile = repo.userProfile.value
            if (profile != null && profile.isConfigured) {
                val routine = repo.getDailyRoutine(
                    profile.selectedCharacterId,
                    profile.currentDayIndex,
                    profile.tdee
                )
                AlarmScheduler.scheduleRoutineAlarms(context, routine)
            }
        }
    }
}
