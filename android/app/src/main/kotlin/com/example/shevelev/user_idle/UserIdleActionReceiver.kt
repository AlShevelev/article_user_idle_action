package com.example.shevelev.user_idle

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.shevelev.user_idle.GlobalConstants.DEBUG_TAG
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * To call this receiver for debug purpose you should use the next command:
 * adb shell am broadcast -a com.example.shevelev.user_idle.USER_IDLE_DEBUG_ACTION -p com.example.shevelev.user_idle
 */
class UserIdleActionReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!validateActions(intent)) {
            return
        }

        try {
            context?.applicationContext?.let { appContext ->
                val pathToBundle = FlutterInjector.instance().flutterLoader().findAppBundlePath()

                FlutterEngine(appContext).dartExecutor.executeDartEntrypoint(
                    DartExecutor.DartEntrypoint(
                        pathToBundle,
                        "userIdleActionEntryPoint",
                    )
                )

                // Ideally we should call FlutterEngine.destroy() method here - to cleanup
                // the engine resources etc.
                // But, due to some wierd reason the method interrupts an execution of our code.
                // And I dunno why.
                // It seems to me that DartExecutor.executeDartEntrypoint works asynchronously,
                // but, according the docs (https://api.flutter.dev/javadoc/io/flutter/embedding/engine/dart/DartExecutor.html)
                // it doesn't ("Once started, a DartExecutor cannot be stopped. The associated
                // Dart code will execute until it completes, or until the FlutterEngine that owns
                // this DartExecutor is destroyed.")
            }
        } catch (ex: Exception) {
            Log.e(DEBUG_TAG, ex.message, ex)
        }
    }

    private fun validateActions(intent: Intent?): Boolean = when (intent?.action) {
        UserIdleActionSetup.USER_IDLE_ACTION -> true

        DEBUG_ACTION -> BuildConfig.DEBUG

        else -> false
    }

    companion object {
        private const val DEBUG_ACTION = "com.example.shevelev.user_idle.USER_IDLE_DEBUG_ACTION"
    }
}
