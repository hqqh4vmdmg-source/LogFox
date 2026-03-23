package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCollectingForCrashTypeFlowUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : GetCollectingForCrashTypeFlowUseCase {

    override fun invoke(crashTypeName: String): Flow<Boolean> =
        crashesSettingsRepository.collectingForFlow(crashTypeName)
}
