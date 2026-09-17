package com.example.arisfitness.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.arisfitness.MainActivity

object NotificationHelper {

    const val CHANNEL_WAKE = "aris_channel_wake"
    const val CHANNEL_MEALS = "aris_channel_meals"
    const val CHANNEL_WORKOUTS = "aris_channel_workouts"
    const val CHANNEL_REMINDERS = "aris_channel_reminders"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            // 1. Wake Channel
            val wakeChannel = NotificationChannel(
                CHANNEL_WAKE,
                "Wake Schedule Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Exact wake-up protocol alarms"
                enableVibration(true)
                setSound(alarmSound, audioAttributes)
            }

            // 2. Meals Channel
            val mealsChannel = NotificationChannel(
                CHANNEL_MEALS,
                "Meal & Nutrition Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Scheduled meal and calorie intake notifications"
                enableVibration(true)
                setSound(notificationSound, audioAttributes)
            }

            // 3. Workouts Channel
            val workoutsChannel = NotificationChannel(
                CHANNEL_WORKOUTS,
                "Workout & Training Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily workout start times and exercise routines"
                enableVibration(true)
                setSound(notificationSound, audioAttributes)
            }

            // 4. Reminders Channel
            val remindersChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Routine & Recovery Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Hydration, joint mobility, and evening sleep protocols"
            }

            notificationManager.createNotificationChannels(
                listOf(wakeChannel, mealsChannel, workoutsChannel, remindersChannel)
            )
        }
    }

    fun showNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        title: String,
        message: String,
        itemId: String? = null
    ) {
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val tapPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(tapPendingIntent)

        // Action button to mark item done if itemId provided
        if (!itemId.isNullOrBlank()) {
            val markDoneIntent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.ACTION_MARK_ITEM_DONE
                putExtra(AlarmReceiver.EXTRA_ITEM_ID, itemId)
            }
            val markDonePending = PendingIntent.getBroadcast(
                context,
                notificationId + 10000,
                markDoneIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(android.R.drawable.checkbox_on_background, "Mark Done", markDonePending)
        }

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Handled when notification permission is denied
        }
    }
}
