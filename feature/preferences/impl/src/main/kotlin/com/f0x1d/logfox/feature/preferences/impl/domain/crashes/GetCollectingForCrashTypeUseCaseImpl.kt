package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeUseCase
import javax.inject.Inject

internal class GetCollectingForCrashTypeUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : GetCollectingForCrashTypeUseCase {

    override fun invoke(crashTypeName: String): Boolean =
        crashesSettingsRepository.collectingFor(crashTypeName)
}
