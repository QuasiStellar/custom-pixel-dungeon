package com.qsr.customspd.android

import android.R.attr.label
import android.R.attr.text
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.qsr.customspd.modding.ModManager
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess


class ExceptionHandler(private val context: Activity) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        val errorReport = """
            >>>>>>>>>> CAUSE OF ERROR
            ${stackTrace
                .toString()
                .substringAfter("Caused by: ")
                .substringBefore("\n\tat ")
                .split('\n')
                .joinToString("\n            ")}
        
            >>>>>>>>>> STACKTRACE
            ${stackTrace
                .toString()
                .split('\n')
                .joinToString("\n            ")}
        
            >>>>>>>>>> ACTIVE MODS
            ${ModManager
                .getEnabledMods()
                .joinToString("\n            ") { "${it.info.name} - version ${it.info.version}" }}
        
            >>>>>>>>>> SOFTWARE
            Version: ${BuildConfig.VERSION_NAME}
            Internal version: ${BuildConfig.VERSION_CODE}
            Type: ${BuildConfig.BUILD_TYPE}
        
            >>>>>>>>>> HARDWARE
            Brand: ${Build.BRAND}
            Device: ${Build.DEVICE}
            Model: ${Build.MODEL}
            Product: ${Build.PRODUCT}
            
            >>>>>>>>>> FIRMWARE
            SDK: ${Build.VERSION.SDK_INT}
            Release: ${Build.VERSION.RELEASE}
            Incremental: ${Build.VERSION.INCREMENTAL}
        """.trimIndent()

        val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied crash report to the clipboard", errorReport)
        clipboard.setPrimaryClip(clip)

        val intent = Intent(context, CrashActivity::class.java)
        intent.putExtra("error", errorReport)
        context.startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(10)
    }
}
