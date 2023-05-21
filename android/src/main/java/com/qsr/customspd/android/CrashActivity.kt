package com.qsr.customspd.android

import android.app.Activity
import android.os.Bundle
import android.widget.TextView


class CrashActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crash_view)
        findViewById<TextView>(R.id.error).text = intent.getStringExtra("error")
    }
}
