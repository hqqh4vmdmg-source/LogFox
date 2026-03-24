package com.f0x1d.logfox.core.copy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import com.f0x1d.logfox.core.context.toast
import com.f0x1d.logfox.feature.strings.Strings

fun Context.copyText(text: String) = runCatching {
    val clipboard = getSystemService<ClipboardManager>()!!
    clipboard.setPrimaryClip(ClipData.newPlainText("LogFox", text))
}.onFailure { th ->
    toast(getString(Strings.error, th.localizedMessage))
}

