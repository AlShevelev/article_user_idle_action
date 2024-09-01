package com.example.shevelev.user_idle

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {
    private val userIdleActionSetup by lazy { UserIdleActionSetup(this.applicationContext, lifecycleScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userIdleActionSetup.setupAlarm()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIdleActionSetup.restartAlarm()
    }
}
