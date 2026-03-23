package com.f0x1d.logfox.feature.preferences.api.domain.crashes

import kotlinx.coroutines.flow.Flow

interface GetShowingNotificationsForCrashTypeFlowUseCase {
    operator fun invoke(crashTypeName: String): Flow<Boolean>
}
