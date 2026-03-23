package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetShowingNotificationsForCrashTypeFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetShowingNotificationsForCrashTypeFlowUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : GetShowingNotificationsForCrashTypeFlowUseCase {

    override fun invoke(crashTypeName: String): Flow<Boolean> =
        crashesSettingsRepository.showingNotificationsForFlow(crashTypeName)
}
