package com.f0x1d.logfox.feature.preferences.api.domain.crashes

interface GetShowingNotificationsForCrashTypeUseCase {
    operator fun invoke(crashTypeName: String): Boolean
}
