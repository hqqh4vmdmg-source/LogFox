package com.f0x1d.logfox.feature.preferences.impl.domain.logs

import com.f0x1d.logfox.feature.preferences.api.data.LogsSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetExportLogsInOriginalFormatFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetExportLogsInOriginalFormatFlowUseCaseImpl @Inject constructor(
    private val logsSettingsRepository: LogsSettingsRepository,
) : GetExportLogsInOriginalFormatFlowUseCase {

    override fun invoke(): Flow<Boolean> = logsSettingsRepository.exportLogsInOriginalFormat()
}
