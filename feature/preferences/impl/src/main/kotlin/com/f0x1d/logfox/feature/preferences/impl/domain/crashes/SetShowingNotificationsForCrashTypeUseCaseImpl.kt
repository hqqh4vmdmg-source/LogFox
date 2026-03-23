package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetShowingNotificationsForCrashTypeUseCase
import javax.inject.Inject

internal class SetShowingNotificationsForCrashTypeUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : SetShowingNotificationsForCrashTypeUseCase {

    override fun invoke(crashTypeName: String, showing: Boolean) {
        crashesSettingsRepository.setShowingNotificationsFor(crashTypeName, showing)
    }
}
