package com.f0x1d.logfox.feature.crashes.impl.data

import com.f0x1d.logfox.feature.crashes.api.data.CrashDetectingRepository
import com.f0x1d.logfox.feature.logging.api.model.LogLine
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CrashDetectingRepositoryImpl @Inject constructor(
    crashDataSources: Set<@JvmSuppressWildcards CrashDataSource>,
) : CrashDetectingRepository {

    private val sourcesWithMutexes = crashDataSources.map { it to Mutex() }

    override suspend fun processLogLine(line: LogLine) {
        coroutineScope {
            sourcesWithMutexes.forEach { (dataSource, mutex) ->
                launch {
                    mutex.withLock {
                        dataSource.process(line)
                    }
                }
            }
        }
    }
}
