package com.f0x1d.logfox.feature.filters.api.model

import com.f0x1d.logfox.feature.logging.api.model.LogLine

fun LogLine.suits(filters: List<UserFilter>) = listOf(this).filterAndSearch(filters).isNotEmpty()

fun List<LogLine>.filterAndSearch(
    filters: List<UserFilter>,
    query: String? = null,
    caseSensitive: Boolean = true,
): List<LogLine> {
    val filtered = filterByExtendedFilters(filters)
    return if (query == null) {
        filtered
    } else {
        filtered.filter { logLine ->
            logLine.tag.contains(query, ignoreCase = !caseSensitive) ||
                logLine.content.contains(query, ignoreCase = !caseSensitive)
        }
    }
}

private fun List<LogLine>.filterByExtendedFilters(filters: List<UserFilter>): List<LogLine> {
    if (filters.isEmpty()) return this

    val includingFilters = filters.filter { it.including }
    val excludingFilters = filters.filter { !it.including }

    return filter { logLine ->
        val shouldExclude = excludingFilters.any { it.lineSuits(logLine) }
        if (shouldExclude) return@filter false

        includingFilters.isEmpty() || includingFilters.any { it.lineSuits(logLine) }
    }
}

private fun UserFilter.lineSuits(logLine: LogLine) =
    (allowedLevels.isEmpty() || allowedLevels.contains(logLine.level)) &&
        uid.equalsOrTrueIfNull(logLine.uid) &&
        pid.equalsOrTrueIfNull(logLine.pid) &&
        tid.equalsOrTrueIfNull(logLine.tid) &&
        packageName.equalsOrTrueIfNull(logLine.packageName.orEmpty()) &&
        tag.equalsOrTrueIfNull(logLine.tag) &&
        content.containsOrTrueIfNull(logLine.content)

private fun String?.equalsOrTrueIfNull(other: String) = this == null || other == this
private fun String?.containsOrTrueIfNull(other: String) = this == null || other.contains(this)
