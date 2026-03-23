package com.f0x1d.logfox.feature.preferences.impl.domain.crashes

import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetOpenCrashesOnStartupFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetOpenCrashesOnStartupFlowUseCaseImpl @Inject constructor(
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : GetOpenCrashesOnStartupFlowUseCase {

    override fun invoke(): Flow<Boolean> = crashesSettingsRepository.openCrashesOnStartup()
}
