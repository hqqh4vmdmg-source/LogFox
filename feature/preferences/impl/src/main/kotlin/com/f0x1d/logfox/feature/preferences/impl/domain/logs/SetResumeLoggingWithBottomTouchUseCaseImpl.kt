package com.f0x1d.logfox.feature.preferences.impl.domain.logs

import com.f0x1d.logfox.feature.preferences.api.data.LogsSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetResumeLoggingWithBottomTouchUseCase
import javax.inject.Inject

internal class SetResumeLoggingWithBottomTouchUseCaseImpl @Inject constructor(
    private val logsSettingsRepository: LogsSettingsRepository,
) : SetResumeLoggingWithBottomTouchUseCase {

    override fun invoke(resumeWithTouch: Boolean) =
        logsSettingsRepository.resumeLoggingWithBottomTouch().set(resumeWithTouch)
}
