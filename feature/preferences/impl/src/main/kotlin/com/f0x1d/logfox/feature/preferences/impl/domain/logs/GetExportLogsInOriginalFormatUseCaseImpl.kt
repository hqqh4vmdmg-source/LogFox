package com.f0x1d.logfox.feature.preferences.impl.domain.logs

import com.f0x1d.logfox.feature.preferences.api.data.LogsSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetExportLogsInOriginalFormatUseCase
import javax.inject.Inject

internal class GetExportLogsInOriginalFormatUseCaseImpl @Inject constructor(
    private val logsSettingsRepository: LogsSettingsRepository,
) : GetExportLogsInOriginalFormatUseCase {

    override fun invoke(): Boolean = logsSettingsRepository.exportLogsInOriginalFormat().value
}
