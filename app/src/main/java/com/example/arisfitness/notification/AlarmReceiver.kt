package com.example.arisfitness.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.arisfitness.data.repository.ArisRepository

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_ROUTINE_ALARM = "com.example.arisfitness.ACTION_ROUTINE_ALARM"
        const val ACTION_MARK_ITEM_DONE = "com.example.arisfitness.ACTION_MARK_ITEM_DONE"

        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val EXTRA_ITEM_ID = "extra_item_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_ROUTINE_ALARM -> {
                val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: NotificationHelper.CHANNEL_REMINDERS
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "ARIS Fitness Routine"
                val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Time for your scheduled protocol."
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 101)
                val itemId = intent.getStringExtra(EXTRA_ITEM_ID)

                NotificationHelper.showNotification(
                    context = context,
                    notificationId = notificationId,
                    channelId = channelId,
                    title = title,
                    message = message,
                    itemId = itemId
                )
            }
            ACTION_MARK_ITEM_DONE -> {
                val itemId = intent.getStringExtra(EXTRA_ITEM_ID)
                if (!itemId.isNullOrBlank()) {
                    val repo = ArisRepository(context)
                    val profile = repo.userProfile.value
                    if (profile != null) {
                        repo.toggleChecklistItem(itemId, profile.selectedCharacterId, profile.currentDayIndex)
                    }
                }
                // Dismiss notification
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 101)
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }
}
