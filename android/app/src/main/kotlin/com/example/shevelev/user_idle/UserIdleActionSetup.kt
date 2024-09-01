package com.example.shevelev.user_idle

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class UserIdleActionSetup(
    private val appContext: Context,
    private val scope: CoroutineScope,
) {
    private var debounceJob: Job? = null

    fun restartAlarm() {
        Log.d(GlobalConstants.DEBUG_TAG, "Android: The times is restarted")

        debounceJob?.cancel()

        debounceJob = scope.launch {
            delay(AUTO_LOGOUT_SETUP_DELAY)

            setupAlarm()
        }
    }

    fun setupAlarm() {
        Log.d(GlobalConstants.DEBUG_TAG, "Android: The timer is set up")

        val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            AUTO_LOGOUT_REQUEST_ID,
            Intent(appContext, UserIdleActionReceiver::class.java).apply {
                action = USER_IDLE_ACTION
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT, // flags
        )
        alarmManager.cancel(pendingIntent)

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + AUTO_LOGOUT_FIRE_INTERVAL,
            pendingIntent,
        )
    }

    companion object {
        private const val AUTO_LOGOUT_FIRE_INTERVAL = 20 * 1_000L // 20 seconds

        private const val AUTO_LOGOUT_REQUEST_ID = 21_062

        private const val AUTO_LOGOUT_SETUP_DELAY = 3_000L // 3 seconds

        const val USER_IDLE_ACTION = "com.example.shevelev.user_idle.USER_IDLE_ACTION"
    }
}
