package com.f0x1d.logfox.feature.preferences.api.domain.crashes

interface SetCollectingForCrashTypeUseCase {
    operator fun invoke(crashTypeName: String, collecting: Boolean)
}
