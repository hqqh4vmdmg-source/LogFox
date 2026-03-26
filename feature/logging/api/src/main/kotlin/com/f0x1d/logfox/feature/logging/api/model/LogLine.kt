package com.f0x1d.logfox.feature.logging.api.model

import com.f0x1d.logfox.core.recycler.Identifiable
import java.util.Date

data class LogLine(
    override val id: Long,
    val dateAndTime: Long,
    val uid: String,
    val pid: String,
    val tid: String,
    val packageName: String?,
    val level: LogLevel,
    val tag: String,
    val content: String,
    val originalContent: String,
) : Identifiable {

    fun formatOriginal(
        values: ShowLogValues,
        formatDate: (Long) -> String = { Date(it).toLocaleString() },
        formatTime: (Long) -> String = { Date(it).toLocaleString() },
    ): String = buildString {
        with(values) {
            if (date) { append(formatDate(dateAndTime)); append(' ') }
            if (time) { append(formatTime(dateAndTime)); append(' ') }
            if (uid) { append(this@LogLine.uid); append(' ') }
            if (pid) { append(this@LogLine.pid); append(' ') }
            if (tid) { append(this@LogLine.tid); append(' ') }
            if (packageName && this@LogLine.packageName != null) {
                append(this@LogLine.packageName)
                append(' ')
            }
            if (tag) {
                append(this@LogLine.tag)
                if (content) append(": ")
            }
            if (content) append(this@LogLine.content)
        }
    }
}
