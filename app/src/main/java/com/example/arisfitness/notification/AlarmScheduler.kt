package com.example.arisfitness.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.arisfitness.data.model.DailyRoutineEntry
import java.util.Calendar

object AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleRoutineAlarms(context: Context, routine: DailyRoutineEntry) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Check exact alarm permission on Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }

        // 1. Wake Time Alarm
        scheduleSingleAlarm(
            context = context,
            alarmManager = alarmManager,
            timeStr = routine.wakeTime,
            requestCode = 1001,
            channelId = NotificationHelper.CHANNEL_WAKE,
            title = "Wake Up Protocol (${routine.wakeTime})",
            message = "Time to conquer Day ${routine.dayIndex}. Hydrate and initiate your routine.",
            itemId = "wake"
        )

        // 2. Meal Alarms
        routine.meals.forEachIndexed { index, meal ->
            scheduleSingleAlarm(
                context = context,
                alarmManager = alarmManager,
                timeStr = meal.time,
                requestCode = 2000 + index,
                channelId = NotificationHelper.CHANNEL_MEALS,
                title = "Nutrition Protocol: ${meal.name}",
                message = "Target: ${meal.baseCalories} kcal | ${meal.proteinG}g Protein | ${meal.carbsG}g Carbs",
                itemId = meal.mealId
            )
        }

        // 3. Workout Alarms
        routine.workouts.forEachIndexed { index, workout ->
            scheduleSingleAlarm(
                context = context,
                alarmManager = alarmManager,
                timeStr = workout.time,
                requestCode = 3000 + index,
                channelId = NotificationHelper.CHANNEL_WORKOUTS,
                title = "Training Session: ${workout.title}",
                message = "Duration: ${workout.durationMin} min | ${workout.category}. Lock in.",
                itemId = workout.workoutId
            )
        }

        // 4. Reminders
        routine.reminders.forEachIndexed { index, reminder ->
            scheduleSingleAlarm(
                context = context,
                alarmManager = alarmManager,
                timeStr = reminder.time,
                requestCode = 4000 + index,
                channelId = NotificationHelper.CHANNEL_REMINDERS,
                title = "Daily Protocol Reminder",
                message = reminder.message,
                itemId = reminder.reminderId
            )
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleSingleAlarm(
        context: Context,
        alarmManager: AlarmManager,
        timeStr: String,
        requestCode: Int,
        channelId: String,
        title: String,
        message: String,
        itemId: String
    ) {
        val parts = timeStr.split(":")
        if (parts.size < 2) return

        val hour = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the time has already passed today, do not schedule for the past
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ROUTINE_ALARM
            putExtra(AlarmReceiver.EXTRA_CHANNEL_ID, channelId)
            putExtra(AlarmReceiver.EXTRA_TITLE, title)
            putExtra(AlarmReceiver.EXTRA_MESSAGE, message)
            putExtra(AlarmReceiver.EXTRA_NOTIFICATION_ID, requestCode)
            putExtra(AlarmReceiver.EXTRA_ITEM_ID, itemId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (_: SecurityException) {
            // Permission not granted
        }
    }

    fun cancelAllAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Cancel base IDs
        for (id in 1001..4050) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.ACTION_ROUTINE_ALARM
            }
            val pi = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pi != null) {
                alarmManager.cancel(pi)
                pi.cancel()
            }
        }
    }
}
