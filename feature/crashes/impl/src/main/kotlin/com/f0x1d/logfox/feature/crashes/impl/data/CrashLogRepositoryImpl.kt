package com.f0x1d.logfox.feature.crashes.impl.data

import com.f0x1d.logfox.feature.crashes.api.data.CrashLogRepository
import com.f0x1d.logfox.feature.crashes.api.model.AppCrash
import com.f0x1d.logfox.feature.logging.api.data.LogLineFormatterRepository
import com.f0x1d.logfox.feature.logging.api.data.LogLineParser
import javax.inject.Inject

internal class CrashLogRepositoryImpl @Inject constructor(
    private val logLineParser: LogLineParser,
    private val logLineFormatterRepository: LogLineFormatterRepository,
) : CrashLogRepository {
    override fun readCrashLog(appCrash: AppCrash): List<String> =
        appCrash.logFile?.readLines()?.mapIndexed { index, line ->
            logLineParser.parse(index.toLong(), line)
                ?.let(logLineFormatterRepository::formatOriginal)
                ?: line
        }.orEmpty()
}
