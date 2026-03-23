package com.f0x1d.logfox.feature.preferences.api.domain.crashes

interface SetOpenCrashesOnStartupUseCase {
    operator fun invoke(openOnStartup: Boolean)
}
