package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetOpenCrashesOnStartupUseCase
import javax.inject.Inject

internal class SetOpenCrashesOnStartupUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : SetOpenCrashesOnStartupUseCase {

    override fun invoke(openOnStartup: Boolean) {
        crashesSettingsRepository.openCrashesOnStartup().set(openOnStartup)
    }
}
