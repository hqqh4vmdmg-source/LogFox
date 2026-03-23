package com.f0x1d.logfox.feature.preferences.api.domain.logs

import kotlinx.coroutines.flow.Flow

interface GetExportLogsInOriginalFormatFlowUseCase {
    operator fun invoke(): Flow<Boolean>
}
