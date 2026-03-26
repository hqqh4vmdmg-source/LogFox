package com.f0x1d.logfox.core.context

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.f0x1d.logfox.core.compat.shouldRequestNotificationsPermission
import com.f0x1d.logfox.feature.strings.Strings
import java.io.File
import kotlin.system.exitProcess

val Context.hasPermissionToReadLogs: Boolean
    get() = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS) ==
        PackageManager.PERMISSION_GRANTED

val Context.notificationManagerCompat get() = NotificationManagerCompat.from(this)
val Context.notificationManager: NotificationManager get() = getSystemService()!!
val Context.activityManager: ActivityManager get() = getSystemService()!!
val Context.inputMethodManager: InputMethodManager get() = getSystemService()!!

fun Context.hardRestartApp() {
    activityManager.appTasks.forEach { it.finishAndRemoveTask() }
    startActivity(packageManager.getLaunchIntentForPackage(packageName))
    exitProcess(0)
}

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Context.toast(text: Int) = toast(getString(text))

fun Context.shareIntent(text: String) = baseShareIntent {
    putExtra(Intent.EXTRA_TEXT, text)
    type = "text/plain"
}

fun Context.shareFileIntent(file: File) = baseShareIntent {
    putExtra(Intent.EXTRA_STREAM, file.asUri(this@shareFileIntent))
    type = "text/plain"
}

private fun Context.baseShareIntent(block: Intent.() -> Unit) {
    runCatching {
        val intent = Intent(Intent.ACTION_SEND).apply(block)
        startActivity(Intent.createChooser(intent, getString(Strings.share)))
    }.onFailure {
        toast(Strings.too_big_log)
    }
}

fun Context.catchingNotNumber(block: () -> Unit) = try {
    block()
} catch (_: NumberFormatException) {
    toast(Strings.this_is_not_a_number)
}

inline fun <reified T> Context.sendService(action: String) =
    startService(Intent(this, T::class.java).setAction(action))

@SuppressLint("InlinedApi")
fun Context.hasNotificationsPermission() = if (shouldRequestNotificationsPermission) {
    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
} else {
    true
}

fun Context.doIfNotificationsAllowed(block: NotificationManagerCompat.() -> Unit) {
    if (hasNotificationsPermission()) block(notificationManagerCompat)
}

val Context.isHorizontalOrientation: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

