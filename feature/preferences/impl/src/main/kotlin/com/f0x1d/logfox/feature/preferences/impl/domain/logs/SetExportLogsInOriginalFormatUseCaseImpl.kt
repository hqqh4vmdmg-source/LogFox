package com.f0x1d.logfox.feature.preferences.impl.domain.logs

import com.f0x1d.logfox.feature.preferences.api.data.LogsSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetExportLogsInOriginalFormatUseCase
import javax.inject.Inject

internal class SetExportLogsInOriginalFormatUseCaseImpl @Inject constructor(
    private val logsSettingsRepository: LogsSettingsRepository,
) : SetExportLogsInOriginalFormatUseCase {

    override fun invoke(inOriginalFormat: Boolean) {
        logsSettingsRepository.exportLogsInOriginalFormat().set(inOriginalFormat)
    }
}
