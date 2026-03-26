package com.f0x1d.logfox.feature.preferences.impl.domain.logs

import com.f0x1d.logfox.feature.preferences.api.data.LogsSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetLogsExpandedUseCase
import javax.inject.Inject

internal class SetLogsExpandedUseCaseImpl @Inject constructor(
    private val logsSettingsRepository: LogsSettingsRepository,
) : SetLogsExpandedUseCase {

    override fun invoke(expanded: Boolean) = logsSettingsRepository.logsExpanded().set(expanded)
}
