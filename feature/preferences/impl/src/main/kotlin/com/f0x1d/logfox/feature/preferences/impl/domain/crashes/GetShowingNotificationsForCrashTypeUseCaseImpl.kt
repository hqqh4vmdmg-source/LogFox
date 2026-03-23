package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetShowingNotificationsForCrashTypeUseCase
import javax.inject.Inject

internal class GetShowingNotificationsForCrashTypeUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : GetShowingNotificationsForCrashTypeUseCase {

    override fun invoke(crashTypeName: String): Boolean =
        crashesSettingsRepository.showingNotificationsFor(crashTypeName)
}
