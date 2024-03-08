package org.totschnig.myexpenses.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import org.totschnig.myexpenses.MyApplication
import org.totschnig.myexpenses.preference.PrefKey
import kotlin.system.exitProcess
import org.totschnig.myexpenses.jacocoInstrumentKt
.SMSInstrumentedReceiver

@SuppressLint("CustomSplashScreen")
class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as? MyApplication)?.also {
            startActivity(Intent(this, if ((application as MyApplication).appComponent.prefHandler()
                    .getInt(PrefKey.CURRENT_VERSION, -1) == -1
            ) OnboardingActivity::class.java else MyExpenses::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            })
        } ?: run {
            Handler().post { exitProcess(0) }
        }

        // register receiver
        val receiver = SMSInstrumentedReceiver()
        val filter = IntentFilter("edu.gatech.m3.emma.COLLECT_COVERAGE")
        registerReceiver(receiver, filter)

        finish()
    }
}