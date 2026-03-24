package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetCollectingForCrashTypeUseCase
import javax.inject.Inject

internal class SetCollectingForCrashTypeUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : SetCollectingForCrashTypeUseCase {

    override fun invoke(crashTypeName: String, collecting: Boolean) = crashesSettingsRepository.setCollectingFor(crashTypeName, collecting)
}
